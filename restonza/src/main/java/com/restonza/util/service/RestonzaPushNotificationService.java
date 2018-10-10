/**
 * 
 */
package com.restonza.util.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

/**
 * @author flex-grow developers
 *
 */
@EnableAsync
@Service
public class RestonzaPushNotificationService {
	@Value("${gcm.projectid}")
	private String projectid;
	
	private final static Logger logger = LoggerFactory.getLogger(RestonzaPushNotificationService.class);
	
	public final static String AUTH_KEY_FCM = "AIzaSyBIy4ChvU4tyFOuQwP08K6-9s1EA-qgDAs";
	public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

	@Async
	public void sendPushNotification(String deviceToken, String notificationtitle, String notitficationbody)
	        throws IOException {
	    String result = "";
	    URL url = new URL(API_URL_FCM);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	    conn.setUseCaches(false);
	    conn.setDoInput(true);
	    conn.setDoOutput(true);

	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
	    conn.setRequestProperty("Content-Type", "application/json");

	    JSONObject json = new JSONObject();

	    json.put("to", deviceToken.trim());
	    JSONObject info = new JSONObject();
	    info.put("title", notificationtitle); // Notification title
	    info.put("body", notitficationbody); // Notification body
	    json.put("notification", info);
	    try {
	        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	        wr.write(json.toString());
	        wr.flush();

	        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

	        String output;
	        while ((output = br.readLine()) != null) {
	            System.out.println(output);
	        }
	        result = "SUCCESS";
	    } catch (Exception e) {
	        result = "FAILURE";
	    }
	    logger.debug(result);
	}

}
