/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.hypermine.hypersign.api;

import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.ServicesLogger;
import org.keycloak.models.*;
import java.util.HashMap; 
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.json.JSONObject;
import org.keycloak.wildfly.adduser.*;
import org.codehaus.jackson.map.ObjectMapper;
import io.hypermine.hypersign.service.AuthServerCaller;

/**
 * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
 */
public class HSResourceProvider implements RealmResourceProvider {

    class HSUserModel {
        String userId;
        boolean hasLoggedIn;
        public HSUserModel(String userId, boolean hasLoggedIn){
            this.userId = userId;
            this.hasLoggedIn = hasLoggedIn;
        }
    }

    class FResponse{
        public String status;
        public String data;
        public FResponse(){}
        public FResponse(String status, String data){
            this.status = status;
            this.data = data;
        }
    }
    
    enum Status {
        SUCCESS,
        FAIL
    }

    private static ServicesLogger logger = ServicesLogger.LOGGER;
    private static HashMap<String, HSUserModel> userSessionMap = new HashMap<>();// this is temporary
    private KeycloakSession session;
    
    public HSResourceProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public Object getResource() {
        return this;
    }

    @GET
    @Produces("text/plain; charset=utf-8")
    public String get() {
        logger.info("This is hypersign authenticator api call");
        String name = session.getContext().getRealm().getDisplayName();
        if (name == null) {
            name = session.getContext().getRealm().getName();
        }
        return "Hello " + name;
    }

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String register(String body) {
        logger.info("Register api called!");
        JSONObject json = null;
        String publicKey = "";
        String emaiid = "";
        String username = "";
        String companyid = "";
        UserModel newuser = null;
    	try{
            if(!body.isEmpty()){
                json = new JSONObject(body);
                if(json != null){
                    publicKey = json.getString("publickey");
                    emaiid = json.getString("email");
                    username = json.getString("username");
                    companyid = json.getString("companyid");
                    if(!publicKey.isEmpty() || !emaiid.isEmpty()) {
                        // saving the user in keycloak
                        newuser = this.session != null && this.session.userLocalStorage() != null
                                  ? this.session.userLocalStorage().addUser(this.session.getContext().getRealm(),publicKey,emaiid,true, true)
                                  : null;
                        if(newuser != null){
                            //saving the user in hs-auth-server
                            String reqstBody = "";
                            String url = "http://localhost:3000/register";
                            String responseFromAuthServer = AuthServerCaller.postApiCall(url,body);
                            json = new JSONObject(responseFromAuthServer);
                            if(json.getInt("status") == 0){
                                //error
                                throw new Exception(json.getString("message"));                 
                            }else{
                                //success
                                return this.formattedReponse(Status.SUCCESS, json.getString("message"));
                            }
                            //return this.formattedReponse(Status.SUCCESS, newuser.getId());
                        }else{
                            throw new Exception("Could not create the user");                               
                        }
                    }else {
                        throw new Exception("Publickey or emailId is null");
                    }            
                }else{
                    throw new Exception("Could not parse the body");
                }
            }else {
                throw new Exception("Request body is null");
            }
        }catch(Exception e){
            return this.formattedReponse(Status.FAIL,e.toString());            
        }    
    }

    @POST
    @Path("sign")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String sign(String body) {
        logger.info("sign api called!");
        JSONObject bodyObj = null;
        String sessionId = "";
        String publickey = "";
        String signature = "";
        HSUserModel user = null;
    	try{
            if(!body.isEmpty()){
                bodyObj = new JSONObject(body);
                if(bodyObj != null){
                    publickey = bodyObj.getString("publickey");
                    sessionId = bodyObj.getString("sessionId");
                    signature = bodyObj.getString("signature");
                    if(!publickey.isEmpty() || !sessionId.isEmpty() || !signature.isEmpty()) {
                        if(isSignatureValid(sessionId, publickey, signature)){
                            if (userSessionMap.containsKey(sessionId)){
                                user = new HSUserModel(publickey, true);
                                userSessionMap.put(sessionId, user);
                                return this.formattedReponse(Status.SUCCESS, "User authenticated");    
                            }else{
                                throw new Exception("Invalid session");                                   
                            }
                        }else{
                            throw new Exception("Invalid signature");                               
                        }   
                    }else {
                        throw new Exception("Publickey, sessionId or signature is null");
                    }            
                }else{
                    throw new Exception("Could not parse the body");
                }
            }else {
                throw new Exception("Request body is null");
            }
        }catch(Exception e){
            return this.formattedReponse(Status.FAIL,e.toString());            
        }    
    }

    @GET
    @Path("session")
    @Produces(MediaType.APPLICATION_JSON)
    public String getNewSession() {
        logger.info("session api called!");
        try{
            UUID gfg = UUID.randomUUID(); 
            String sessionId = gfg.toString();
            if(userSessionMap != null){
                userSessionMap.put(sessionId, null);
                return sessionId; //this.formattedReponse(Status.SUCCESS, sessionId);    
            }else{
                throw new Exception("userSessionMap is null");
            }
        }catch(Exception e){
            return this.formattedReponse(Status.FAIL,e.toString());            
        }
    }

    @GET
    @Path("listen/success/{sessionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String listenSuccess(@PathParam("sessionId") String sessionId) {
        logger.info("listen/success api called!");
        try{
            if(userSessionMap != null){
                if (userSessionMap.containsKey(sessionId) && userSessionMap.get(sessionId) != null){
                    HSUserModel user = userSessionMap.get(sessionId);
                    if (user != null && user.hasLoggedIn){
                        return user.userId; //this.formattedReponse(Status.SUCCESS, user.userId); 
                    }else{
                        throw new Exception("User not found or not validated");      
                    }
                }else{
                    throw new Exception("Invalid session");  
                }
            }else{
                throw new Exception("userSessionMap is null");
            }
        }catch(Exception e){
            return this.formattedReponse(Status.FAIL,e.toString());            
        } 
    }

    @GET
    @Path("listen/fail/{sessionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String listenFail(@PathParam("sessionId") String sessionId) { 
        logger.info("listen/fail api called!");
        try{
            if(userSessionMap != null){
                if (userSessionMap.containsKey(sessionId)){
                    userSessionMap.remove(sessionId);
                    return this.formattedReponse(Status.SUCCESS, "Session deleted");    
                }else{
                    throw new Exception("Invalid session");  
                }
            }else{
                throw new Exception("userSessionMap is null");
            }
        }catch(Exception e){
            return this.formattedReponse(Status.FAIL,e.toString());            
        } 
    }

    private String formattedReponse(Status status, String data){
        String respStr = "";
        try{
            FResponse response  = new FResponse();
            response.status = status.name();
            response.data = data;
            ObjectMapper Obj = new ObjectMapper();
            // JSONObject bodyObj = new JSONObject(response);
            respStr = Obj.writeValueAsString(response);            
        }
        catch(Exception e){
            respStr = e.toString();
        }   
        return respStr;
    }
    
    private Boolean isSignatureValid(String serssionId, String publickey, String signature){
        // call auth-server to validate this user.
        return true;
    }

    @Override
    public void close() {
    }

}
