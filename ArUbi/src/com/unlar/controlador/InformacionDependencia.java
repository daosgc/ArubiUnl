package com.unlar.controlador;

import com.unlar.modelo.Area;
import com.unlar.modelo.Bloque;
import com.unlar.modelo.Usuario;
import com.unlar.modelo.dao.Area_Dao;
import com.unlar.modelo.dao.Bloque_Dao;
import com.unlar.modelo.dao.Usuario_Dao;
import com.unlar.twitter.MandaTuitTask;
import com.unlar.twitter.OAuthRequestTokenTask;
import com.unlar.twitter.TwitterData;
import com.unlar.utility.ConnectionDetector;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class InformacionDependencia extends Activity {

	// parametros de twitter
	private static CommonsHttpOAuthConsumer httpOauthConsumer;
	private static OAuthProvider httpOauthprovider;

	private Bundle bExtras;
	private ImageView imgVdep;
	private TextView txtnom;
	private TextView txtcodigo;
	private TextView txtarea;
	private TextView txtbloq;
	private TextView txtdelegad;
	private TextView txtcontact;

	private ConnectionDetector cd;
	private Boolean isConectionPresent;
	private Boolean isInternetPresent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_informacion_dependencia);
		cd = new ConnectionDetector(getApplicationContext());
		isConectionPresent = cd.isConnectingToInternet();

		if (!isConectionPresent) {
			Toast.makeText(getBaseContext(),
					"Sin conexión, Intentelo mas tarde...", Toast.LENGTH_LONG)
					.show();
			this.finish();
		} else {
			bExtras = getIntent().getExtras();
			// realizar consultas al web service usando Background Thread
			new ObtenerDependencia().execute();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_vtndependencia, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itmVerMapa:
			Intent i = new Intent(InformacionDependencia.this,
					MapaUnl.class);
			i.putExtra("id_bloq", bExtras.getInt("id_bloq"));
			i.putExtra("opc", "ar");
			startActivity(i);
			return true;
		case R.id.itmcheckinTwt:
			autorizarApp();
			return true;
		case R.id.itmcheckinFcb:
			Intent i1 = new Intent(InformacionDependencia.this,
					FacebookActivity.class);
			i1.putExtra("nombre", txtnom.getText().toString());
			i1.putExtra("bloque", txtbloq.getText().toString());
			i1.putExtra("area", txtarea.getText().toString());
			startActivity(i1);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Called when the OAuthRequestTokenTask finishes (user has authorized the
	 * request token). The callback URL will be intercepted here.
	 */
	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		final Uri uri = intent.getData();
		SharedPreferences preferencias = this.getSharedPreferences(
				"TwitterPrefs", MODE_PRIVATE);

		if (uri != null
				&& uri.toString().indexOf(TwitterData.CALLBACK_URL) != -1) {
			Log.i("MGL", "Callback received : " + uri);

			new RetrieveAccessTokenTask(InformacionDependencia.this,
					getConsumer(), getProvider(), preferencias).execute(uri); // funcion
																				// de
																				// enviar
																				// el
																				// tweet
		}
	}

	protected void autorizarApp() {
		try {

			getProvider().setOAuth10a(true);
			// retrieve the request token
			new OAuthRequestTokenTask(InformacionDependencia.this,
					getConsumer(), getProvider()).execute();

		} catch (Exception e) {

		}
	}

	/**
	 * @return the provider (initialize on the first call)
	 */
	public static OAuthProvider getProvider() {
		if (httpOauthprovider == null) {
			httpOauthprovider = new DefaultOAuthProvider(
					TwitterData.REQUEST_URL, TwitterData.ACCESS_URL,
					TwitterData.AUTHORIZE_URL);
			httpOauthprovider.setOAuth10a(true);
		}
		return httpOauthprovider;
	}

	/**
	 * @param context
	 *            the context
	 * @return the consumer (initialize on the first call)
	 */
	public static CommonsHttpOAuthConsumer getConsumer() {
		if (httpOauthConsumer == null) {
			httpOauthConsumer = new CommonsHttpOAuthConsumer(
					TwitterData.CONSUMER_KEY, TwitterData.CONSUMER_SECRET);
		}
		return httpOauthConsumer;
	}

	public class RetrieveAccessTokenTask extends AsyncTask<Uri, Void, Boolean> {

		private Context context;
		private OAuthProvider provider;
		private OAuthConsumer consumer;
		private SharedPreferences prefs;

		public RetrieveAccessTokenTask(Context context, OAuthConsumer consumer,
				OAuthProvider provider, SharedPreferences prefs) {
			this.context = context;
			this.consumer = consumer;
			this.provider = provider;
			this.prefs = prefs;
		}

		/**
		 * Retrieve the oauth_verifier, and store the oauth and
		 * oauth_token_secret for future API calls.
		 */
		@Override
		protected Boolean doInBackground(Uri... params) {
			final Uri uri = params[0];
			final String oauth_verifier = uri
					.getQueryParameter(OAuth.OAUTH_VERIFIER);

			try {
				Log.i("MGL", "Obtained oAuth Verifier: " + oauth_verifier);

				provider.retrieveAccessToken(consumer, oauth_verifier);

				final Editor edit = prefs.edit();
				edit.putString(OAuth.OAUTH_TOKEN, consumer.getToken());
				edit.putString(OAuth.OAUTH_TOKEN_SECRET,
						consumer.getTokenSecret());
				edit.commit();

				String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
				String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
				if (secret == null || token == null || secret.equals("")
						|| token.equals(""))
					return false;
				consumer.setTokenWithSecret(token, secret);
				context.startActivity(new Intent(context,
						InformacionDependencia.class));

				Log.i("MGL", "OAuth - Access Token Retrieved");

			} catch (Exception e) {
				Log.e("MGL", "OAuth - Access Token Retrieval Error", e);
			}

			return true;
		}

		protected void onPostExecute(Boolean result) {

			if (result) {
				String tuit = "Ha estado en " + txtnom.getText().toString()
						+ " del bloque "
						+ txtbloq.getText().toString().substring(0, 1) + " de "
						+ txtarea.getText().toString()
						+ ". Compartido a travéz de @ArUbiUnl";
				new MandaTuitTask(tuit, prefs).execute();

				Toast.makeText(this.context, "Acceso a twitter conseguido!",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this.context,
						"Acceso a twitter NO conseguido! :(",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	/**
	 * Background Async Task to Create new product
	 * */
	class ObtenerDependencia extends AsyncTask<Void, Void, Void> {
		private ProgressDialog pDialog;
		private Bloque bloque;
		private Usuario delegado;
		private Area area;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(InformacionDependencia.this);
			pDialog.setMessage("Cargando datos..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			
			pDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					ObtenerDependencia.this.cancel(true);
					AlertDialog.Builder builder = new AlertDialog.Builder(InformacionDependencia.this); 
					builder.setTitle("Alerta");
					builder.setMessage("Esta ud seguro que desea cancelar la actividad...");
					builder.setPositiveButton("Aceptar", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						InformacionDependencia.this.finish();
					}});
					builder.setNegativeButton("Cancelar", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						new ObtenerDependencia().execute();
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
				// consulta en la bd
				bloque = new Bloque_Dao().Obtener_bloque_segunID(bExtras
						.getInt("id_bloq"));
				delegado = new Usuario_Dao().Obtener_Delegado_segunID(bExtras
						.getInt("id_delegado"));
				area = new Area_Dao().Obtener_area_segunID(bloque.getId_area());
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
						InformacionDependencia.this,
						"No se puede establecer una conexión a internet, Intentelo mas tarde...",
						Toast.LENGTH_LONG).show();
				InformacionDependencia.this.finish();
			} else {
				imgVdep = (ImageView) findViewById(R.id.imgvtninfor);
				txtnom = (TextView) findViewById(R.id.nomvtninfor);
				txtcodigo = (TextView) findViewById(R.id.codvtninfor);
				txtarea = (TextView) findViewById(R.id.areavtninfor);
				txtbloq = (TextView) findViewById(R.id.bloqvtninfor);
				txtdelegad = (TextView) findViewById(R.id.delegadvtninfor);
				txtcontact = (TextView) findViewById(R.id.contactvtninfor);

				// fijamos los datos en cada uno de los elementos de la pantalla
				txtnom.setText(bExtras.getString("nombre"));
				txtcodigo.setText(bExtras.getString("codigo"));
				imgVdep.setImageBitmap(bloque.getImagen_externa());
				txtarea.setText(area.getNombre());
				txtbloq.setText(String.valueOf(bloque.getNumero() + " Código.-"
						+ bloque.getCodigo()));
				txtdelegad.setText(delegado.getNombres() + " "
						+ delegado.getApellidos());
				txtcontact.setText(delegado.getCorreo());
			}
		}
	}
}
