# DIF

### GET /

Test

```js
Response: "Hello Keycloak"

```

### POST /register

Call when user wants to register himself from mobile app.

```js
Request:
{
	"username":"",
	"email":"",
	"publickey" : "",
	"companyid" : ""
}

Response:
{
	"status":"SUCCESS",
	"data": "Validation link is sent to your email. Click to verify"
}

OR

{
	"status":"FAIL",
	"data": "Error message"
}
```

### GET session/

Get new hypersign session

```js
Response:
{
	"status":"SUCCESS",
	"data": "47064da2-7870-4126-af13-04491075e658"
}
```

### POST /sign

User scans the QR, sign and calls this API to get authenticated himself. 

```js
Request:
{
  "sessionId":"", // from /session api
  "publickey":"",
  "signature":""
}

Response:
{
	"status":"SUCCESS",
	"data": "User authenticated"
}

OR

{
	"status":"FAIL",
	"data": "Error message"
}
```


### GET listen/success/{sessionId}

Polling service on registration/login page will keep listening to the requested session to see if user has scaned and called `/sign` api or not.

```js
Response:
{
	"status":"SUCCESS",
	"data": "user_id_001"
}

OR

{
	"status":"FAIL",
	"data": "Error message"
}
```


### GET listen/fail/{sessionId}

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


