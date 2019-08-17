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

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.ServicesLogger;

import javax.ws.rs.*;
// import javax.ws.rs.POST;
// import javax.ws.rs.Produces;

/**
 * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
 */
public class HSResourceProvider implements RealmResourceProvider {
    private static ServicesLogger logger = ServicesLogger.LOGGER;
    private static Boolean signature = false;
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
    @Path("listen")
    @Produces("text/plain; charset=utf-8")
    public String listen() {
        // logger.info("list api get called");
        // String name = session.getContext().getRealm().getDisplayName();
        // if (name == null) {
        //     name = session.getContext().getRealm().getName();
        // }
        return signature.toString();
    }

    @POST
    @Path("sign")
    @Produces("text/plain; charset=utf-8")
    public String post() {
      signature = true;
      return signature.toString();
    }

    @Override
    public void close() {
    }

}
