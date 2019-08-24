# DIF

### /

Test

```js
Method: GET

Response: "Hello Keycloak"

```

### /register

Call when user wants to register himself from mobile app.

```js
Method: POST

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

### /register/validate

User clicks on validation link in email

```js
Method: GET

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


### /access

User clicks on validation link in email

```js
Method: POST

Request:
{
	"clientid":"",     // from QR
  "companyid":"",   // from mobile
  "publickey":"",  // from mobile 
  "sessionid":"", // from QR
  "signature":"" // signed using private key
}

Response:
{
	"status":"SUCCESS",
	"data": ""
}

OR

{
	"status":"FAIL",
	"data": "Error message"
}
```


### /access/status

User clicks on validation link in email

```js
Method: GET

Request:
{
  "sessionid":"" // on QR  
}

Response:
{
	"status":"SUCCESS",
	"data": "User validated"
}

OR

{
	"status":"FAIL",
	"data": "Error message"
}
```

### /sign

User scans the QR, sign and calls this API to get authenticated himself. 

```js
Method: POST

Request:
{
  "sessionid":"", // from /session api
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

### /listen/login

Polling service will keep listening to the requested session from login page

```js
Method: POST

Request:
{
  "sessionid":"", // from /session api
}

Response:
{
	"status":"SUCCESS",
	"data": "publickey"
}

OR

{
	"status":"FAIL",
	"data": "Error message"
}
```

### /listen/register

Polling service will keep listening to the requested session from registration page

```js
Method: POST

Request:
{
  "sessionid":"", // from /session api
}

Response:
{
	"status":"SUCCESS",
	"data": "publickey"
}

OR

{
	"status":"FAIL",
	"data": "Error message"
}
```


