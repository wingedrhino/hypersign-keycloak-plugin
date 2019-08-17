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

package io.hypermine.hypersign.authenticator;

import org.jboss.resteasy.spi.HttpResponse;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.common.util.ServerCookie;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class HyperSignAuthenticator implements Authenticator {

    public static final String CREDENTIAL_TYPE = "hypersign_qrcode";

    protected boolean hasCookie(AuthenticationFlowContext context) {
        Cookie cookie = context.getHttpRequest().getHttpHeaders().getCookies().get("HYPERSIGN_QRCODE_SOLVED");
        boolean result = cookie != null;
        return result;
    }

    /**********************************************************************************
     * This will check if browser has already tried solving the challenge before, if that
     * is the case then it wont again give the same challenge, rather it will bypass it
     * 
     * ********************************************************************************/
    @Override
    public void authenticate(AuthenticationFlowContext context) {
        if (hasCookie(context)) {
            context.success();
            return;
        }
         String newHsSession = "ssssss";
        //String response = QRCodeGenerator.createORLoginPage(context.getRealm().getDisplayName());
        Response challenge = context.form().setAttribute("loginMethod", "UAF").setAttribute("hsSession",newHsSession).createForm("hypersign-new.ftl");
        context.challenge(challenge);
        
        System.out.println("*********PRINTING THE ACTION URL THAT WILL BE USED BY HYPERSIGN MOBILE APP IN ORDER CALL THE KEYCLOAK ACTION************");
        System.out.println(context.getActionUrl(context.generateAccessCode()));
        context.form().setAttribute("hypersign", "This is for HyperSign testing");
    }

    /**********************************************************************************
     * This is the main method that will get called once user solves the challenge and 
     * click on the submit button.
     * 
     * ********************************************************************************/
    @Override
    public void action(AuthenticationFlowContext context) {
        System.out.println("*******I AM INSIDE THE ACTION CONTROLLER***********");

        //Added Static UserId
        String userId = "64ecaa15-4be3-49db-b56d-c171d021a346";
        UserModel user = context.getSession().users().getUserById(userId, context.getRealm());

        System.out.println("*******Looking for user session ***********");
        System.out.println(user.getUsername());

    	// UserModel user = session.users().getUserById(userId, context.getRealm());

        System.out.println("*******Got the session***********");

        // System.out.println(user.toString());
        System.out.println("finish printing user");
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        if (formData.containsKey("cancel")) {
            System.out.println("cancel");
            context.cancelLogin();
            return;
        }
    
        // boolean validated = validateAnswer(context);
        UserCredentialModel input = new UserCredentialModel();
        // input.setType(HyperSignCredentialProvider.QR_CODE);xs
        input.setValue("secret");

        boolean result = true;
        // try {
        //     result = session.userCredentialManager().isValid(context.getRealm(), user, input);
        // } catch (Exception e) {
        //     e.printStackTrace();
        //     // throw new AuthenticationFlowException("unknown user authenticated by the authenticator",
        //     //         AuthenticationFlowError.UNKNOWN_USER);
        // }
        if (result) {
            context.setUser(user);
            setCookie(context);
            context.success();
        } else {
            context.cancelLogin();
        }

        // if (!validated) {
        //     Response challenge =  context.form()
        //             .setError("badSecret")
        //             .createForm("hypersign.ftl");
        //     context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, challenge);
        //     return;
        // }
        // setCookie(context);
        // context.success();
    }
    /**********************************************************************************
     * Set the Hypersign cookie for 30 days.
     * 
     * ********************************************************************************/
   
    protected void setCookie(AuthenticationFlowContext context) {
        AuthenticatorConfigModel config = context.getAuthenticatorConfig();
        int maxCookieAge = 60 * 60 * 24 * 30; // 30 days
        if (config != null) {
            maxCookieAge = Integer.valueOf(config.getConfig().get("cookie.max.age"));

        }
        URI uri = context.getUriInfo().getBaseUriBuilder().path("realms").path(context.getRealm().getName()).build();
        addCookie("HYPERSIGN_QRCODE_SOLVED", "true",
                uri.getRawPath(),
                null, null,
                maxCookieAge,
                false, true);
    }

    public static void addCookie(String name, String value, String path, String domain, String comment, int maxAge, boolean secure, boolean httpOnly) {
        HttpResponse response = ResteasyProviderFactory.getContextData(HttpResponse.class);
        StringBuffer cookieBuf = new StringBuffer();
        ServerCookie.appendCookieValue(cookieBuf, 1, name, value, path, domain, comment, maxAge, secure, httpOnly);
        String cookie = cookieBuf.toString();
        response.getOutputHeaders().add(HttpHeaders.SET_COOKIE, cookie);
    }


    protected boolean validateAnswer(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        String secret = formData.getFirst("QR_CODE");
        UserCredentialModel input = new UserCredentialModel();
        input.setType(HyperSignCredentialProvider.QR_CODE);
        input.setValue(secret);
        return context.getSession().userCredentialManager().isValid(context.getRealm(), context.getUser(), input);
    }

    /**********************************************************************************
     * We are setting this as false since we dont need the user information as of now.
     * 
     * ********************************************************************************/
    @Override
    public boolean requiresUser() {
        return false;
    }

    /**********************************************************************************
     * Setting this as false sine we do not want to store any information
     * 
     * ********************************************************************************/
    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        //return false;
    	return session.userCredentialManager().isConfiguredFor(realm, user, HyperSignCredentialProvider.QR_CODE);
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        user.addRequiredAction(HyperSignRequiredAction.PROVIDER_ID);
    }

    @Override
    public void close() {

    }
}
