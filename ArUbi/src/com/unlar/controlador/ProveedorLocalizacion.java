package com.unlar.controlador;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

import com.unlar.controlador.ArchitectViewInterfaz.ILocationProvider;




/**
 * Aplicación de muestra de un LocationProvider
 */
public class ProveedorLocalizacion implements ILocationProvider {

	/** location listener llamado en cada actualizacion de la ubicacion del dispositivo */
	private final LocationListener	locationListener;

	/** system's locationManager permisos para acceder a la ubicaicon mediante GPS / RED  */
	private final LocationManager	locationManager;

	/** actualizaciones de ubicación deben disparar aproximadamente cada segundo */
	private static final int		LOCATION_UPDATE_MIN_TIME_GPS	= 1000;

	/** actualizaciones de ubicación disparará, incluso si la última señal es la misma que la actual (segun los mtrs que se mueve el dispositivo) */
	private static final int		LOCATION_UPDATE_DISTANCE_GPS	= 0;

	/** actualizaciones de ubicación deben disparar aproximadamente cada segundo */
	private static final int		LOCATION_UPDATE_MIN_TIME_NW		= 1000;

	/** actualizaciones de ubicación disparará, incluso si la última señal es la misma que la actual (segun los mtrs que se mueve el dispositivo) */
	private static final int		LOCATION_UPDATE_DISTANCE_NW		= 0;

	/** al lugar de acceso más rápido, incluso el uso de 10 minutos despues de puesto en marcha */
	private static final int		LOCATION_OUTDATED_WHEN_OLDER_MS	= 1000 * 60 * 10;

	/** si gpsProvider and networkProvider son habilitados en la configuracion del sistema */
	private boolean					gpsProviderEnabled, networkProviderEnabled;

	/** el contexto con el cual se esta trabajando */
	private final Context			context;


	public ProveedorLocalizacion( final Context context, LocationListener locationListener ) {
		super();
		this.locationManager = (LocationManager)context.getSystemService( Context.LOCATION_SERVICE );
		this.locationListener = locationListener;
		this.context = context;
		this.gpsProviderEnabled = this.locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER );
		this.networkProviderEnabled = this.locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER );
	}

	@Override
	public void onPause() {
		if ( this.locationListener != null && this.locationManager != null && (this.gpsProviderEnabled || this.networkProviderEnabled) ) {
			this.locationManager.removeUpdates( this.locationListener );
		}
	}

	@Override
	public void onResume() {
		if ( this.locationManager != null && this.locationListener != null ) {

			// check which providers are available are available
			this.gpsProviderEnabled = this.locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER );
			this.networkProviderEnabled = this.locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER );

			/** is GPS provider enabled? */
			if ( this.gpsProviderEnabled ) {
				final Location lastKnownGPSLocation = this.locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
				if ( lastKnownGPSLocation != null && lastKnownGPSLocation.getTime() > System.currentTimeMillis() - LOCATION_OUTDATED_WHEN_OLDER_MS ) {
					locationListener.onLocationChanged( lastKnownGPSLocation );
				}
				if (locationManager.getProvider(LocationManager.GPS_PROVIDER)!=null) {
					this.locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, LOCATION_UPDATE_MIN_TIME_GPS, LOCATION_UPDATE_DISTANCE_GPS, this.locationListener );
				}
			}

			/** is Network / WiFi positioning provider available? */
			if ( this.networkProviderEnabled ) {
				final Location lastKnownNWLocation = this.locationManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );
				if ( lastKnownNWLocation != null && lastKnownNWLocation.getTime() > System.currentTimeMillis() - LOCATION_OUTDATED_WHEN_OLDER_MS ) {
					locationListener.onLocationChanged( lastKnownNWLocation );
				}
				if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER)!=null) {
					this.locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_MIN_TIME_NW, LOCATION_UPDATE_DISTANCE_NW, this.locationListener );
				}
			}

			/** user didn't check a single positioning in the location settings, recommended: handle this event properly in your app, e.g. forward user directly to location-settings, new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS ) */
			if ( !this.gpsProviderEnabled || !this.networkProviderEnabled ) {
				Toast.makeText( this.context, "Please enable GPS and Network positioning in your Settings ", Toast.LENGTH_LONG ).show();
			}
		}
	}
}
