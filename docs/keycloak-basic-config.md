## Configure a client

- Click on `Clients` menue item, press the `Create` button to create a new client. 
- Give clientid as `hs-playground` and RootUrl as `https://hypermine-bc.github.io/` and press on `Save` button.

## Configure HS-APIs

- Click on `Clients` menue item, press the `Create` button to create a new client. 
- Give clientid as `hs-api` and RootUrl as `/keycloak/auth/realms/master/hypersign` and press on `Save` button.
- Enter base url as `/keycloak/auth/realms/master/hypersign` and press save.
- Test the api by hitting `GET /` endpoint.
   
   ```
   Request Url : https://dev.hs.hypermine.in/keycloak/auth/realms/master/hypersign/
   Response : Hello Keycloak
   ```
## Add HS authenticator in keycloak

- Login to admin console.  Hit browser refresh if you are already logged in so that the new providers show up.
- Go to the `Authentication` menu item and go to the `Flows` tab, you will be able to view the currently defined flows (*Auth Types*).  
- You cannot modify an built in flows, so, to add the Hypersign authenticator, you have to copy an existing flow or create your own.  
- Select `Browser` from the dropdown and click on `Copy` button to copy the `Browser` flow. Rename it to `Hypersign`
- In your copy, click the `Actions` menu item and "Add Execution".  Pick Secret Question
- Next you have to register the required action that you created. Click on the Required Actions tab in the Authenticaiton menu.
- Click on the Register button and choose your new Required Action.
- Your new required action should now be displayed and enabled in the required actions list.

## Configure HS authenticator with Client

- Click on the click where you want to add Hypersign authenticator
- Look for `Authentication Flow Override`
- Coose `Hypersign` as browser flow as well as Direct Grant Flow and press save.