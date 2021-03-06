<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<div class="container">
  <div class="height--high" style=" height: 93vh;">
    <div class="row">
      <nav class="navbar">
        <div class="container-fluid">
          <div class="navbar-header"> <a class="navbar-brand" href="#"> <img alt="Brand" class=""
                src="https://i.ibb.co/n0mRFG5/HS-logo-Key-C.png"> </a> </div>
        </div>
      </nav>
    </div>
    <div class="row">
      <div class="container container-table" style="margin-top: 15%">
        <div class="row vertical-center-row">
          <div class="text-center col-md-4 col-md-offset-4">
            <div class="placeholder--text--header"> Scan the QR code with Your Mobile App to Login </div>
            <div> <img alt="QR" class="qr-code" style="max-height:150px;"
                src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAyAAAAMgAQAAAADzCzvFAAACPklEQVR42u3dQY7CMAwF0Nwg979lbtDZjaCxnRSYWdCXBRJF4W2/Etttxz+sBoFAIBAIBAKBQCAQCAQCgUAgEAgEAoFAIBAIBAKBQCAQyJchowWrP//0+7Vv7IJAIBAIBAKBZEh/jmWnJ2P+g41dEAgEAoFAIJAQecxxowx3p895FwQCgUAgEAjkEvL4vP4KgUAgEAgEAvkI0qNzNQgEAoFAIBDIa0j4ZEwFZSf8EzemEAgEAoFAIDdCwjL8sfr8RIcABAKBQCAQyI2QYo2kXbLvbIZAIBAIBAKBROFunkJxKhkLbyzDfkoIBAKBQCAQSBbuwuw28gbK8FytQyAQCAQCgUBWSJHpsj/LmikhEAgEAoFAIHW4mzPavDvLdz285IRAIBAIBAKBrA7V5pR3vNs0CYFAIBAIBAJJx+FnRWRts6IfAoFAIBAIBJIMCRtJjpsP3tpErS4zIRAIBAKBQCBBH2TWHNmmQfjFFggEAoFAIBBI9jKicBB++NrtcIzrsT5Ug0AgEAgEArk1kq1sCOtYxjoIBAKBQCAQSBLuspcR1e2SV5omIRAIBAKBQCBp+2NWXNaSmn0IBAKBQCAQyGZ9V3Y5WZfwp3PxIRAIBAKBQCDbSDas4tWmSQgEAoFAIBBIGu5qsBelZBAIBAKBQCCQi5eZR1QsVl9sQiAQCAQCgUBCpCjeD6extiT9QSAQCAQCgUDql0D+yYJAIBAIBAKBQCAQCAQCgUAgEAgEAoFAIBAIBAKBQCAQCOQLkB85N/YsxzpcZAAAAABJRU5ErkJggg==" />
            </div>
            <div class="placeholder--text--footer"> <a>CANT SCAN?</a> </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div id="footer">
    <div class="" style="">
      <p class="text-muted">Secured By Hypersign.</p>
    </div>
  </div>
</div>
<style>
  /* CSS used here will be applied after bootstrap.css */
  body {
    background: url('https://i.ibb.co/s9mqdDJ/login-main.png') no-repeat center center fixed;
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

<script src="https://cdnjs.cloudflare.com/ajax/libs/axios/0.19.0/axios.min.js"></script>

<script>
const start = () => {
  console.log('starting polling...')
  let timerId= setInterval(() => {
    console.log('tick')
    axios.get('http://localhost:8080/auth/realms/master/hypersign/listen')
    .then(response => {
      if(response.data)
        clearInterval(timerId);

      console.log(response.data)
    })
    .catch(error => {
      console.log(error);
    });
  }, 3000)
}

start();
</script>