/**
 * 
 */
package com.restonza.util.service;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.restonza.dao.repository.HotelAnalyzerRepository;

/**
 * @author flex-grow developers
 *
 */
public class RestonzaUtility {
	
	public static final String WAITER_MSG = "Waiter Call at Table No:";
	
	public static final String WATER_MSG = "Water Call at Table No:";
	
	public static final String PLACE_ORDER_MSG = "New Order placed at Table No:";
	
	public static final String PLACE_ORDER= "Place Order";
	
	public static final String WAITER= "WAITER";
	
	public static final String WATER= "WATER";
	
	@Autowired
	private static HotelAnalyzerRepository hotelAnalyzerRepository;
	
	public static String getStatus(boolean status) {
		return status ? "2" : "1";
//		if (status) {
//			return "2";
//		}
//		return "1";
	}
	
	public static String getStatus(boolean status, boolean expire) {
		return expire ? "Exprired" : status ? "Active" : "Inactive";
	}
	
	public static String getActiveStatus(boolean status) {
		if (status) {
			return "active";
		}
		return "inactive";
	}
	
	public static boolean isNullOrEmpty(String string) {
		if (string != null) {
			if (!string.isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	public static String[] splitQRCode(String qrcode) {
		return qrcode.split("-");
	}
	
	public static boolean isObjectFieldsForNullOREmpty(Object compareObject, String[] field) throws IllegalArgumentException, IllegalAccessException {
		if (compareObject == null) {
			return true;
		} else {
			List<String> fieldName = Arrays.asList(field);
			for (Field f : compareObject.getClass().getFields()) {
				  f.setAccessible(true);
				  if (fieldName.contains(f.getName()) && f.get(compareObject) == null || f.get(compareObject).equals("")) {
					  return true;
				  }
				}
			return false;
		}
	}
	
	public static boolean isObjectFieldsForNullOREmpty(Object compareObject) throws IllegalArgumentException, IllegalAccessException {
		if (compareObject == null) {
			return true;
		} else {
			for (Field f : compareObject.getClass().getFields()) {
				  f.setAccessible(true);
				  if (f.get(compareObject) == null || f.get(compareObject).equals("")) {
					  return true;
				  }
				}
			return false;
		}
	}
	
	public static String formatOrderSummaries(String orderSummaries) {
		StringBuffer output = new StringBuffer();
		String ordsmarr[] = orderSummaries.split("\\|");
		for (int i = 0; i < ordsmarr.length; i++) {
			if (!ordsmarr[i].equals("")) {
				String subarr[] = ordsmarr[i].split("=");
				output.append(subarr[1].split(",")[0]);
				output.append("-");
				output.append(subarr[2]);
				output.append(",");
			}
		}
		return output.toString();
	}
	
	public static String formatOrderSummariesForCombineBill(String orderSummaries) {
		StringBuffer output = new StringBuffer();
		if(null != orderSummaries) {
			String ordsmarr[] = orderSummaries.split("\\|");
			for (int i = 0; i < ordsmarr.length; i++) {
				if (!ordsmarr[i].equals("")) {
					if (ordsmarr[i].contains("dishname=")) {
						String subarr[] = ordsmarr[i].split("=");
						output.append(subarr[1].split(",")[0]);
						output.append("-");
						output.append(subarr[2]);
						output.append(",");
					} else {
						output.append(ordsmarr[i]);
					}
				}
			}
		}
		return output.toString();
	}
	
	@Async
	public static void sendWebPush(List<String> hotelListIds,String type,String message) {
		try {
			   String jsonResponse;
			   
			   URL url = new URL("https://onesignal.com/api/v1/notifications");
			   HttpURLConnection con = (HttpURLConnection)url.openConnection();
			   con.setUseCaches(false);
			   con.setDoOutput(true);
			   con.setDoInput(true);

			   con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			   con.setRequestProperty("Authorization", "Basic OTA2OWZiMDUtMjEyNS00MGYyLTg2MDQtMGY3ZGQ3YWIwYTQy");
			   con.setRequestMethod("POST");

			   String strJsonBody = "{"
			                      +   "\"app_id\": \"8c2242d9-01e7-47ab-b246-d8f247873da3\","
			                      +   "\"include_player_ids\": "+hotelListIds+","
			                      +   "\"data\": {\"foo\": \"bar\"},"
			                      +   "\"contents\": {\"en\": "+message+"}"
			                      + "}";
			   
			   System.out.println("strJsonBody:\n" + strJsonBody);

			   byte[] sendBytes = strJsonBody.getBytes("UTF-8");
			   con.setFixedLengthStreamingMode(sendBytes.length);

			   OutputStream outputStream = con.getOutputStream();
			   outputStream.write(sendBytes);

			   int httpResponse = con.getResponseCode();
			   System.out.println("httpResponse: " + httpResponse);

			   if (  httpResponse >= HttpURLConnection.HTTP_OK
			      && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
			      Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
			      jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
			      scanner.close();
			   }
			   else {
			      Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
			      jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
			      scanner.close();
			   }
			   System.out.println("jsonResponse:\n" + jsonResponse);
			   
			} catch(Throwable t) {
			   t.printStackTrace();
			}
	}
	
	public static java.sql.Timestamp getSystime() {
		Date today = new Date();
		java.sql.Timestamp systime  = new java.sql.Timestamp(today.getTime());
		return systime;
	}
	
}
