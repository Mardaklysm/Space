package at.hakkon.space.event.management;

import android.content.Context;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.room.AbsRoom;
import at.hakkon.space.event.AbsEvent;
import at.hakkon.space.event.EEventType;
import at.hakkon.space.utility.Utility;

/**
 * Created by Markus on 23.08.2017.
 */

public class UpgradeEvent extends AbsEvent {

	private AbsRoom room;

	public UpgradeEvent(AbsRoom room, int level) {
		super(level);
		this.room = room;
	}

	@Override
	public EEventType getEventType() {
		return EEventType.Upgrade;
	}

	@Override
	protected void executeImpl(Context context) {
		String text = room.getName() + " LVL." + room.getLevel() + " (" + room.getEfficency() + ")\n\n";

		if (ApplicationClass.getInstance().getShip().getMoney() >= room.getUpgradeCosts()) {
			text += "Upgrade this room for " + room.getUpgradeCosts() + "$ ?\n\n";
			text += room.getUpgradeInformationText();
			Utility.getInstance().showYesNoDialog(context, text, this);
		} else {
			text += "You are missing " + (room.getUpgradeCosts() - ApplicationClass.getInstance().getShip().getMoney()) + "$ to upgrade!";
			Utility.getInstance().showTextDialog(context, text);
		}

	}

	@Override
	public void callbackImpl(Context context, int hint) {

		if (hint == 1) {
			room.upgrade();
			ApplicationClass.getInstance().updateScore(room.getLevel() * 50);
		}
	}
}
