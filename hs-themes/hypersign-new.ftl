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
            <img alt="QR" class="qr-code" style="max-height:300px;"src="data:image/png;base64,${hsQr}" />
            <form action="${url.loginAction}" style="display:hidden" class="${properties.kcFFormClass!}"
                  id="kc-hs-login-form"
                  method="post">
                <input type="hidden" name="loginMethod" value="UAF" />
                <input type="hidden" name="sessionId" id="hsSession" value="${hsSession}" />
                <input type="hidden" name="ksSessionId" id="ksSessionId" value="${ksSessionId}" />
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
    let ssSessionId = document.getElementById('ksSessionId').value;
    const url  = baseUrl + "/keycloak/auth/realms/" + realm + "/hypersign/listen/success/" + ssSessionId;
    console.log(url);
    axios.get(url)
    .then(resp => {
      const response =  resp.data;
      if(response && response.status != "FAIL"){
        clearInterval(timerId);
        document.getElementById('hsUserId').value = response.data;
        document.getElementById('kc-hs-login-form').submit();
      }else{
        console.log(response);
      }
    })
    .catch(error => {
      console.log(error);
    });
  }, 3000)
}

start();
</script>
</@layout.registrationLayout>