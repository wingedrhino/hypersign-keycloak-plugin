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
        JSONObject bodyObj = null;
        String publicKey = "";
        String emaiid = "";
        UserModel newuser = null;
    	try{
            if(!body.isEmpty()){
                bodyObj = new JSONObject(body);
                if(bodyObj != null){
                    publicKey = bodyObj.getString("publickey");
                    emaiid = bodyObj.getString("email");
                    if(!publicKey.isEmpty() && !emaiid.isEmpty()) {
                        newuser = this.session != null && this.session.userLocalStorage() != null
                                  ? this.session.userLocalStorage().addUser(this.session.getContext().getRealm(),publicKey,emaiid,true, true)
                                  : null;
                        if(newuser != null){
                            return this.formattedReponse(Status.SUCCESS, newuser.getId());
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

    @GET
    @Path("listen/{status}/{sessionId}")
    @Produces("text/plain; charset=utf-8")
    public String listen(@PathParam("sessionId") String sessionId, @PathParam("status") String status) {        
        String userId = "";
        try{
            if(userSessionMap != null){
                if(status.equals("success")){
                    if (userSessionMap.containsKey(sessionId) && userSessionMap.get(sessionId) != null){
                        HSUserModel user = userSessionMap.get(sessionId);
                        if (user != null && user.hasLoggedIn){
                            userId = user.userId;
                        }
                    }
                }else{
                    if (userSessionMap.containsKey(sessionId)){
                        userSessionMap.remove(sessionId);
                    }else{
                        return "Invalid user session. Call /session to create new one.";
                    }
                }
            }else{
                return "userSessionMap is null or empty";
            }
        }catch(Exception e){
            return e.toString();
        }
        return userId;        
    }

    @GET
    @Path("session")
    @Produces("text/plain; charset=utf-8")
    public String generateNewSession() {
        UUID gfg = UUID.randomUUID(); 
        String sessionId = gfg.toString();
        userSessionMap.put(sessionId, null);
        return sessionId;
    }

    @POST
    @Path("sign/{sessionId}/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("text/plain; charset=utf-8")
    public String postSignature(@PathParam("sessionId") String sessionId, @PathParam("userId") String userId) {
        HSUserModel user = null;
        Boolean authenticated = false;
        if(isSignatureValid(sessionId, userId, "")){
            if (userSessionMap.containsKey(sessionId)){
                user = new HSUserModel(userId, true);
                userSessionMap.put(sessionId, user);
                authenticated = true;
                // this.session.getContext
            }
        }           
        return authenticated.toString();
    }

    

    // will use this code once we fix the 3rd party api library use in this project.
    // @POST
    // @Path("sign")
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_JSON)
    // public HSUserModel postSignature(String body) {
    //     HSUserModel user = null;
    //     if(!body.isEmpty()){
    //         JSONObject bodyObj = new JSONObject(body);
            
    //         String serssionId = bodyObj.getString("sessionId");
    //         String userId = bodyObj.getString("sessionId");
    //         if(isSignatureValid(serssionId, userId, "")){
    //             if (!this.userSessionMap.containsKey(serssionId)){
    //                 user = new HSUserModel(userId, true);
    //                 this.userSessionMap.put(sessionId, user);

    //                 //
    //                 // this.session.getContext
    //             }
    //         }           
    //     }
    //     return user;
    // }

    

    private Boolean isSignatureValid(String serssionId, String publickey, String signature){
        return true;
    }

    @Override
    public void close() {
    }

}
