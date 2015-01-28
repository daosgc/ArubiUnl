package com.unlar.controlador;

import android.location.LocationListener;

import com.wikitude.architect.ArchitectView.ArchitectUrlListener;
import com.wikitude.architect.ArchitectView.SensorAccuracyChangeListener;

public interface ArchitectViewInterfaz {
	
	/**
	 * 50km = architectView's cullingDistance por defecto, retorna este valor en "getInitialCullingDistanceMeters()".
	 */
	public static final int CULLING_DISTANCE_DEFAULT_METERS = 50 * 1000;
	
	/**
	 * ruta de acceso al archivo del arquitecto (AR-Experiencia HTML)
	 * @return
	 */
	public String getARchitectWorldPath();
	
	/**
	 * url oyente que se dispara ejm. 'document.location = "architectsdk://foo?id=1"' es una llamada en JS
	 * @return
	 */
	public ArchitectUrlListener getUrlListener();
	
	/**
	 * @return id del layout que contiene el ARchitect View, ejm. R.layout.camview
	 */
	public int getContentViewId();
	public int getIdBloque();
	
	/**
	 * @return Licencia del Wikitude SDK, verificar www.wikitude.com para mas detalles
	 */
	public String getWikitudeSDKLicenseKey();
	
	/**
	 * @return layout-id para el architectView, ejm. R.id.architectView
	 */
	public int getArchitectViewId();

	/**
	 * 
	 * @return Implementación de una Localización
	 */
	public ILocationProvider getLocationProvider(final LocationListener locationListener);
	
	/**
	 * @return Implementación de Sensor-Accuracy-Listener. De esta forma puede por ejemplo mostrar prompt para calibrar la brújula
	 */
	public SensorAccuracyChangeListener getSensorAccuracyListener();
	
	/**
	 * establece la distancia máxima para presentar los puntos de interes. En caso de que los lugares son más de 50 km de distancia desde el usuario debe ajustar este valor (compare 'AR.context.scene.cullingDistance'). 
	 * ArchitectViewHolder.CULLING_DISTANCE_DEFAULT_METERS Cambiar el comportamiento por defecto (rango de 50 km) o cualquier valor positivo para establecer cullingDistance en el arranque architectView.
	 * @return
	 */
	public float getInitialCullingDistanceMeters();
	
	/**
	 * Interfaz para la ubicación, que se encarga de las posiciones GPS / red que se ocupa en los eventos de un ciclo de vida
	 */
	public static interface ILocationProvider {

		/**
		 * Llamada cuando se reanude una actividad (normalmente dentro de un ciclo de vida de una actividad)
		 */
		public void onResume();

		/**
		 * Llamada cuando una actividad esta en pausa (por lo general dentro de un ciclo de vida de una actividad)
		*/
		public void onPause();

	}

}
