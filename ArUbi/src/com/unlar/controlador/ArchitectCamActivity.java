package com.unlar.controlador;

import java.io.IOException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.media.AudioManager;
import android.opengl.GLES20;
import android.os.Bundle;
import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.ArchitectView.ArchitectConfig;
import com.wikitude.architect.ArchitectView.ArchitectUrlListener;
import com.wikitude.architect.ArchitectView.SensorAccuracyChangeListener;

/**
 * Resumen actividad que se ocupa de los eventos de ciclo directo de un ArchitectView. 
 */


public abstract class ArchitectCamActivity extends Activity implements ArchitectViewInterfaz{

	/**
	 * Wikitude SDK AR-View aquí es donde se configuran los marcadores, brújula, modelos 3D, etc que seran representados en la camara
	 */
	protected ArchitectView					architectView;
	
	/**
	 * sensor oyente en caso de que quiera mostrar sugerencias de calibración
	 */
	protected SensorAccuracyChangeListener	sensorAccuracyListener;
	
	/**
	 * última ubicación conocida del usuario
	 */
	protected Location 						lastKnownLocaton;

	/**
	 * estrategia de ubicación
	 */
	protected ILocationProvider				locationProvider;
	
	/**
	 * recibe actualizaciones de ubicación para ser enviadas al architectView
	 */
	protected LocationListener 				locationListener;
	
	/**
	 * urlListener se usa para llamar el mundo arquitecto ejm "document.location= 'architectsdk://...' o " llamadas JavaScript a un metodo del mundo arquitecto"
	 */
	protected ArchitectUrlListener 			urlListener;
	
	/** Se le llama cuando se crea por primera vez la actividad. */
	@SuppressLint("NewApi")
	@Override
	public void onCreate( final Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		/* presionando las teclas volume subir/bajar manipula el sonido en el mundo arquitecto */
		this.setVolumeControlStream( AudioManager.STREAM_MUSIC );

		/* config una vista de contenido */
		this.setContentView( this.getContentViewId() );
		
		this.setTitle( this.getActivityTitle() );
		
		/* set AR-view para las notificaciones del ciclo de vida etc. */
		this.architectView = (ArchitectView)this.findViewById( this.getArchitectViewId()  );

		/* clave SDK, éste sólo es válido para este identificador de paquete y no debe ser utilizado en otro lugar */
		final ArchitectConfig config = new ArchitectConfig( this.getWikitudeSDKLicenseKey() );

		/* primera notificación del ciclo de vida obligatorio */
		this.architectView.onCreate( config );

		// set accuracy listener if implemented, you may e.g. show calibration prompt for compass using this listener
		this.sensorAccuracyListener = this.getSensorAccuracyListener();
		
		// set urlListener, any calls made in JS like "document.location = 'architectsdk://foo?bar=123'" is forwarded to this listener, use this to interact between JS and native Android activity/fragment
		this.urlListener = this.getUrlListener();  
		
		// register valid urlListener in architectView, ensure this is set before content is loaded to not miss any event
		if ( this.urlListener !=null ) {
			this.architectView.registerUrlListener( this.getUrlListener() );
		}
		
		// listener passed over to locationProvider, any location update is handled here
		this.locationListener = new LocationListener() {

			@Override
			public void onStatusChanged( String provider, int status, Bundle extras ) {
			}

			@Override
			public void onProviderEnabled( String provider ) {
			}

			@Override
			public void onProviderDisabled( String provider ) {
			}

			@Override
			public void onLocationChanged( final Location location ) {
				// forward location updates fired by LocationProvider to architectView, you can set lat/lon from any location-strategy
				if (location!=null) {
				// sore last location as member, in case it is needed somewhere (in e.g. your adjusted project)
				ArchitectCamActivity.this.lastKnownLocaton = location;
				if ( ArchitectCamActivity.this.architectView != null ) {
					// check if location has altitude at certain accuracy level & call right architect method (the one with altitude information)
					if ( location.hasAltitude() && location.hasAccuracy() && location.getAccuracy()<1) {
						ArchitectCamActivity.this.architectView.setLocation( location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getAccuracy() );
					} else {
						ArchitectCamActivity.this.architectView.setLocation( location.getLatitude(), location.getLongitude(), location.hasAccuracy() ? location.getAccuracy() : 1000 );
					}
				}
				}
			}
		};

		// locationProvider used to fetch user position
		this.locationProvider = getLocationProvider( this.locationListener );
		
	}

