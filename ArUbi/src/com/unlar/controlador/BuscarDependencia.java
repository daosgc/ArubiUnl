package com.unlar.controlador;

import java.util.ArrayList;

import com.unlar.modelo.Area;
import com.unlar.modelo.Bloque;
import com.unlar.modelo.Dependencia;
import com.unlar.modelo.dao.Area_Dao;
import com.unlar.modelo.dao.Bloque_Dao;
import com.unlar.modelo.dao.Dependencia_Dao;
import com.unlar.utility.ConnectionDetector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BuscarDependencia extends Activity {

	private ListView lView;
	private EditText editxtBusqueda;

	private static final int MnuOpc1 = 1;
	private static final int itembusqNombre = 11;
	private static final int itembusqTipo = 12;
	private static final int itembusqArea = 13;
	private static final int GRUPO_MENU_1 = 101;

	private int opcionSeleccionada = 1;

	private ConnectionDetector cd;
	private Boolean isConectionPresent;
	private Boolean isInternetPresent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buscar_dependencia);
		cd = new ConnectionDetector(getApplicationContext());
		isConectionPresent = cd.isConnectingToInternet();

		if (!isConectionPresent) {
			Toast.makeText(getBaseContext(),
					"Sin conexión, Intentelo mas tarde...", Toast.LENGTH_LONG)
					.show();
			this.finish();
		} else {
			this.editxtBusqueda = (EditText) findViewById(R.id.editxtBusqueda);
			editxtBusqueda.setOnKeyListener(new OnKeyListener() {
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// If the event is a key-down event on the "enter" button
					if ((event.getAction() == KeyEvent.ACTION_DOWN)
							&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
						// Perform action on key press
						if (opcionSeleccionada == 1) {
							if (editxtBusqueda.getText().toString().equals("")) {
								Toast.makeText(
										BuscarDependencia.this,
										"No se puede realizar la búsqueda, por favor ingrese un valor.",
										Toast.LENGTH_LONG).show();
							} else {
								new ListarDependencias().execute();
							}
						} else if (opcionSeleccionada == 2) {
							if (editxtBusqueda.getText().toString().equals("")) {
								Toast.makeText(
										BuscarDependencia.this,
										"No se puede realizar la búsqueda, por favor ingrese un valor.",
										Toast.LENGTH_LONG).show();
							} else {
								new ListarDependencias().execute();
							}
						} else if (opcionSeleccionada == 3) {
							if (editxtBusqueda.getText().toString().equals("")) {
								Toast.makeText(
										BuscarDependencia.this,
										"No se puede realizar la búsqueda, por favor ingrese un valor.",
										Toast.LENGTH_LONG).show();
							} else {
								new ListarDependencias().execute();
							}
						}
						return true;
					}
					return false;
				}
			});
			// realizar consultas al web service usando Background Thread
			new ListarDependencias().execute();

			this.lView = (ListView) findViewById(R.id.listView);

			lView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> pariente, View view,
						int posicion, long id) {
					Dependencia elegido = (Dependencia) pariente
							.getItemAtPosition(posicion);
					Intent i = new Intent(BuscarDependencia.this,
							InformacionDependencia.class);
					i.putExtra("nombre", elegido.getNombre());
					i.putExtra("codigo", elegido.getCodigo());
					i.putExtra("id_delegado", elegido.getId_usuario_delegado());
					i.putExtra("id_bloq", elegido.getId_bloque());
					startActivity(i);
				}
			});
		}

	}

	public void buscar(View view) {
		if (opcionSeleccionada == 1) {
			if (editxtBusqueda.getText().toString().equals("")) {
				Toast.makeText(
						BuscarDependencia.this,
						"No se puede realizar la búsqueda, por favor ingrese un valor.",
						Toast.LENGTH_LONG).show();
			} else {
				new ListarDependencias().execute();
			}
		} else if (opcionSeleccionada == 2) {
			if (editxtBusqueda.getText().toString().equals("")) {
				Toast.makeText(
						BuscarDependencia.this,
						"No se puede realizar la búsqueda, por favor ingrese un valor.",
						Toast.LENGTH_LONG).show();
			} else {
				new ListarDependencias().execute();
			}
		} else if (opcionSeleccionada == 3) {
			if (editxtBusqueda.getText().toString().equals("")) {
				Toast.makeText(
						BuscarDependencia.this,
						"No se puede realizar la búsqueda, por favor ingrese un valor.",
						Toast.LENGTH_LONG).show();
			} else {
				new ListarDependencias().execute();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu smnu = menu.addSubMenu(Menu.NONE, MnuOpc1, Menu.NONE,
				"Parámetros de Búsqueda").setIcon(
				android.R.drawable.ic_menu_agenda);
		smnu.add(GRUPO_MENU_1, itembusqNombre, Menu.NONE, "Nombre");
		smnu.add(GRUPO_MENU_1, itembusqTipo, Menu.NONE, "Tipo");
		smnu.add(GRUPO_MENU_1, itembusqArea, Menu.NONE, "Área");

		smnu.setGroupCheckable(GRUPO_MENU_1, true, true);

		if (opcionSeleccionada == 1)
			smnu.getItem(0).setChecked(true);
		else if (opcionSeleccionada == 2)
			smnu.getItem(1).setChecked(true);
		else if (opcionSeleccionada == 3)
			smnu.getItem(2).setChecked(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case itembusqNombre:
			opcionSeleccionada = 1;
			item.setChecked(true);
			return true;
		case itembusqTipo:
			opcionSeleccionada = 2;
			item.setChecked(true);
			return true;
		case itembusqArea:
			opcionSeleccionada = 3;
			item.setChecked(true);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * Background Async Task to Create new product
	 * */
	class ListarDependencias extends AsyncTask<Void, Void, Void> {
		
		private ProgressDialog pDialog;
		private ArrayList<Area> listAreas;
		private ArrayList<Bloque> listBloques;
		private ArrayList<Dependencia> listDependencias;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(BuscarDependencia.this);
			pDialog.setMessage("Listando dependencias..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

			pDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					ListarDependencias.this.cancel(true);
					
					AlertDialog.Builder builder = new AlertDialog.Builder(BuscarDependencia.this); 
					builder.setTitle("Alerta");
					builder.setMessage("Esta ud seguro que desea cancelar la actividad...");
					builder.setPositiveButton("Aceptar", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						BuscarDependencia.this.finish();
					}});
					builder.setNegativeButton("Cancelar", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						new ListarDependencias().execute();
					}});
					builder.show();
				}
			});
		}

		/**
		 * Creating product
		 * */
		protected Void doInBackground(Void... arg0) {
			isInternetPresent = cd.isOnline();
			if (isInternetPresent) {
				listBloques = new Bloque_Dao().listarBloques();
				listAreas = new Area_Dao().listarAreas();

				if (opcionSeleccionada == 1) {
					listDependencias = new Dependencia_Dao()
							.obtenerDependencias_segunNombre(editxtBusqueda
									.getText().toString());
				} else if (opcionSeleccionada == 2) {
					listDependencias = new Dependencia_Dao()
							.obtenerDependencias_segunTipo(editxtBusqueda
									.getText().toString());
				} else if (opcionSeleccionada == 3) {
					listDependencias = new Dependencia_Dao()
							.obtenerDependencias_segunArea(editxtBusqueda
									.getText().toString());
				}
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(Void arg1) {
			// dismiss the dialog once done
			pDialog.dismiss();
			if (!isInternetPresent) {
				Toast.makeText(
						BuscarDependencia.this,
						"No se puede establecer una conexión a internet, Intentelo mas tarde...",
						Toast.LENGTH_LONG).show();
				BuscarDependencia.this.finish();
			} else {
				ItemDependenciaAdapter adapter = new ItemDependenciaAdapter(
						BuscarDependencia.this, listDependencias, listBloques,
						listAreas);
				lView.setAdapter(adapter);
				if (listDependencias.size() == 0) {
					Toast.makeText(
							BuscarDependencia.this,
							"No se ha encontrado ningún resultado que coincida con su búsqueda",
							Toast.LENGTH_LONG).show();
				}
			}
		}
	}

}
