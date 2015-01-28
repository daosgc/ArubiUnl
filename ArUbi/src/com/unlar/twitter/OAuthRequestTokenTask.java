package com.unlar.twitter;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

/**
 * An asynchronous task that communicates with Twitter to retrieve a request
 * token. (OAuthGetRequestToken)
 * 
 * After receiving the request token from Twitter, pop a browser to the user
 * to authorize the Request Token. (OAuthAuthorizeToken)
 * 
 */
public class OAuthRequestTokenTask extends AsyncTask<Void, Void, Void> {

	private static final String TAG = "MGL";
	private Context context;
	private OAuthProvider provider;
	private OAuthConsumer consumer;

	public OAuthRequestTokenTask(Context context, OAuthConsumer consumer,
			OAuthProvider provider) {
		this.context = context;
		this.consumer = consumer;
		this.provider = provider;
	}

	/**
	 * 
	 * Retrieve the OAuth Request Token and present a browser to the user to
	 * authorize the token.
	 * 
	 */
	@Override
	protected Void doInBackground(Void... params) {

		try {
			Log.i(TAG, "Retrieving request token from twitter servers");
			final String url = provider.retrieveRequestToken(consumer,
					TwitterData.CALLBACK_URL);
			Log.i(TAG, "Popping a browser with the authorize URL : " + url);
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url))
					.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
							| Intent.FLAG_ACTIVITY_NO_HISTORY
							| Intent.FLAG_FROM_BACKGROUND);
			context.startActivity(intent);
		} catch (Exception e) {
			Log.e(TAG, "Error during OAUth retrieve request token", e);
		}

		return null;
	}

}
