package org.hypermine.hypersign.authenticator;

import java.io.IOException;

import com.google.zxing.WriterException;

public class TestJson {

	public static void main(String[] args) {
		String response="";
		try {
			response = QRCodeGenerator.createQRLoginPage("Hypermine");
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// // Object o1 = JSONValue.parse(response);
		// JSONObject jsonObject = new JSONObject(response);

		// JSONArray arr = jsonObject.getJSONArray("data");

		// String challenge = "";
		// for (int i = 0; i < arr.length(); i++) {

		// 	JSONObject attributes = arr.getJSONObject(i).getJSONObject("attributes");

		// 	challenge = attributes.getJSONObject("data").getString("challenge");
			
		// }

		// jsonObject.getJSONObject("data").getString("pageName");

		// System.out.println(jsonObject.data.getString("data"));
		System.out.println("\nTes JSON : " + response);
	}
}