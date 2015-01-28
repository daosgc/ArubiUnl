package com.unlar.controlador;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogoPersonalizado extends DialogFragment {

	private int idBloque;
	private int numeroBloque;
	private String codigoBloque;
	private String areaBloque;
	private Bitmap fotoBloque;

	private TextView tvCodigoBloq;
	private TextView tvAreaBloq;
	private ImageView imvFotoBloq;

	private LatLng origen;
	private LatLng destino;
	private GoogleMap mapa;
	private TextView tvDistancia;
	
	public static final String EXTRAS_KEY_ACTIVITY_TITLE_STRING = "activityTitle";
	public static final String EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL = "activityArchitectWorldUrl";
	public static final String EXTRAS_ID_BLOQUE = "idBloque";
	
	public void ingresarDatos(int id, int numero, String codigo, String area,
			Bitmap foto, LatLng inicio, LatLng fin, GoogleMap gMapa, TextView tvDistancia) {
		this.idBloque = id;
		this.numeroBloque = numero;
		this.codigoBloque = codigo;
		this.areaBloque = area;
		this.fotoBloque = foto;
		this.origen = inicio;
		this.destino = fin;
		this.mapa = gMapa;
		this.tvDistancia = tvDistancia;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// fijamos los datos en cada uno de los elementos de la pantalla
		// imgVdep.setImageBitmap(bloque.getImagen_externa());
		// txtnom.setText(bExtras.getString("nombre"));

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		builder.setTitle("Bloque " + numeroBloque);
		View view = inflater.inflate(R.layout.dialogo_personalizado, null);
		tvCodigoBloq = (TextView) view.findViewById(R.id.tvCodigo);
		tvAreaBloq = (TextView) view.findViewById(R.id.tvArea);
		imvFotoBloq = (ImageView) view.findViewById(R.id.ivFoto);

		imvFotoBloq.setImageBitmap(fotoBloque);
		tvCodigoBloq.setText("Código.-" + codigoBloque);
		tvAreaBloq.setText("Área " + areaBloque);

		builder.setView(view)
				.setPositiveButton("Vista en RA",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent intento = new Intent(getActivity(),
										RealidadAumentada.class);
								intento.putExtra(EXTRAS_KEY_ACTIVITY_TITLE_STRING,
										"ArUbi");
								intento.putExtra(EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL, "http://arubiunl.com/serverforArUbi/unlar/dependencias/index.html");
								intento.putExtra(EXTRAS_ID_BLOQUE, idBloque);
								intento.putExtra("opc", "map");
								startActivity(intento);
							}
						})
				.setNegativeButton("Como llegar...",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								new RutaDependencia(origen, destino, mapa, tvDistancia);
								
							}
						});
		;

		return builder.create();
	}
}
