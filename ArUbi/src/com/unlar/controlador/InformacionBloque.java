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
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class InformacionBloque extends Activity {

	private Bundle extras;
	private ListView lView;
	private ConnectionDetector cd;
	private Boolean isConectionPresent;
	private Boolean isInternetPresent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_informacion_bloque);
		cd = new ConnectionDetector(getApplicationContext());
		isConectionPresent = cd.isConnectingToInternet();
		if (!isConectionPresent) {
			Toast.makeText(getBaseContext(),
					"Sin conexión, Intentelo mas tarde...", Toast.LENGTH_LONG)
					.show();
			this.finish();
		} else {
			extras = getIntent().getExtras();
			new ObtenerBloque().execute();
			this.lView = (ListView) findViewById(R.id.listView);
			lView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> pariente, View view,
						int posicion, long id) {
					Dependencia elegido = (Dependencia) pariente
							.getItemAtPosition(posicion);

					Intent i = new Intent(InformacionBloque.this,
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_vtnbloq, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itmVerMapa:
			Intent i = new Intent(InformacionBloque.this,
					MapaUnl.class);
			i.putExtra("id_bloq", extras.getInt("idBloque"));
			i.putExtra("opc", "ar");
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Background Async Task to Create new product
	 * */
	class ObtenerBloque extends AsyncTask<Void, Void, Void> {
		private ProgressDialog pDialog;
		private ArrayList<Area> listAreas;
		private ArrayList<Bloque> listBloques;
		private ArrayList<Dependencia> listDependencias;
		private Bloque bloque;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(InformacionBloque.this);
			pDialog.setMessage("Cargando datos..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			
			pDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					ObtenerBloque.this.cancel(true);
					
					AlertDialog.Builder builder = new AlertDialog.Builder(InformacionBloque.this); 
					builder.setTitle("Alerta");
					builder.setMessage("Esta ud seguro que desea cancelar la actividad...");
					builder.setPositiveButton("Aceptar", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						InformacionBloque.this.finish();
					}});
					builder.setNegativeButton("Cancelar", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						new ObtenerBloque().execute();
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
				// consultas a la bd
				bloque = new Bloque_Dao().Obtener_bloque_segunID(extras
						.getInt("idBloque"));
				listDependencias = new Dependencia_Dao()
						.obtenerDependencias_segunBloque(extras
								.getInt("idBloque"));
				listBloques = new Bloque_Dao().listarBloques();
				listAreas = new Area_Dao().listarAreas();
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
						InformacionBloque.this,
						"No se puede establecer una conexión a internet, Intentelo mas tarde...",
						Toast.LENGTH_LONG).show();
				InformacionBloque.this.finish();
			} else {
				// instanciamos todos los elementos
				ImageView imgBloq = (ImageView) findViewById(R.id.txtVwImgBloq);
				TextView txtnombre = (TextView) findViewById(R.id.txtTituloBloq);
				TextView txtcodigo = (TextView) findViewById(R.id.txtCodigoBloq);

				// fijamos los parametros
				imgBloq.setImageBitmap(bloque.getImagen_externa());
				txtnombre.setText("Bloque " + bloque.getNumero());
				txtcodigo.setText(bloque.getCodigo());

				ItemDependenciaAdapter adapter = new ItemDependenciaAdapter(
						InformacionBloque.this, listDependencias, listBloques,
						listAreas);
				lView.setAdapter(adapter);
			}
		}
	}
}
