package org.hypermine.hypersing.authenticator;

// import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import io.github.cdimascio.dotenv.Dotenv;
// import org.json.JSONArray;
// import org.json.JSONException;
import org.json.JSONObject;
// import org.json.JSONString;

public class QRCodeGenerator {
	// private static HttpURLConnection connection;
	private static String USER_AGENT = "Mozilla/5.0";
	private static String BaseUri;
	private static String CompanyId;
	private static String Token;
	
	QRCodeGenerator(){

		Dotenv dotenv = Dotenv.load();

		this.BaseUri = dotenv.get("HS_BASE_URI");

		this.CompanyId = dotenv.get("HS_COMPANY_ID");

		this.Token = dotenv.get("HS_TOKEN");

   	}

	public  static String createORLoginPage(String relamName) {
			
		String challenge = getChallenge();

		String qrCodeText = relamName+'-'+challenge;

		ByteArrayOutputStream bout =QRCode.from(qrCodeText).withSize(800, 800).to(ImageType.PNG).stream();

		String encoded = Base64.getEncoder().encodeToString(bout.toByteArray());        
		
		String loginTemp="<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css\"><div class=\"container\"> <div class=\"height--high\" style=\" height: 93vh;\"> <div class=\"row\"> <nav class=\"navbar\"> <div class=\"container-fluid\"> <div class=\"navbar-header\"> <a class=\"navbar-brand\" href=\"#\"> <img alt=\"Brand\" class=\"\" src=\"https://i.ibb.co/n0mRFG5/HS-logo-Key-C.png\"> </a> </div></div></nav> </div><div class=\"row\"> <div class=\"container container-table\" style=\"margin-top: 15%\"> <div class=\"row vertical-center-row\"> <div class=\"text-center col-md-4 col-md-offset-4\"> <div class=\"placeholder--text--header\"> Scan the QR code with Your Mobile App to Login </div><div> <img alt=\"QR\" class=\"qr-code\" style=\"max-height:150px;\" src=\"data:image/png;base64,"+encoded+"\"/> </div><div class=\"placeholder--text--footer\"> <a>CANT SCAN?</a> </div></div></div></div></div></div><div id=\"footer\"> <div class=\"\" style=\"\"> <p class=\"text-muted\">Secured By Hypersign.</p></div></div></div><style>/* CSS used here will be applied after bootstrap.css */body{background: url('https://i.ibb.co/s9mqdDJ/login-main.png') no-repeat center center fixed; -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover;}.qr-code{border: 1px solid #696A6A; border-radius: 2%; padding: 5px; background: white;}.container-table{display: table;}.vertical-center-row{display: table-cell; vertical-align: middle;}.placeholder--text--header{padding-bottom: 20px;}.placeholder--text--footer{padding-top: 20px;}#footer{bottom: 0; width: 100%; height: 60px; text-align: right;}</style>";
		
		return loginTemp;
	}

	public  static String getChallenge() {

		try {
			QRCodeGenerator obj = new QRCodeGenerator();

			return challengeService(obj.CompanyId, obj.Token);

		} catch (Exception e) {

			System.out.println("\nSending 'POST' request to URL : " + e);

			return "CompanyId";
		}
	}

	// HTTP POST request
	private  static String challengeService(String companyId, String token) throws Exception {
		QRCodeGenerator qrg = new QRCodeGenerator();

		String url = qrg.BaseUri+"challenge";

		StringBuilder response = new StringBuilder();
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// Send post request
		con.setDoOutput(true);

		//Request Body 
		JSONObject data = new JSONObject();
		JSONObject attributes = new JSONObject();
		JSONObject company = new JSONObject();

		company.put("companyId", companyId);
		attributes.put("attributes",company);
		data.put("data", attributes);

		String jsonInputString = data.toString();
		// System.out.println(jsonInputString);
		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("token", token);
		con.setRequestProperty("content-length", "181");
		con.setRequestProperty("access-control-allow-origin", "*");
		con.setRequestProperty("access-control-allow-headers", "Content-Type");
		
		try (OutputStream os = con.getOutputStream()) {
			byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
			os.write(input);
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
			
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			// System.out.println(response.toString());
		}

		return response.toString();
	}

	private static String verifyNotifyService(String companyId, String token, String signedRsv, String publicToken, String rawMsg, String direction) throws Exception {
		
		QRCodeGenerator qrg = new QRCodeGenerator();
		
		String url = qrg.BaseUri;

		if(direction=="verify"){

			 url = url+"appLogin";

		} else if(direction == "notify"){

			 url = url+"notifyTx";

		}

		StringBuilder response = new StringBuilder();
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// Send post request
		con.setDoOutput(true);

		// Request Body
		JSONObject data = new JSONObject();
		JSONObject attributes = new JSONObject();
		JSONObject company = new JSONObject();

		company.put("companyId", companyId);
		company.put("signedRsv", signedRsv);
		company.put("publicToken", publicToken);
		company.put("rawMsg", rawMsg);

		attributes.put("attributes", company);
		data.put("data", attributes);

		String jsonInputString = data.toString();
		// System.out.println(jsonInputString);
		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("token", token);
		con.setRequestProperty("content-length", "181");
		con.setRequestProperty("access-control-allow-origin", "*");
		con.setRequestProperty("access-control-allow-headers", "Content-Type");

		try (OutputStream os = con.getOutputStream()) {
			byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
			os.write(input);
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {

			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			// System.out.println(response.toString());
		}

		return response.toString();
	}

}
