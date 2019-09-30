# DIF

### GET dev.hs.hypermine.in/keycloak/auth/realms/master/hypersign/

Test

```js
Response: "Hello Keycloak"

```

### POST dev.hs.hypermine.in/keycloak/auth/realms/master/hypersign/register

Call when user wants to register himself from mobile app.

```js
Request:
{
	"username":"newuser vishwas",
	"email":"bhushan@imaginea.com",
	"publickey" : "0x4b9ee8840b254bf1ec45df7802585042ac8b7f45",
	"companyid" : "playground"
}

Response:
{
    "status": "SUCCESS",
    "data": "Sucessfully Registered"
}

OR

{
	"status":"FAIL",
	"data": "Error message"
}
```

### POST dev.hs.hypermine.in/keycloak/auth/realms/master/hypersign/session

Get new hypersign session

```json
REquest:
{
	"kcSessionId":"my_new_session",
	"companyId":"playground"
}

Response:
{
	"status":"SUCCESS",
	"data": "47064da2-7870-4126-af13-04491075e658"
}
```

### POST dev.hs.hypermine.in/keycloak/auth/realms/master/hypersign/sign/

User scans the QR, sign and calls this API to get authenticated himself. 

```json
Request:
{
  "companyId": "playground",
  "publicKey": "0x4b9ee8840b254bf1ec45df7802585042ac8b7f45",
  "signedRsv": "{\"r\":{\"type\":\"Buffer\",\"data\":[252,54,228,125,123,61,165,211,220,106,188,36,132,83,24,198,222,145,14,60,130,34,7,130,242,181,168,104,39,193,139,168]},\"s\":{\"type\":\"Buffer\",\"data\":[6,1,54,181,191,79,237,172,147,118,175,34,9,190,1,74,24,18,44,149,49,111,23,238,72,153,98,207,249,42,167,16]},\"v\":27}",
  "rawMsg": "Quick Brown Fox Jump Over a Lazy Dog",
  "ksSessionId" : "my_new_session123",
  "challenge" : "62d19600-ccaa-11e9-8e6e-c30f5e42b2be"
}

Response:
{
    "status": "SUCCESS",
    "data": "User authenticated"
}

OR

{
	"status":"FAIL",
	"data": "Error message"
}
```


### GET dev.hs.hypermine.in/keycloak/auth/realms/master/hypersign/listen/success/{sessionId}

Polling service on registration/login page will keep listening to the requested session to see if user has scaned and called `/sign` api or not.

```js
Response:
{
	"status":"SUCCESS",
	"data": "0x4b9ee8840b254bf1ec45df7802585042ac8b7f45"
}

OR

{
	"status":"FAIL",
	"data": "Error message"
}
```


### GET dev.hs.hypermine.in/keycloak/auth/realms/master/hypersign/listen/fail/{sessionId}

If user is not valid after calling `/sign`, then call this api to delete that session.

```js
Response:
{
	"status":"SUCCESS",
	"data": "Session deleted"
}

OR

{
	"status":"FAIL",
	"data": "Error message"
}
```
### GET /register/validate

User clicks on validation link in email

```js
Request:
{
	"sessionid":""
}

Response:
{
	"status":"SUCCESS",
	"data": "You are verified."
}

OR

{
	"status":"FAIL",
	"data": "The link is expired"
}
```




### POST https://dev.hs.hypermine.in/hsauth/company

User clicks on validation link in email

```js
Request:
{
	"data" : {
		"attributes" : {
			"companyId":"master",
			"companyName": "Hypermine",
			"publicToken":"publicToken",
			"other":"others"		
		}
	}
}

Response:
{
    "data": {
        "_id": "5d9094bd76a761316aaa8570",
        "companyId": "master",
        "companyName": "Hypermine",
        "publicToken": "publicToken",
        "other": "others",
        "createdAt": "2019-09-29T11:25:49.480Z",
        "updatedAt": "2019-09-29T11:25:49.480Z"
    },
    "message": "Company Registered"
}
```


