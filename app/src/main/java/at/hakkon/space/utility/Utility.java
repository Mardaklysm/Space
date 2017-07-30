package at.hakkon.space.utility;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import at.hakkon.space.datamodel.event.AbsEvent;

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

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
}


