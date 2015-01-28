package com.unlar.controlador;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unlar.modelo.Area;
import com.unlar.modelo.Bloque;
import com.unlar.modelo.dao.Area_Dao;
import com.unlar.modelo.dao.Bloque_Dao;
import com.unlar.utility.ConnectionDetector;

public class MapaUnl extends FragmentActivity implements
		LocationListener {

	private GoogleMap googleMap;

	private Bundle bExtras;
	private TextView tvDistanceDuration;
	private ArrayList<Bloque> listaBloques;
	private int idBloque;
	private String opcion;
	private ConnectionDetector cd;
	private Boolean isConectionPresent;
	private Boolean isInternetPresent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostrar_mapa);
		cd = new ConnectionDetector(getApplicationContext());
		isConectionPresent = cd.isConnectingToInternet();

		if (!isConectionPresent) {
			Toast.makeText(getBaseContext(),
					"Sin conexión, Intentelo mas tarde...", Toast.LENGTH_LONG)
					.show();
			this.finish();
		} else {
			// Getting Google Play availability status
			int status = GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(getBaseContext());

			// Showing status
			if (status != ConnectionResult.SUCCESS) { // Google Play Services
														// are not available

				int requestCode = 10;
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,
						this, requestCode);
				dialog.show();

			} else {
				LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
				if (!locationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
						&& !locationManager
								.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					Toast.makeText(
							getBaseContext(),
							"Servicios de ubicación inhabilitados, Active los servicios de ubicación e intentelo mas tarde...",
							Toast.LENGTH_LONG).show();
					this.finish();
				} else {
					// Google Play Services are available
					// parametros recibidos
					bExtras = getIntent().getExtras();
					tvDistanceDuration = (TextView) findViewById(R.id.tv_distance_time);
					opcion = bExtras.getString("opc");
					// Getting reference to the SupportMapFragment of
					// activity_main.xml
					SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
							.findFragmentById(R.id.map);

					// Getting GoogleMap object from the fragment
					googleMap = fm.getMap();

					// Enabling MyLocation Layer of Google Map
					googleMap.setMyLocationEnabled(true);

					// desactivar el boton de la ubicacion del usuario
					googleMap.getUiSettings().setMyLocationButtonEnabled(true);

					// Getting LocationManager object from System Service
					// LOCATION_SERVICE

					// Creating a criteria object to retrieve provider
					Criteria criteria = new Criteria();

					// Getting the name of the best provider
					String provider = locationManager.getBestProvider(criteria,
							true);

					// Getting Current Location
					Location location = locationManager
							.getLastKnownLocation(provider);

					if (location != null) {
						onLocationChanged(location);
					}

					locationManager.requestLocationUpdates(provider, 10000, 0,
							this);

					new ListarBloques().execute();
					if (!opcion.equalsIgnoreCase("ar")) {
						centrarmapa(new LatLng(-4.03361388096575,
								-79.20185565948486), 15);
					}
				}
			}
		}

	}

	@Override
	public void onLocationChanged(Location location) {

		// mover el mapa de acuerdo como se mueve la ubicacion del usuario
		// Getting latitude of the current location
		// double latitude = location.getLatitude();

		// Getting longitude of the current location
		// double longitude = location.getLongitude();

		// Creating a LatLng object for the current location
		// LatLng latLng = new LatLng(latitude, longitude);

		// Showing the current location in Google Map
		// googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

		// Zoom in the Google Map
		// googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
	}

	@Override
	public void onProviderDisabled(String provider) {
		// cuando se deshabilita un proveedor de localización... prefiero no
		// hacer nada
		Toast.makeText(this, "provider disabled", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_vtnmapa, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itmMiCampus:
			if (listaBloques.size() == 1) {
				opcion = "map";
				new ListarBloques().execute();
			}
			centrarmapa(new LatLng(-4.03361388096575, -79.20185565948486), 15);
			return true;
		case R.id.itmLimpiarMap:
			tvDistanceDuration.setVisibility(View.GONE);
			limpiarMapa("map");
			return true;
		case R.id.imtAdministracion:
			if (listaBloques.size() == 1) {
				opcion = "map";
				new ListarBloques().execute();
			}
			centrarmapa(new LatLng(-4.03328211083049, -79.20271933078766), 18);
			return true;
		case R.id.itmEnergia:
			if (listaBloques.size() == 1) {
				opcion = "map";
				new ListarBloques().execute();
			}
			centrarmapa(new LatLng(-4.03169817412425, -79.19979572296143), 18);
			return true;
		case R.id.itmEducativa:
			if (listaBloques.size() == 1) {
				opcion = "map";
				new ListarBloques().execute();
			}
			centrarmapa(new LatLng(-4.0343255809601875, -79.2038083076477), 18);
			return true;
		case R.id.imtSalud:
			if (listaBloques.size() == 1) {
				opcion = "map";
				new ListarBloques().execute();
			}
			centrarmapa(new LatLng(-3.9929656832430034, -79.20745074748993), 18);
			return true;
		case R.id.imtJuridica:
			if (listaBloques.size() == 1) {
				opcion = "map";
				new ListarBloques().execute();
			}
			centrarmapa(new LatLng(-4.036032588179927, -79.20471489429474), 18);
			return true;
		case R.id.imtAgropecuaria:
			if (listaBloques.size() == 1) {
				opcion = "map";
				new ListarBloques().execute();
			}
			centrarmapa(new LatLng(-4.035641956750293, -79.20269787311554), 18);
			return true;
		case R.id.imtNivelacion:
			if (listaBloques.size() == 1) {
				opcion = "map";
				new ListarBloques().execute();
			}
			centrarmapa(new LatLng(-3.94515, -79.22348), 18);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void limpiarMapa(String opc) {
		googleMap.clear();
		opcion = opc;
		new ListarBloques().execute();
		if (!opcion.equalsIgnoreCase("ar")) {
			centrarmapa(new LatLng(-4.03361388096575, -79.20185565948486), 15);
		}
	}

	public void centrarmapa(LatLng ubicacion, int zoom) {
		CameraPosition newCameraPosition = new CameraPosition.Builder()
				.target(ubicacion).zoom(zoom).bearing(0).tilt(0).build();
		googleMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(newCameraPosition));
		/*
		 * // Showing the current location in Google Map
		 * googleMap.moveCamera(CameraUpdateFactory.newLatLng(ubicacion));
		 * 
		 * // Zoom in the Google Map
		 * googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
		 */
	}

	public void añadirmarcador(LatLng ubicacion, BitmapDescriptor icono,
			String titulo) {
		googleMap.addMarker(new MarkerOptions().position(ubicacion).icon(icono)
				.title(titulo));
	}

	LatLng destino;
	String v_codBloq = "";
	String v_areaBloq = "";

	// cuadro de dialogo cuando se preciona un marcador
	public void dialogoMarker(String titulo, LatLng ubicacionMarker) {
		destino = ubicacionMarker;
		for (int i = 0; i < titulo.length(); i++) {
			if (titulo.charAt(i) == ',') {
				// numBloq = Integer.parseInt(titulo.substring(10, i));
				v_codBloq = titulo.substring(0, i);
				v_areaBloq = titulo.substring(i + 7, titulo.length());
			}
		}

		for (int i = 0; i < listaBloques.size(); i++) {
			if (v_codBloq.equalsIgnoreCase(listaBloques.get(i).getCodigo())) {
				idBloque = listaBloques.get(i).getId();
			}
		}
		new TareaObtenerBloque().execute();
	}

	class ListarBloques extends AsyncTask<Void, Void, Void> {
		private ProgressDialog pDialog;
		private ArrayList<Area> listaAreas;
		private Area area;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MapaUnl.this);
			pDialog.setMessage("Cargando puntos..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			
			pDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					ListarBloques.this.cancel(true);
					AlertDialog.Builder builder = new AlertDialog.Builder(MapaUnl.this); 
					builder.setTitle("Alerta");
					builder.setMessage("Esta ud seguro que desea cancelar la actividad...");
					builder.setPositiveButton("Aceptar", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						MapaUnl.this.finish();
					}});
					builder.setNegativeButton("Cancelar", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						new ListarBloques().execute();
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
				// llenar listado de bloques consultados en la BD
				if (opcion.equalsIgnoreCase("map")) {
					listaBloques = new Bloque_Dao().listarBloques();
					listaAreas = new Area_Dao().listarAreas();
				} else if (opcion.equalsIgnoreCase("ar")) {
					listaAreas = new Area_Dao().listarAreas();
					listaBloques = new ArrayList<Bloque>();
					listaBloques.add(new Bloque_Dao()
							.Obtener_bloque_segunID(bExtras.getInt("id_bloq")));
				}
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(Void arg1) {
			if (!isInternetPresent) {
				Toast.makeText(
						MapaUnl.this,
						"No se puede establecer una conexión a internet, Intentelo mas tarde...",
						Toast.LENGTH_LONG).show();
				MapaUnl.this.finish();
			} else {
				// conectamos al hilo principal y añadimos los marcadores al
				// mapa
				for (int i = 0; i < listaBloques.size(); i++) {
					for (int j = 0; j < listaAreas.size(); j++) {
						if (listaAreas.get(j).getId() == listaBloques.get(i)
								.getId_area()) {
							area = listaAreas.get(j);
						}
					}
					añadirmarcador(new LatLng(listaBloques.get(i).getLatitud(),
							listaBloques.get(i).getLongitud()),
							BitmapDescriptorFactory.fromBitmap(area
									.getMarcador()), listaBloques.get(i)
									.getCodigo() + ", Área " + area.getNombre());
					/*
					 * "Bloque " + String.valueOf(listaBloques.get(i)
					 * .getNumero() + ", Area "+area.getNombre()));
					 */
				}
				// evento al momento de dar un clic sobre un marcador
				googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
					public boolean onMarkerClick(Marker marker) {
						dialogoMarker(marker.getTitle(), marker.getPosition());
						return false;
					}
				});

				if (opcion.equalsIgnoreCase("ar")) {
					centrarmapa(new LatLng(listaBloques.get(0).getLatitud(),
							listaBloques.get(0).getLongitud()), 15);
				}
			}
			pDialog.dismiss();
		}
	}

	class TareaObtenerBloque extends AsyncTask<Void, Void, Void> {
		private ProgressDialog pDialog;
		private Area areaSelected;
		private LatLng inicioRuta;
		private LatLng finRuta;
		private Bloque objBloque;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MapaUnl.this);
			pDialog.setMessage("Cargando Informacion..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			
			pDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					TareaObtenerBloque.this.cancel(true);
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
				objBloque = new Bloque_Dao().Obtener_bloque_segunID(idBloque);
				areaSelected = new Area_Dao().Obtener_area_segunID(objBloque
						.getId_area());
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
						MapaUnl.this,
						"No se puede establecer una conexión a internet, Intentelo mas tarde...",
						Toast.LENGTH_LONG).show();
				MapaUnl.this.finish();
			} else {
				limpiarMapa("map");
				inicioRuta = new LatLng(
						googleMap.getMyLocation().getLatitude(), googleMap
								.getMyLocation().getLongitude());
				finRuta = new LatLng(objBloque.getLatitud(),
						objBloque.getLongitud());
				FragmentManager fragmentManager = getSupportFragmentManager();
				DialogoPersonalizado dialogo = new DialogoPersonalizado();
				dialogo.ingresarDatos(objBloque.getId(), objBloque.getNumero(),
						objBloque.getCodigo(), areaSelected.getNombre(),
						objBloque.getImagen_externa(), inicioRuta, finRuta,
						googleMap, tvDistanceDuration);
				dialogo.show(fragmentManager, "tagPersonalizado");
			}
		}
	}

}