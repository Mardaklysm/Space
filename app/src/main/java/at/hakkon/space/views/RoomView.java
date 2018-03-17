package at.hakkon.space.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.TypedValue;
import android.view.View;

import at.hakkon.space.R;
import at.hakkon.space.datamodel.room.AbsRoom;
import at.hakkon.space.datamodel.ship.AbsShip;
import at.hakkon.space.utility.Utility;

/**
 * Created by Markus on 29.07.2017.
 */

public class RoomView extends android.support.v7.widget.AppCompatButton {

	private View view;

	private AbsShip ship;
	private int roomIndex;
	private AbsRoom room;

	public RoomView(Context context, AbsShip ship, int roomIndex) {
		super(context);
		this.ship = ship;
		this.roomIndex = roomIndex;

		if (roomIndex >= ship.getRooms().size()) {
			throw new RuntimeException("Invalid roomIndex in RoomView constructor");
		}
		room = ship.getRooms().get(roomIndex);

		update();

		//Style
		setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.font_size_room_view));
	}

	public void update() {
		String text = room.getName() + " LVL." + room.getLevel() + "\n(" + Utility.roundTwoDecimals(room.getEffectiveEfficency()) + ")";
		this.setText(text);

		int health = room.getHealth();
		int maxHealth = room.getMaxHealth();

		float alpha = (float)health/(float)maxHealth;

		setAlpha(Math.max(0.1f,alpha));

		if (room.isShielded()){
			getBackground().setColorFilter(colorShieldOn, PorterDuff.Mode.DARKEN);
		}else{
			getBackground().setColorFilter(null);
		}

		/**
		if (health <= maxHealth * 0.20) {
			setAlpha(0.2f);
			//getBackground().setColorFilter(colorP10, PorterDuff.Mode.DARKEN);
		} else if (health <= maxHealth * 0.50) {
			setAlpha(0.5f);
			//getBackground().setColorFilter(colorP25, PorterDuff.Mode.DARKEN);
		} else if (health <= maxHealth * 0.75) {
			//getBackground().setColorFilter(colorP50, PorterDuff.Mode.DARKEN);
			setAlpha(0.75f);
		} else if (health <= maxHealth * 0.99) {
			//getBackground().setAlpha(220);
			setAlpha(0.9f);
			//getBackground().setColorFilter(colorP75, PorterDuff.Mode.DARKEN);
		} else {
		//getBackground().setColorFilter(colorP100, PorterDuff.Mode.DARKEN);
			getBackground().setAlpha(255);
		}
		**/

	}




	public AbsShip getShip() {
		return ship;
	}

	private static int colorP10 = Color.parseColor("#de3923");
	private static int colorP25 = Color.parseColor("#de8a27");
	private static int colorP50 = Color.parseColor("#e7e02a");
	private static int colorP75 = Color.parseColor("#aeba24");
	private static int colorP100 = Color.parseColor("#4ca22a");

	private static int colorShieldOn = Color.parseColor("#1273ec");


}