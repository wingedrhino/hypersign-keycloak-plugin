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
import javax.ws.rs.*;
import java.util.UUID;
// import javax.ws.rs.POST;
// import javax.ws.rs.Produces;

/**
 * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
 */
public class HSResourceProvider implements RealmResourceProvider {

    class HSUserModel {
        String userId;
        Boolean hasLoggedIn;
        public HSUserModel(String userId, Boolean hasLoggedIn){
            this.userId = userId;
            this.hasLoggedIn = hasLoggedIn;
        }
    }

    private static ServicesLogger logger = ServicesLogger.LOGGER;
    private static HashMap<String, HSUserModel> userSessionMap =  new HashMap<>();// this is temporary
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
    @Path("auth/{sessionId}")
    @Produces("text/plain; charset=utf-8")
    public String listen(@PathParam("sessionId") String sessionId) {
        Boolean userAuthenticated = false;
        if (userSessionMap.containsKey(sessionId) && userSessionMap.get(sessionId) != null){
            HSUserModel user = userSessionMap.containsKey(sessionId);
            if (user != null && user.hasLoggedIn){
                userAuthenticated =  true;
            }
        }
        return userAuthenticated;
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
    @Path("sign")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public HSUserModel postSignature(String body) {
        String serssionId = body.sessionId;
        String userId = body.userId;
        HSUserModel user = null;
        if(isSignatureValid(serssionId, userId, "")){
            if (!userSessionMap.containsKey(serssionId)){
                user = new HSUserModel(userId, true);
                userSessionMap.put(sessionId, user);
            }
        }
        
        return user;
    }

    private Boolean isSignatureValid(String serssionId, String publickey, String signature){
        return true;
    }

    @Override
    public void close() {
    }

}