	@Override
	protected void onPostCreate( final Bundle savedInstanceState ) {
		super.onPostCreate( savedInstanceState );
		
		if ( this.architectView != null ) {
			
			// call mandatory live-cycle method of architectView
			this.architectView.onPostCreate();
			
			try {			
				// load content via url in architectView, ensure '<script src="architect://architect.js"></script>' is part of this HTML file, have a look at wikitude.com's developer section for API references
				this.architectView.load( this.getARchitectWorldPath() );
				
				if(this.getIdBloque()!=0){
					this.architectView.callJavascript("World.getIdBloque( " + this.getIdBloque() + " );");
				}
				
				if (this.getInitialCullingDistanceMeters() != ArchitectViewInterfaz.CULLING_DISTANCE_DEFAULT_METERS) {
					// set the culling distance - meaning: the maximum distance to render geo-content
					this.architectView.setCullingDistance( this.getInitialCullingDistanceMeters() );
				}
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		// call mandatory live-cycle method of architectView
		if ( this.architectView != null ) {
			this.architectView.onResume();
			
			// register accuracy listener in architectView, if set
			if (this.sensorAccuracyListener!=null) {
				this.architectView.registerSensorAccuracyChangeListener( this.sensorAccuracyListener );
			}
		}

		// tell locationProvider to resume, usually location is then (again) fetched, so the GPS indicator appears in status bar
		if ( this.locationProvider != null ) {
			this.locationProvider.onResume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		// call mandatory live-cycle method of architectView
		if ( this.architectView != null ) {
			this.architectView.onPause();
			
			// unregister accuracy listener in architectView, if set
			if ( this.sensorAccuracyListener != null ) {
				this.architectView.unregisterSensorAccuracyChangeListener( this.sensorAccuracyListener );
			}
		}
		
		// tell locationProvider to pause, usually location is then no longer fetched, so the GPS indicator disappears in status bar
		if ( this.locationProvider != null ) {
			this.locationProvider.onPause();
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		// call mandatory live-cycle method of architectView
		if ( this.architectView != null ) {
			this.architectView.onDestroy();
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		if ( this.architectView != null ) {
			this.architectView.onLowMemory();
		}
	}

	/**
	 * titulo visible en la actividad
	 * @return
	 */
	public abstract String getActivityTitle();
	
	/**
	 * ruta de acceso al archivo del arquitecto (AR-Experiencia HTML)
	 * @return
	 */
	@Override
	public abstract String getARchitectWorldPath();
	
	/**
	 * url oyente que se dispara ejm. 'document.location = "architectsdk://foo?id=1"' es una llamada en JS
	 * @return
	 */
	@Override
	public abstract ArchitectUrlListener getUrlListener();
	
	/**
	 * @return id del layout que contiene el ARchitect View, ejm. R.layout.camview
	 */
	@Override
	public abstract int getContentViewId();
	@Override
	public abstract int getIdBloque();
	
	/**
	 * @return Licencia del Wikitude SDK, verificar www.wikitude.com para mas detalles
	 */
	@Override
	public abstract String getWikitudeSDKLicenseKey();
	
	/**
	 * @return layout-id para el architectView, ejm. R.id.architectView
	 */
	@Override
	public abstract int getArchitectViewId();

	/**
	 * 
	 * @return Implementación de una Localización
	 */
	@Override
	public abstract ILocationProvider getLocationProvider(final LocationListener locationListener);
	
	/**
	 * @return Implementación de Sensor-Accuracy-Listener. De esta forma puede por ejemplo mostrar prompt para calibrar la brújula
	 */
	@Override
	public abstract ArchitectView.SensorAccuracyChangeListener getSensorAccuracyListener();
	
	/**
	 * ayudante para comprobar si video-drawables son compatibles con este dispositivo. recomendada para comprobar antes de lanzar Mundos arquitecto con videodrawables 
	 * @return verdadero si AR.VideoDrawables son soportados,  caso contrario retornaria falso
	 */
	public static final boolean isVideoDrawablesSupported() {
		String extensions = GLES20.glGetString( GLES20.GL_EXTENSIONS );
		return extensions != null && extensions.contains( "GL_OES_EGL_image_external" ) && android.os.Build.VERSION.SDK_INT >= 14 ;
	}

}