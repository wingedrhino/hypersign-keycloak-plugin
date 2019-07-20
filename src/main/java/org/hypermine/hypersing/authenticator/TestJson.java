package org.hypermine.hypersing.authenticator;

import org.json.JSONObject;

public class TestJson {

	public static void main(String[] args) {
        String response = QRCodeGenerator.getChallenge();
        // Object o1 = JSONValue.parse(response);
        JSONObject jsonObject = new JSONObject(response);
        // System.out.println(jsonObject.data.getString("data"));
        System.out.println("\nTes JSON : " + response);
    }
}