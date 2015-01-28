package com.unlar.controlador;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class ArUbi extends Activity {
	public static final String EXTRAS_KEY_ACTIVITY_TITLE_STRING = "activityTitle";
	public static final String EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL = "activityArchitectWorldUrl";
	public static final String EXTRAS_ID_BLOQUE = "idBloque";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_arubi);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_vtnarubi, menu);
		return true;
	}

	public void cargarMapa(View view) {
		Intent i = new Intent(this, MapaUnl.class);
		i.putExtra("opc", "map");
		startActivity(i);
	}

	public void visualizarRealidadAumentada(View view) {
		Intent i = new Intent(this, RealidadAumentada.class);
		i.putExtra(EXTRAS_KEY_ACTIVITY_TITLE_STRING,
				"ArUbi");
		//intent.putExtra(EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL, "ObtainPoiDataFromWebservice"+ File.separator + "index.html");
		i.putExtra(EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL, "http://arubiunl.com/serverforArUbi/unlar/bloques/index.html");
		i.putExtra(EXTRAS_ID_BLOQUE, 0);
		i.putExtra("opc", "ar");
		startActivity(i);
	}

	public void realizarBusquedas(View view) {
		Intent i = new Intent(this, BuscarDependencia.class);
		startActivity(i);
	}

	public void recomendarApp(View view) {
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "ArUbi Unl");
		sharingIntent
				.putExtra(
						Intent.EXTRA_TEXT,
						"Descargue la aplicación móvil ArUbi de la Universidad Nacional de Loja desde el Google Play - https://play.google.com/store/apps/details?id=com.unlar.controlador ");
		startActivity(Intent.createChooser(sharingIntent, "Recomendar via"));
	}
}
