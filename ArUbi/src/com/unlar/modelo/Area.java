package com.unlar.modelo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Area {

	private Integer id;
	private String nombre;
	private Bitmap marcador;
	private String sitio_web;
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

	public String getSitio_web() {
		return sitio_web;
	}

	public void setSitio_web(String sitio_web) {
		this.sitio_web = sitio_web;
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

	public Area(Integer id, String nombre, Bitmap marcador, String sitio_web) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.marcador = marcador;
		this.sitio_web = sitio_web;
	}

	public Area(Integer id, String nombre, String sitio_web) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.sitio_web = sitio_web;
	}

	public Area() {
		super();
	}
}
