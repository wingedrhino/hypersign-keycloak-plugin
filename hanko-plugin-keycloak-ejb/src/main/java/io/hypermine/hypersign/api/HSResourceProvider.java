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
// import org.json.JSONObject;
import org.keycloak.wildfly.adduser.*;

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

    @POST
    @Path("register/{publicKey}/{emaiid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public String register(@PathParam("publicKey") String publicKey, @PathParam("emaiid") String emaiid) {
        try{
            if(!publicKey.isEmpty() && !emaiid.isEmpty()) {
                UserModel newuser = this.session.userLocalStorage().addUser(this.session.getContext().getRealm(),publicKey,emaiid,true, true);
                if(newuser != null){
                    return newuser.getId();
                }
            }else {
                return "false";
            }
        }catch(Exception e){
            return e.toString();
        }
        return "false";
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

    // @POST
    // @Path("register")
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces("application/json")
    // public String register(String body) {
    // 	try{
    //         if(!body.isEmpty()){
    //             JSONObject bodyObj = new JSONObject(body);
    //             String publicKey =  bodyObj.getString("publicKey");
    //             String emaiid =  bodyObj.getString("emaiid");
    //             if(!publicKey.isEmpty() && !emaiid.isEmpty()) {
    //                 UserModel newuser = this.session.userLocalStorage().addUser(this.session.getContext().getRealm(),publicKey,emaiid,true, true);
    //                 if(newuser != null){
    //                     return newuser.getId();
    //                 }
    //             }else {
    //                 return "Err : Publickey or email is null";
    //             }
                    
    //         }else {
    //             return "Err : Request body is null";
    //         }
    //     }catch(Exception e){
    //         return e.toString(); 
    //     }    
    //     return "true"; 
    // }

    private Boolean isSignatureValid(String serssionId, String publickey, String signature){
        return true;
    }

    @Override
    public void close() {
    }

}
