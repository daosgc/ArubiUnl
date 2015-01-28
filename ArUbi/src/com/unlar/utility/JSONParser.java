package com.unlar.utility;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";

	// constructor
	public JSONParser() {

	}

	
	/**
	 * Metodo para Obtener un Objeto JSON desde una url haciendo HTTP POST o GET
	 * @param url
	 * @param method
	 * @param params
	 * @return
	 */
	public JSONObject makeHttpRequest(String url, String method,
			List<NameValuePair> params) {

		// Making HTTP request
		try {
			
			// check for request method
			if(method == "POST"){
				// request method is POST
				// defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();
				/*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
				HttpPost httpPost = new HttpPost(url);
				/*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
				httpPost.setEntity(new UrlEncodedFormEntity(params));
				/*Anexamos los parametros al objeto para que al enviarse al servidor envien los datos que hemos añadido*/
				HttpResponse httpResponse = httpClient.execute(httpPost);
				/*Finalmente ejecutamos enviando la info al server*/
				HttpEntity httpEntity = httpResponse.getEntity();
				/*y obtenemos una respuesta*/

				is = httpEntity.getContent();
				
			}else if(method == "GET"){
				// request method is GET
				// defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(params, "utf-8");
				//concatenamos la direccion url con los parametros enviados a esa url
				url += "?" + paramString;
				//Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http
				HttpGet httpGet = new HttpGet(url);
				//Anexamos los parametros al objeto para que al enviarse al servidor envien los datos que hemos añadido
				HttpResponse httpResponse = httpClient.execute(httpGet);
				//Finalmente ejecutamos enviando la info al server
				HttpEntity httpEntity = httpResponse.getEntity();
				//y obtenemos una respuesta
				is = httpEntity.getContent();
				
				//verificamos si se obtuvo respuesta
				/*String respStr = EntityUtils.toString(httpEntity);
				if(respStr.equals("true"))*/
				}			
			

		} catch (UnsupportedEncodingException e) {
			Log.e("JSONCONECCION", "UnsupportedEcondingException " + e.toString());
			//e.printStackTrace();
		} catch (ClientProtocolException e) {
			Log.e("JSONCONECCION", "ClientProtocolException " + e.toString());
			//e.printStackTrace();
		} catch (IOException e) {
			Log.e("JSONCONECCION", "IOException " + e.toString());
			//e.printStackTrace();
		}
		
		//Convierte un InputStream en un String
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}
}
