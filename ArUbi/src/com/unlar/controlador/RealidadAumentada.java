package com.unlar.controlador;

import android.content.Intent;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.net.Uri;
import android.widget.Toast;

import com.wikitude.architect.ArchitectView.ArchitectUrlListener;
import com.wikitude.architect.ArchitectView.SensorAccuracyChangeListener;

public class RealidadAumentada extends ArchitectCamActivity {

	/**
	 * clave extras para el título de la actividad, por lo general estática y configurada en Manifest.xml
	 */
	protected static final String EXTRAS_KEY_ACTIVITY_TITLE_STRING = "activityTitle";
	
	/**
	 * clave extras para el arquitecto-url para cargar, por lo general ya conoce por adelantado,
	 * puede ser carpeta relativa a los activos (myWorld.html -> Activos / myWorld.html se carga) 
	 * o web-url ("http://myserver.com/myWorld . html "). 
	 */
	protected static final String EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL = "activityArchitectWorldUrl";
	
	protected static final String EXTRAS_ID_BLOQUE = "idBloque";
	
	/**
	 * tenga en cuenta que esta clave sólo es válida para esta aplicación de ejemplo, si necesita uno para su propia aplicación, por favor visite www.wikitude.com (SDK Store)
	 */
	protected static final String WIKITUDE_SDK_KEY = "D/HsrTjpJl/erFCHUJ7Z14Sn+XR1Rn1oCzTHhq02z6IWp0lFqUAoQhjrM4JX6xRh4+huadkz15irYUXkbqUGCoISm8TtlvbJASwhnVlURyt1pQYtc7Rbr5wxGo4Tc6ksvrqqkUd8uSgNDW8YkNAi36IYyq6jtGuePIiW/MPTM/dTYWx0ZWRfX+O5ezmrj4/vocQDwlXCn441weZ56ODjNQDsKwCGKRnxOrBaDX/yL8AZmsa5Z6AGsviMUPfRfNCueCap76EFpwzF1uZ4hhw+tCLRrb44D+EC0+8UyJroP78bhRkJpRej8bdlyYUVpncffS46IGeLi8nlDE+GXD/Iuz+v+PDPPrWRjaP9g+tq2RgaWfVQqsRqMi3kfRJkpgWiHcxHhRAsQq+IrZ7XKywpAJyg/H2GKSmHJI9VAWIxY8jBwL4xU9UBJudH+OPFNkW/OQnJlDgkdo3O+msA4bUlSJhhZFurxnWsMYZbIlNnvyUfoKL2Er48A+XPsqHeh0JYkIMJOggV9WHnlvpXT57KE1yHrCoHUIGMd/TJlOYL+vnfYaP0fdnyGr877H0hSVyZZ/YK491rKXkyQ1KWAj/xSEGqH1X1JJESiSeKPj/9K/iNkCmZf5+Fa/zxLRhT0njclGjxJJxH95VJ7Id2dh5q1l61O44vQrOmjll+lxoDf24=";
	//protected static final String WIKITUDE_SDK_KEY = "";
	
	/**
	 * mensaje cuando la brujula necesita calibracion
	 */
	private long lastCalibrationToastShownTimeMillis = System.currentTimeMillis();

	@Override
	public String getARchitectWorldPath() {
		return getIntent().getExtras().getString(
				EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL);
	}

	@Override
	public String getActivityTitle() {
		return (getIntent().getExtras() != null && getIntent().getExtras().get(
				EXTRAS_KEY_ACTIVITY_TITLE_STRING) != null) ? getIntent()
				.getExtras().getString(EXTRAS_KEY_ACTIVITY_TITLE_STRING)
				: "Test-World";
	}

	@Override
	public int getContentViewId() {
		return R.layout.activity_realidad_aumentada;
	}

	@Override
	public int getArchitectViewId() {
		return R.id.architectView;
	}
	
	@Override
	public int getIdBloque() {
		return getIntent().getExtras().getInt(
				EXTRAS_ID_BLOQUE);
	}
	
	@Override
	public String getWikitudeSDKLicenseKey() {
		return WIKITUDE_SDK_KEY;
	}
	
	@Override
	public SensorAccuracyChangeListener getSensorAccuracyListener() {
		return new SensorAccuracyChangeListener() {
			@Override
			public void onCompassAccuracyChanged( int accuracy ) {
				/* UNRELIABLE = 0, LOW = 1, MEDIUM = 2, HIGH = 3 */
				if ( accuracy < SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM && RealidadAumentada.this != null && !RealidadAumentada.this.isFinishing() && System.currentTimeMillis() - RealidadAumentada.this.lastCalibrationToastShownTimeMillis > 5 * 1000) {
					Toast.makeText( RealidadAumentada.this, R.string.compass_accuracy_low, Toast.LENGTH_LONG ).show();
					RealidadAumentada.this.lastCalibrationToastShownTimeMillis = System.currentTimeMillis();
				}
			}
		};
	}

	@Override
	public ArchitectUrlListener getUrlListener() {
		return new ArchitectUrlListener() {

			@Override
			public boolean urlWasInvoked(String uriString) {
				// by default: no action applied when url was invoked
				Uri invokedUri = Uri.parse(uriString);
				if ("markerselected".equalsIgnoreCase(invokedUri.getHost())) {
						if(getIntent().getExtras().getString("opc").equalsIgnoreCase("ar")){
							final Intent poiDetailIntent = new Intent(RealidadAumentada.this, InformacionBloque.class);
							poiDetailIntent.putExtra("idBloque", Integer.parseInt(invokedUri.getQueryParameter("id")) );
							RealidadAumentada.this.startActivity(poiDetailIntent);
							return true;
						}else if(getIntent().getExtras().getString("opc").equalsIgnoreCase("map")){
							final Intent poiDetailIntent = new Intent(RealidadAumentada.this, InformacionDependencia.class);
							poiDetailIntent.putExtra("nombre", String.valueOf(invokedUri.getQueryParameter("nombre")));
							poiDetailIntent.putExtra("codigo", String.valueOf(invokedUri.getQueryParameter("codigo")));
							poiDetailIntent.putExtra("id_delegado", Integer.parseInt(invokedUri.getQueryParameter("idDelegado")));
							poiDetailIntent.putExtra("id_bloq", Integer.parseInt(invokedUri.getQueryParameter("idBloque")));
							RealidadAumentada.this.startActivity(poiDetailIntent);
							return true;
						}						
				}
				return false;
			}
		};
	}

	@Override
	public ILocationProvider getLocationProvider(final LocationListener locationListener) {
		return new ProveedorLocalizacion(this, locationListener);
	}
	
	@Override
	public float getInitialCullingDistanceMeters() {
		// you need to adjust this in case your POIs are more than 50km away from user here while loading or in JS code (compare 'AR.context.scene.cullingDistance')
		return ArchitectViewInterfaz.CULLING_DISTANCE_DEFAULT_METERS;
	}

}
