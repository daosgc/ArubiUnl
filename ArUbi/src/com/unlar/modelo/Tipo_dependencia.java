package com.unlar.modelo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Tipo_dependencia {
	
	private Integer id;
	private String nombre;
	private Bitmap marcador;
	
	private String data;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Bitmap getMarcador() {
		return marcador;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
		try {
			byte[] byteData = Base64.decode(data, Base64.DEFAULT);
			this.marcador = BitmapFactory.decodeByteArray(byteData, 0,
					byteData.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Tipo_dependencia(Integer id, String nombre, Bitmap marcador) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.marcador = marcador;
	}	
}
