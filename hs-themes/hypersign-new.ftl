<#import "template.ftl" as layout>
<@layout.registrationLayout displayInfo=true; section>
    <#if section = "title">
        <#if loginMethod = "PASSWORD">
            Sign in with Password
        <#elseif loginMethod = "UAF">
            Sign in with HyperSign Authenticator
        <#elseif loginMethod = "WEBAUTHN">
            Sign in with WebAuthn
        </#if>
    <#elseif section = "header">
        <#if loginMethod = "PASSWORD">
            Sign in with Password
        <#elseif loginMethod = "UAF">
            Sign in with HyperSign Authenticator
        <#elseif loginMethod = "WEBAUTHN">
            Sign in with WebAuthn
        </#if>
    <#elseif section = "form">
        <#if loginMethod = "UAF">
            <p>Please confirm your authentication with the HyperSign Authenticator you registered with your account. Scan Qrcode.</p>
            <img alt="QR" class="qr-code" style="max-height:150px;"
                src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAyAAAAMgAQAAAADzCzvFAAACPklEQVR42u3dQY7CMAwF0Nwg979lbtDZjaCxnRSYWdCXBRJF4W2/Etttxz+sBoFAIBAIBAKBQCAQCAQCgUAgEAgEAoFAIBAIBAKBQCAQyJchowWrP//0+7Vv7IJAIBAIBAKBZEh/jmWnJ2P+g41dEAgEAoFAIJAQecxxowx3p895FwQCgUAgEAjkEvL4vP4KgUAgEAgEAvkI0qNzNQgEAoFAIBDIa0j4ZEwFZSf8EzemEAgEAoFAIDdCwjL8sfr8RIcABAKBQCAQyI2QYo2kXbLvbIZAIBAIBAKBROFunkJxKhkLbyzDfkoIBAKBQCAQSBbuwuw28gbK8FytQyAQCAQCgUBWSJHpsj/LmikhEAgEAoFAIHW4mzPavDvLdz285IRAIBAIBAKBrA7V5pR3vNs0CYFAIBAIBAJJx+FnRWRts6IfAoFAIBAIBJIMCRtJjpsP3tpErS4zIRAIBAKBQCBBH2TWHNmmQfjFFggEAoFAIBBI9jKicBB++NrtcIzrsT5Ug0AgEAgEArk1kq1sCOtYxjoIBAKBQCAQSBLuspcR1e2SV5omIRAIBAKBQCBp+2NWXNaSmn0IBAKBQCAQyGZ9V3Y5WZfwp3PxIRAIBAKBQCDbSDas4tWmSQgEAoFAIBBIGu5qsBelZBAIBAKBQCCQi5eZR1QsVl9sQiAQCAQCgUBCpCjeD6extiT9QSAQCAQCgUDql0D+yYJAIBAIBAKBQCAQCAQCgUAgEAgEAoFAIBAIBAKBQCAQCOQLkB85N/YsxzpcZAAAAABJRU5ErkJggg==" />
            <form action="${url.loginAction}" style="display:hidden" class="${properties.kcFFormClass!}"
                  id="kc-hs-login-form"
                  method="post">
                <input type="hidden" name="loginMethod" value="UAF" />
                <input type="hidden" name="sessionId" id="hsSession" value="${hsSession}" />
                <input type="hidden" name="userId" id="hsUserId" value="" />
            </form>
        </#if>
    </#if>
<script src="https://cdnjs.cloudflare.com/ajax/libs/axios/0.19.0/axios.min.js"></script>
<style>
  /* CSS used here will be applied after bootstrap.css */
#kc-header-wrapper{
    background: url('https://i.ibb.co/n0mRFG5/HS-logo-Key-C.png') no-repeat center center fixed !important;
}
.login-pf body{
  background: url('https://i.ibb.co/s9mqdDJ/login-main.png') no-repeat center center fixed !important;
  -webkit-background-size: cover;
  -moz-background-size: cover;
  -o-background-size: cover;
  background-size: cover;
}
  .qr-code {
    border: 1px solid #696A6A;
    border-radius: 2%;
    padding: 5px;
    background: white;
  }

  .container-table {
    display: table;
  }

  .vertical-center-row {
    display: table-cell;
    vertical-align: middle;
  }

  .placeholder--text--header {
    padding-bottom: 20px;
  }

  .placeholder--text--footer {
    padding-top: 20px;
  }

  #footer {
    bottom: 0;
    width: 100%;
    height: 60px;
    text-align: right;
  }
</style>
<script>
const start = () => {
  console.log('starting polling...')
  const pathname = window.location.pathname; 
  let realm = pathname.substring(pathname.lastIndexOf("ms/") + 3, pathname.lastIndexOf("/protocol"));
  let baseUrl = window.location.origin;
  let timerId= setInterval(() => {
    console.log('tick')
    let ssSessionId = document.getElementById('hsSession').value;
    const url  = baseUrl + "/auth/realms/" + realm + "/hypersign/listen/success/" + ssSessionId;
    axios.get(url)
    .then(response => {
      if(response.data != ""){
        clearInterval(timerId);
        document.getElementById('hsUserId').value = response.data;
        document.getElementById('kc-hs-login-form').submit();
      }
      console.log(response.data)
    })
    .catch(error => {
      console.log(error);
    });
  }, 3000)
}

start();
</script>
</@layout.registrationLayout>