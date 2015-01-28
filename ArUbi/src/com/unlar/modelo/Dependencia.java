package com.unlar.modelo;

public class Dependencia {

	private int id;
	private String nombre;
	private String codigo;
	private int id_bloque;
	private int id_usuario_delegado;
	private int id_tipo_dependencia;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public int getId_bloque() {
		return id_bloque;
	}

	public void setId_bloque(int id_bloque) {
		this.id_bloque = id_bloque;
	}

	public int getId_usuario_delegado() {
		return id_usuario_delegado;
	}

	public void setId_usuario_delegado(int id_usuario_delegado) {
		this.id_usuario_delegado = id_usuario_delegado;
	}

	public int getId_tipo_dependencia() {
		return id_tipo_dependencia;
	}

	public void setId_tipo_dependencia(int id_tipo_dependencia) {
		this.id_tipo_dependencia = id_tipo_dependencia;
	}

	public Dependencia(int id, String nombre, String codigo, int id_bloque,
			int id_usuario_delegado, int id_tipo_dependencia) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.codigo = codigo;
		this.id_bloque = id_bloque;
		this.id_usuario_delegado = id_usuario_delegado;
		this.id_tipo_dependencia = id_tipo_dependencia;
	}

	public Dependencia() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
