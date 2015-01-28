package com.unlar.modelo.dao;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.unlar.modelo.Bloque;
import com.unlar.utility.JSONParser;

import android.util.Log;

public class Bloque_Dao {
	private static final String URL_ALL_LISTA_BLOQUES = "http://arubiunl.com/serverforArUbi/consultar_Bloques.php";
	private static final String URL_BLOQUE_SEGUN_ID = "http://arubiunl.com/serverforArUbi/consultar_Bloques_idBloque.php";
	private static final String URL_BLOQUES_REALIDAD_AUMENTADA = "http://arubiunl.com/serverforArUbi/consultar_Bloques_RealidadAumentada.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_BLOQUE = "bloque";
	private static final String TAG_BLOQUES = "bloques";
	private static final String TAG_ID = "id";
	private static final String TAG_NUMERO = "numero";
	private static final String TAG_CODIGO = "codigo";
	private static final String TAG_LATITUD = "latitud";
	private static final String TAG_LONGITUD = "longitud";
	private static final String TAG_IMAGEN = "imagen_externa";
	private static final String TAG_ID_AREA = "id_area";

	private static final JSONParser jParser = new JSONParser();
	
	JSONArray bloquesJSON = null;

	/** Metodo para obtener toda una lista de bloques*/
	public ArrayList<Bloque> listarBloques() {
		ArrayList<Bloque> listaBloques = new ArrayList<Bloque>();
		List<NameValuePair> parametros = new ArrayList<NameValuePair>();
		JSONObject jsonObject = jParser.makeHttpRequest(URL_ALL_LISTA_BLOQUES, "GET",
				parametros);
		Log.d("Listado Bloques: ", jsonObject.toString());
		try {
			int success = jsonObject.getInt(TAG_SUCCESS);
			if (success == 1) {
				bloquesJSON = jsonObject.getJSONArray(TAG_BLOQUES);
				for (int i = 0; i < bloquesJSON.length(); i++) {
					JSONObject bloqueJSON = bloquesJSON.getJSONObject(i);
					Bloque bloque= new Bloque(bloqueJSON.getInt(TAG_ID), bloqueJSON.getInt(TAG_NUMERO), 
							bloqueJSON.getString(TAG_CODIGO), bloqueJSON.getDouble(TAG_LATITUD),
							bloqueJSON.getDouble(TAG_LONGITUD), bloqueJSON.getInt(TAG_ID_AREA));
					listaBloques.add(bloque);
				}
			}
			return listaBloques;
		} catch (JSONException e) {
			Log.e("JSONEXCEPTION", "JSONException " + e.toString());
			return null;
		}
	}
	
	/** Metodo para obtener un bloque segun su id */
	public Bloque Obtener_bloque_segunID(Integer id) {
		Bloque bloque = new Bloque();
		try {
		int success;
		List<NameValuePair> parametros = new ArrayList<NameValuePair>();
		parametros.add(new BasicNameValuePair("id", id + ""));
		JSONObject jsonObject = jParser.makeHttpRequest(URL_BLOQUE_SEGUN_ID,
				"GET", parametros);
		
			JSONArray listaBloquesJSON = jsonObject.getJSONArray(TAG_BLOQUE); // JSON Array
			JSONObject bloqueJSON = listaBloquesJSON.getJSONObject(0);
			Log.d("Bloque: ", bloqueJSON.toString());
			success = jsonObject.getInt(TAG_SUCCESS);
			if (success == 1) {
				bloque=new Bloque(bloqueJSON.getInt(TAG_ID), bloqueJSON.getInt(TAG_NUMERO), 
						bloqueJSON.getString(TAG_CODIGO), bloqueJSON.getDouble(TAG_LATITUD),
						bloqueJSON.getDouble(TAG_LONGITUD), bloqueJSON.getInt(TAG_ID_AREA));
				bloque.setData(bloqueJSON.getString(TAG_IMAGEN));
			}
		} catch (JSONException e) {
			Log.e("JSONEXCEPTION", "JSONException " + e.toString());
		}
		return bloque;
	}
	
/**
 * Metodo que permite Obtener los puntos de interes para la Realidad Aumentada
 * @return Devuelve un Arreglo de JSON
 */
public JSONArray poisBloques() {
		
		JSONArray listpoiBean = new JSONArray();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		JSONObject json = jParser.makeHttpRequest(URL_BLOQUES_REALIDAD_AUMENTADA, "GET",
				params);
		
		try {
			int success = json.getInt(TAG_SUCCESS);
			if (success == 1) {
				bloquesJSON = json.getJSONArray(TAG_BLOQUES);
				for (int i = 0; i < bloquesJSON.length(); i++) {
					JSONObject jsObject = new JSONObject();
					jsObject.put("id", i);
					jsObject.put("idBloque", bloquesJSON.getJSONObject(i).getInt(TAG_ID));
					jsObject.put("nombre", "Bloque "+bloquesJSON.getJSONObject(i).getInt(TAG_NUMERO));
					jsObject.put("codigo", bloquesJSON.getJSONObject(i).getString("codigo"));
					jsObject.put("descripcion", "Area "+bloquesJSON.getJSONObject(i).getString("area"));
					jsObject.put("tipo", bloquesJSON.getJSONObject(i).getString("area"));
					jsObject.put("latitud",bloquesJSON.getJSONObject(i).getDouble(TAG_LATITUD));
					jsObject.put("longitud", bloquesJSON.getJSONObject(i).getDouble(TAG_LONGITUD));
					jsObject.put("id_area", bloquesJSON.getJSONObject(i).getInt("id_area"));
					
					listpoiBean.put(jsObject);
				}
			} else {

			}
			Log.d("PoisBloques: ", listpoiBean.toString());
			return listpoiBean;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}	
	}

}
