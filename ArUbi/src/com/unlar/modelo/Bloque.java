package com.unlar.modelo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Bloque {

	private int id;
	private int numero;
	private String codigo;
	private double latitud;
	private double longitud;
	private Bitmap imagen_externa;
	private int id_area;

	private String data;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public Bitmap getImagen_externa() {
		return imagen_externa;
	}

	public int getId_area() {
		return id_area;
	}

	public void setId_area(int id_area) {
		this.id_area = id_area;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
		try {
			byte[] byteData = Base64.decode(data, Base64.DEFAULT);
			this.imagen_externa = BitmapFactory.decodeByteArray(byteData, 0,
					byteData.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Bloque(int id, int numero, String codigo, double latitud,
			double longitud, Bitmap imagen_externa, int id_area) {
		super();
		this.id = id;
		this.numero = numero;
		this.codigo = codigo;
		this.latitud = latitud;
		this.longitud = longitud;
		this.imagen_externa = imagen_externa;
		this.id_area = id_area;
	}
	
	public Bloque(int id, int numero, String codigo, double latitud,
			double longitud, int id_area) {
		super();
		this.id = id;
		this.numero = numero;
		this.codigo = codigo;
		this.latitud = latitud;
		this.longitud = longitud;
		this.id_area = id_area;
	}
	
	public Bloque(){
		super();
	}
}
