package org.hypermine.hypersign.authenticator;

import org.hypermine.hypersign.service.AuthServerCaller;
import org.json.JSONObject;
import org.json.JSONArray;

public class TestJson {

	public static void main(String[] args) {
		AuthServerCaller authserCaller = new AuthServerCaller();

		String response = authserCaller.getChallenge();
		// Object o1 = JSONValue.parse(response);
		JSONObject jsonObject = new JSONObject(response);

		JSONArray arr = jsonObject.getJSONArray("data");

		String challenge = "";
		for (int i = 0; i < arr.length(); i++) {

			JSONObject attributes = arr.getJSONObject(i).getJSONObject("attributes");

			challenge = attributes.getJSONObject("data").getString("challenge");
			
		}

		// jsonObject.getJSONObject("data").getString("pageName");

		// System.out.println(jsonObject.data.getString("data"));
		System.out.println("\nTes JSON : " + challenge);
	}
}