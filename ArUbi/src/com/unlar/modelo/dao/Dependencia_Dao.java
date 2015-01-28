package com.unlar.modelo.dao;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.unlar.modelo.Dependencia;
import com.unlar.utility.JSONParser;

import android.util.Log;

public class Dependencia_Dao {
	/**
	 * Declaración de variables
	 * */
	private static final String url_todas_dependencias = "http://arubiunl.com/serverforArUbi/consultar_Dependencias.php";
	private static final String url_dependencias_segun_bloque = "http://arubiunl.com/serverforArUbi/consultar_Dependencias_idBloque.php";
	private static final String url_dependencias_segun_nombre = "http://arubiunl.com/serverforArUbi/consultar_Dependencias_Por_Nombre.php";
	private static final String url_dependencias_segun_area = "http://arubiunl.com/serverforArUbi/consultar_Dependencias_Por_Area.php";
	private static final String url_dependencias_segun_tipo = "http://arubiunl.com/serverforArUbi/consultar_Dependencias_Por_Tipo.php";
	private static final String URL_BLOQUES_REALIDAD_AUMENTADA = "http://arubiunl.com/serverforArUbi/consultar_Dependencias_RealidadAumentada.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_DEPENDENCIAS = "dependencias";
	private static final String TAG_ID = "id";
	private static final String TAG_NOMBRE = "nombre";
	private static final String TAG_CODIGO = "codigo";
	private static final String TAG_ID_BLOQUE = "id_bloque";
	private static final String TAG_ID_USUARIO_DELEGADO = "id_usuario_delegado";
	private static final String TAG_ID_TIPO_DEPENDENCIA = "id_tipo_dependencia";
	
	private static final String TAG_POIBEANS = "PoiBeans";
	
	private static final JSONParser jParser = new JSONParser();
	
	JSONArray dependenciasJSON = null;
	
