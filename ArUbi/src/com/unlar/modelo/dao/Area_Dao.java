package com.unlar.modelo.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.unlar.modelo.Area;
import com.unlar.utility.JSONParser;

import android.util.Log;

public class Area_Dao {
	
	private static final String URL_ALL_LISTA_AREAS = "http://arubiunl.com/serverforArUbi/consultar_Areas.php";
	private static final String URL_AREA_SEGUN_ID = "http://arubiunl.com/serverforArUbi/consultar_Areas_idArea.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_AREA = "area";
	private static final String TAG_AREAS = "areas";
	private static final String TAG_ID = "id";
	private static final String TAG_NOMBRE = "nombre";
	private static final String TAG_SITIO_WEB = "sitio_web";
	private static final String TAG_MARCADOR = "marcador";

	private static final JSONParser jParser = new JSONParser();
	
	JSONArray areasJSON = null;
		
	/** Metodo para obtener todas las areas*/
	public ArrayList<Area> listarAreas() {
		ArrayList<Area> listaAreas = new ArrayList<Area>();
		List<NameValuePair> parametros = new ArrayList<NameValuePair>();
		JSONObject jsonObject = jParser.makeHttpRequest(URL_ALL_LISTA_AREAS, "GET",
				parametros);
		Log.d("Listado Areas: ", jsonObject.toString());
		try {
			int success = jsonObject.getInt(TAG_SUCCESS);
			if (success == 1) {
				areasJSON = jsonObject.getJSONArray(TAG_AREAS);
				for (int i = 0; i < areasJSON.length(); i++) {
					JSONObject areaJSON = areasJSON.getJSONObject(i);
					Area area = new Area(areaJSON.getInt(TAG_ID), areaJSON.getString(TAG_NOMBRE), areaJSON.getString(TAG_SITIO_WEB));
					area.setData(areaJSON.getString(TAG_MARCADOR));
					listaAreas.add(area);
				}
			}
			return listaAreas;
		} catch (JSONException e) {
			Log.e("JSONEXCEPTION", "JSONException " + e.toString());
			return null;
		}
	}
	
	/** Metodo para obtener un area segun su id */
	public Area Obtener_area_segunID(Integer id) {
		Area area = new Area();
		List<NameValuePair> parametros = new ArrayList<NameValuePair>();
		parametros.add(new BasicNameValuePair("id", id + ""));
		JSONObject jsonObject = jParser.makeHttpRequest(URL_AREA_SEGUN_ID,
				"GET", parametros);
		try {
			JSONArray listaAreasJSON = jsonObject.getJSONArray(TAG_AREA); // JSON Array
			JSONObject areaJSON = listaAreasJSON.getJSONObject(0);
			int success = jsonObject.getInt(TAG_SUCCESS);
			if (success == 1) {
				area=new Area(areaJSON.getInt(TAG_ID), areaJSON.getString(TAG_NOMBRE), areaJSON.getString(TAG_SITIO_WEB));
				area.setData(areaJSON.getString(TAG_MARCADOR));
			}
		} catch (JSONException e) {
			Log.e("JSONEXCEPTION", "JSONException " + e.toString());
		}
		return area;
	}
}
