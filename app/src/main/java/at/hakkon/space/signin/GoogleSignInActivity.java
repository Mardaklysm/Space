package at.hakkon.space.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;

import at.hakkon.space.R;
import at.hakkon.space.activity.MainActivity;
import at.hakkon.space.application.ApplicationClass;



public class GoogleSignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

	private static final String TAG = "T1_GSignInActivity";

	private static int RC_SIGN_IN = 9001;

	private boolean mResolvingConnectionFailure = false;
	private boolean mAutoStartSignInflow = true;
	private boolean mSignInClicked = false;

	private static GoogleApiClient googleApiClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "GoogleSignInActivity");

		setupGoogleClient();
	}



	public void setupGoogleClient() {
		googleApiClient = new GoogleApiClient.Builder(this)
				.addApi(Games.API).addScope(Games.SCOPE_GAMES)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();

		ApplicationClass.getInstance().setGoogleClient(googleApiClient);
	}


	@Override
	public void onStart() {
		super.onStart();
		googleApiClient.connect();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RC_SIGN_IN) {
			mSignInClicked = false;
			mResolvingConnectionFailure = false;
			if (resultCode == RESULT_OK) {
				googleApiClient.connect();
			} else {
				// Bring up an error dialog to alert the user that sign-in
				// failed. The R.string.signin_failure should reference an error
				// string in your strings.xml file that tells the user they
				// could not be signed in, such as "Unable to sign in."
				BaseGameUtils.showActivityResultError(this,
						requestCode, resultCode, R.string.sign_in_other_error);
			}
		}
	}

	private void startNextActivity() {
		finish();
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}


	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Toast.makeText(this, "Google Client Connection failed:\n" + connectionResult.toString(), Toast.LENGTH_LONG ).show();

		if (mResolvingConnectionFailure) {
			// already resolving
			return;
		}

		// if the sign-in button was clicked or if auto sign-in is enabled,
		// launch the sign-in flow
		if (mSignInClicked || mAutoStartSignInflow) {
			mAutoStartSignInflow = false;
			mSignInClicked = false;
			mResolvingConnectionFailure = true;

			// Attempt to resolve the connection failure using BaseGameUtils.
			// The R.string.signin_other_error value should reference a generic
			// error string in your strings.xml file, such as "There was
			// an issue with sign-in, please try again later."

			if (!BaseGameUtils.resolveConnectionFailure(this, googleApiClient, connectionResult, RC_SIGN_IN, R.string.sign_in_other_error)) {
				mResolvingConnectionFailure = false;
				Toast.makeText(this, "Conflict NOT RESOLVED :((", Toast.LENGTH_LONG ).show();
			}else{
				Toast.makeText(this, "Conflict RESOLVED", Toast.LENGTH_LONG ).show();
				//startNextActivity();
			}

		}

		// Put code here to display the sign-in button

		//Log.d(TAG, "onConnectionFailed:" + connectionResult);
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {
		Toast.makeText(this, "Google Client Connected.", Toast.LENGTH_LONG ).show();
		startNextActivity();
	}

	@Override
	public void onConnectionSuspended(int i) {
		Toast.makeText(this, "Google Client Connection Suspended.", Toast.LENGTH_LONG ).show();
	}
}