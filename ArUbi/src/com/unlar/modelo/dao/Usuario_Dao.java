package com.unlar.modelo.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.unlar.modelo.Usuario;
import com.unlar.utility.JSONParser;

import android.util.Log;

public class Usuario_Dao {

	private static final String URL_DELEGADO_SEGUN_ID = "http://arubiunl.com/serverforArUbi/consultar_Delegado_por_Id.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_DELEGADO = "delegado";
	private static final String TAG_ID = "id";
	private static final String TAG_NOMBRES = "nombres";
	private static final String TAG_APELLIDOS = "apellidos";
	private static final String TAG_TELEFONO = "telefono";
	private static final String TAG_CORREO = "correo";

	private static final JSONParser jParser = new JSONParser();

	JSONArray delegadosJSON = null;

	/** Metodo para obtener un delegado segun su id */
	public Usuario Obtener_Delegado_segunID(Integer id) {
		Usuario usuario = new Usuario();
		try {
			int success;
			List<NameValuePair> parametros = new ArrayList<NameValuePair>();
			parametros.add(new BasicNameValuePair("id", id + ""));
			JSONObject jsonObject = jParser.makeHttpRequest(
					URL_DELEGADO_SEGUN_ID, "GET", parametros);

			JSONArray listaDelegadosJSON = jsonObject
					.getJSONArray(TAG_DELEGADO); // JSON Array
			JSONObject delegadoJSON = listaDelegadosJSON.getJSONObject(0);
			success = jsonObject.getInt(TAG_SUCCESS);
			if (success == 1) {
				usuario = new Usuario(delegadoJSON.getInt(TAG_ID),
						delegadoJSON.getString(TAG_NOMBRES),
						delegadoJSON.getString(TAG_APELLIDOS),
						delegadoJSON.getString(TAG_TELEFONO),
						delegadoJSON.getString(TAG_CORREO));
			}
		} catch (JSONException e) {
			Log.e("JSONEXCEPTION", "JSONException " + e.toString());
		}
		return usuario;
	}
}
