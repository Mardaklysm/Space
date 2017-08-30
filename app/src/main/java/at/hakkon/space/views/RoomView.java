package at.hakkon.space.views;

import android.content.Context;
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
		String text = room.getName() + "\n(" + Utility.roundTwoDecimals(room.getEffectiveEfficency()) + ")";
		this.setText(text);

	}


	public AbsShip getShip() {
		return ship;
	}


}