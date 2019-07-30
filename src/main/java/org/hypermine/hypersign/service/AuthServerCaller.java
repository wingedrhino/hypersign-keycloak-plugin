package org.hypermine.hypersign.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

import io.github.cdimascio.dotenv.Dotenv;

public class AuthServerCaller {

	private static String USER_AGENT = "Mozilla/5.0";
	private static String BaseUri;
	private static String CompanyId;
	private static String Token;

	public AuthServerCaller() {
		Dotenv dotenv = Dotenv.load();

		this.BaseUri = dotenv.get("HS_BASE_URI");

		this.CompanyId = dotenv.get("HS_COMPANY_ID");

		this.Token = dotenv.get("HS_TOKEN");
	}

	public static String getChallenge() {

		try {
			
			AuthServerCaller obj = new AuthServerCaller();

			return challengeService(obj.CompanyId, obj.Token);

		} catch (Exception e) {

			System.out.println("\nSending 'POST' request to URL : " + e);

			return "CompanyId";
		}
	}

	// HTTP POST request
	private static String challengeService(String companyId, String token) throws Exception {
		AuthServerCaller qrg = new AuthServerCaller();

		String url = qrg.BaseUri + "challenge";

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

	private static String verifyNotifyService(String companyId, String token, String signedRsv, String publicToken,
			String rawMsg, String direction) throws Exception {

		AuthServerCaller qrg = new AuthServerCaller();

		String url = qrg.BaseUri;

		if (direction == "verify") {

			url = url + "appLogin";

		} else if (direction == "notify") {

			url = url + "notifyTx";

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