	/** Metodo que permite obtner una lista de dependencias*/
		public ArrayList<Dependencia> listarDependencias() {
			ArrayList<Dependencia> listDependencias = new ArrayList<Dependencia>();
			List<NameValuePair> parametros = new ArrayList<NameValuePair>();
			JSONObject jsonObject = jParser.makeHttpRequest(url_todas_dependencias,
					"GET", parametros);
			Log.d("Todas las Dependencias: ", jsonObject.toString());
			try {
				int success = jsonObject.getInt(TAG_SUCCESS);
				if (success == 1) {
					dependenciasJSON = jsonObject.getJSONArray(TAG_DEPENDENCIAS);
					for (int i = 0; i < dependenciasJSON.length(); i++) {
						JSONObject dependenciaJSON = dependenciasJSON.getJSONObject(i);
						Dependencia d = new Dependencia(dependenciaJSON.getInt(TAG_ID),
								dependenciaJSON.getString(TAG_NOMBRE),
								dependenciaJSON.getString(TAG_CODIGO),
								dependenciaJSON.getInt(TAG_ID_BLOQUE),
								dependenciaJSON.getInt(TAG_ID_USUARIO_DELEGADO),
								dependenciaJSON.getInt(TAG_ID_TIPO_DEPENDENCIA));
						listDependencias.add(d);
					}
				} else {
					listDependencias = null;
				}
				return listDependencias;
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}
		
	/** Metodo que permite obtener una lista de dependencias segun el bloque */
	public ArrayList<Dependencia> obtenerDependencias_segunBloque(Integer id) {
		ArrayList<Dependencia> listDependencias = new ArrayList<Dependencia>();
		List<NameValuePair> parametros = new ArrayList<NameValuePair>();
		parametros.add(new BasicNameValuePair("id_bloque", id + ""));
		JSONObject jsonObject = jParser.makeHttpRequest(
				url_dependencias_segun_bloque, "GET", parametros);
		Log.d("All Dependencias: ", jsonObject.toString());
		try {
			int success = jsonObject.getInt(TAG_SUCCESS);
			if (success == 1) {
				dependenciasJSON = jsonObject.getJSONArray(TAG_DEPENDENCIAS);
				for (int i = 0; i < dependenciasJSON.length(); i++) {
					JSONObject dependencia = dependenciasJSON.getJSONObject(i);
					Dependencia d = new Dependencia(dependencia.getInt(TAG_ID),
							dependencia.getString(TAG_NOMBRE),
							dependencia.getString(TAG_CODIGO),
							dependencia.getInt(TAG_ID_BLOQUE),
							dependencia.getInt(TAG_ID_USUARIO_DELEGADO),
							dependencia.getInt(TAG_ID_TIPO_DEPENDENCIA));
					listDependencias.add(d);
				}
			} else {

			}
			return listDependencias;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/** Metodo que permite obtener una lista de dependencias segun el nombre */
	public ArrayList<Dependencia> obtenerDependencias_segunNombre(String parametro) {
		ArrayList<Dependencia> listDependencias = new ArrayList<Dependencia>();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("nombre", parametro + ""));
		JSONObject json = jParser.makeHttpRequest(
				url_dependencias_segun_nombre, "GET", params);
		Log.d("Dependencias segun Nombre: ", json.toString());
		try {
			int success = json.getInt(TAG_SUCCESS);
			if (success == 1) {
				dependenciasJSON = json.getJSONArray(TAG_DEPENDENCIAS);
				for (int i = 0; i < dependenciasJSON.length(); i++) {
					JSONObject dependencia = dependenciasJSON.getJSONObject(i);
					Dependencia d = new Dependencia(dependencia.getInt(TAG_ID),
							dependencia.getString(TAG_NOMBRE),
							dependencia.getString(TAG_CODIGO),
							dependencia.getInt(TAG_ID_BLOQUE),
							dependencia.getInt(TAG_ID_USUARIO_DELEGADO),
							dependencia.getInt(TAG_ID_TIPO_DEPENDENCIA));
					listDependencias.add(d);
				}
			} else {
				listDependencias = new ArrayList<Dependencia>();
			}
			return listDependencias;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	/** Metodo que permite obtener una lista de dependencias segun el area */
	public ArrayList<Dependencia> obtenerDependencias_segunArea(String parametro) {
		ArrayList<Dependencia> listDependencias = new ArrayList<Dependencia>();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("area", parametro + ""));
		JSONObject json = jParser.makeHttpRequest(
				url_dependencias_segun_area, "GET", params);
		Log.d("Dependencias segun area: ", json.toString());
		try {
			int success = json.getInt(TAG_SUCCESS);
			if (success == 1) {
				dependenciasJSON = json.getJSONArray(TAG_DEPENDENCIAS);
				for (int i = 0; i < dependenciasJSON.length(); i++) {
					JSONObject dependencia = dependenciasJSON.getJSONObject(i);
					Dependencia d = new Dependencia(dependencia.getInt(TAG_ID),
							dependencia.getString(TAG_NOMBRE),
							dependencia.getString(TAG_CODIGO),
							dependencia.getInt(TAG_ID_BLOQUE),
							dependencia.getInt(TAG_ID_USUARIO_DELEGADO),
							dependencia.getInt(TAG_ID_TIPO_DEPENDENCIA));
					listDependencias.add(d);
				}
			} else {

			}
			return listDependencias;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/** Metodo que permite obtener una lista de dependencias segun el tipo */
	public ArrayList<Dependencia> obtenerDependencias_segunTipo(String parametro) {
		ArrayList<Dependencia> listDependencias = new ArrayList<Dependencia>();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tipo", parametro + ""));
		JSONObject json = jParser.makeHttpRequest(
				url_dependencias_segun_tipo, "GET", params);
		Log.d("All Dependencias: ", json.toString());
		try {
			int success = json.getInt(TAG_SUCCESS);
			if (success == 1) {
				dependenciasJSON = json.getJSONArray(TAG_DEPENDENCIAS);
				for (int i = 0; i < dependenciasJSON.length(); i++) {
					JSONObject dependencia = dependenciasJSON.getJSONObject(i);
					Dependencia d = new Dependencia(dependencia.getInt(TAG_ID),
							dependencia.getString(TAG_NOMBRE),
							dependencia.getString(TAG_CODIGO),
							dependencia.getInt(TAG_ID_BLOQUE),
							dependencia.getInt(TAG_ID_USUARIO_DELEGADO),
							dependencia.getInt(TAG_ID_TIPO_DEPENDENCIA));
					listDependencias.add(d);
				}
			} else {

			}
			return listDependencias;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Metodo que permite Obtener los puntos de interes para la Realidad Aumentada
	 * @return Devuelve un Arreglo de JSON
	 */
	public JSONArray poisDependenciasSegunBloq(Integer id) {
		
		JSONArray listpoiBean = new JSONArray();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id_bloque", id + ""));
		JSONObject json = jParser.makeHttpRequest(
				URL_BLOQUES_REALIDAD_AUMENTADA, "GET", params);
		Log.d("All PoiBeans: ", json.toString());
		
		try {
			int success = json.getInt(TAG_SUCCESS);
			if (success == 1) {
				dependenciasJSON = json.getJSONArray(TAG_POIBEANS);
				for (int i = 0; i < dependenciasJSON.length(); i++) {
					JSONObject jsObject = new JSONObject();
					jsObject.put("id", i);
					jsObject.put("id_depend", dependenciasJSON.getJSONObject(i).getInt(TAG_ID));
					jsObject.put("nombre_depend", dependenciasJSON.getJSONObject(i).getString(TAG_NOMBRE));
					jsObject.put("cod_depend", dependenciasJSON.getJSONObject(i).getString(TAG_CODIGO));
					//jsObject.put("tipo_depend", dependenciasJSON.getJSONObject(i).getString("tipo"));
					//jsObject.put("contact_depend", dependenciasJSON.getJSONObject(i).getString("contacto"));
					//jsObject.put("delegad_depend", dependenciasJSON.getJSONObject(i).getString("delegado"));
					jsObject.put("id_deleg", dependenciasJSON.getJSONObject(i).getInt(TAG_ID_USUARIO_DELEGADO));
					jsObject.put("id_bloq", dependenciasJSON.getJSONObject(i).getInt(TAG_ID_BLOQUE));
					jsObject.put("id_tipo", dependenciasJSON.getJSONObject(i).getInt(TAG_ID_TIPO_DEPENDENCIA));
					jsObject.put("numero_bloq",dependenciasJSON.getJSONObject(i).getInt("numero") );
					jsObject.put("area_bloq", dependenciasJSON.getJSONObject(i).getString("area"));
					jsObject.put("lat_bloq",dependenciasJSON.getJSONObject(i).getDouble("latitud"));
					jsObject.put("long_bloq", dependenciasJSON.getJSONObject(i).getDouble("longitud"));
					listpoiBean.put(jsObject);
					
				}
			}	
			Log.d("PoisDependencias: ", listpoiBean.toString());
			return listpoiBean;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
