package io.hypermine.hypersign.authenticator;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

public class QRCodeGenerator {

	/**********************************************************************************
     * This will create base64 which is a encoded text embedded in the QR code.
     * 
     * ********************************************************************************/
	public static String createORLoginPage(String qrJSON) {

		// String qrCodeText = relamName + '-' + challenge;
		ByteArrayOutputStream bout = QRCode.from(qrJSON).withSize(800, 800).to(ImageType.PNG).stream();
		String encoded = Base64.getEncoder().encodeToString(bout.toByteArray());
		String loginTemp = "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css\"><div class=\"container\"> <div class=\"height--high\" style=\" height: 93vh;\"> <div class=\"row\"> <nav class=\"navbar\"> <div class=\"container-fluid\"> <div class=\"navbar-header\"> <a class=\"navbar-brand\" href=\"#\"> <img alt=\"Brand\" class=\"\" src=\"https://i.ibb.co/n0mRFG5/HS-logo-Key-C.png\"> </a> </div></div></nav> </div><div class=\"row\"> <div class=\"container container-table\" style=\"margin-top: 15%\"> <div class=\"row vertical-center-row\"> <div class=\"text-center col-md-4 col-md-offset-4\"> <div class=\"placeholder--text--header\"> Scan the QR code with Your Mobile App to Login </div><div> <img alt=\"QR\" class=\"qr-code\" style=\"max-height:150px;\" src=\"data:image/png;base64,"
				+ encoded
				+ "\"/> </div><div class=\"placeholder--text--footer\"> <a>CANT SCAN?</a> </div></div></div></div></div></div><div id=\"footer\"> <div class=\"\" style=\"\"> <p class=\"text-muted\">Secured By Hypersign.</p></div></div></div><style>/* CSS used here will be applied after bootstrap.css */body{background: url('https://i.ibb.co/s9mqdDJ/login-main.png') no-repeat center center fixed; -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover;}.qr-code{border: 1px solid #696A6A; border-radius: 2%; padding: 5px; background: white;}.container-table{display: table;}.vertical-center-row{display: table-cell; vertical-align: middle;}.placeholder--text--header{padding-bottom: 20px;}.placeholder--text--footer{padding-top: 20px;}#footer{bottom: 0; width: 100%; height: 60px; text-align: right;}</style>";
		return encoded;
	}

}
