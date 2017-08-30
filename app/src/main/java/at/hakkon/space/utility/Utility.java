package at.hakkon.space.utility;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import java.math.BigDecimal;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.galaxy.AbsPlanet;
import at.hakkon.space.datamodel.ship.PlayerShip;
import at.hakkon.space.event.AbsEvent;

/**
 * Created by Markus on 30.07.2017.
 */

public class Utility {

	private final static String TAG = "Utility";

	private static Utility instance;


	public static Utility getInstance() {
		if (instance == null) {
			instance = new Utility();
		}

		return instance;
	}

	public void showYesNoDialog(final Context context, String text, final AbsEvent event) {

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						event.callback(context, 1);
						break;

					case DialogInterface.BUTTON_NEGATIVE:
						event.callback(context, 2);
						break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(ApplicationClass.getInstance().getActiveContext());
		builder.setCancelable(false);
		builder.setMessage(text).setPositiveButton("Yes", dialogClickListener)
				.setNegativeButton("No", dialogClickListener).show();

	}

	public void showTextDialog(Context context, String text) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(text).show();
	}


	private AlertDialog alertDialog;

	public void showQuestionsDialog(final Context context, String text, CharSequence[] charSequences, final AbsEvent event) {


		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if (charSequences.length > 0) {
			builder.setCancelable(false);
		}


		TextView textView = new TextView(context);
		textView.setText(text);

		builder.setCustomTitle(textView);


		alertDialog = builder.setItems(charSequences, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				event.callback(context, which);
			}
		}).show();
	}

	public static int getTravelCost(PlayerShip ship, AbsPlanet planet) {
		AbsPlanet startPlanet = ship.getCurrentPlanet();

		int distanceX = Math.abs(startPlanet.getPlanetPosition().getX() - planet.getPlanetPosition().getX());
		int distanceY = Math.abs(startPlanet.getPlanetPosition().getY() - planet.getPlanetPosition().getY());


		int totDistance = distanceX + distanceY * 2;

		return totDistance;


	}

	public static double roundTwoDecimals(double d) {
		BigDecimal a = new BigDecimal(d);
		BigDecimal roundOff = a.setScale(2, BigDecimal.ROUND_HALF_UP);
		return roundOff.doubleValue();
	}
}


