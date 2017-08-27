package at.hakkon.space.event;

import android.content.Context;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.inventory.IInventoryItem;
import at.hakkon.space.utility.Utility;

/**
 * Created by Markus on 10.08.2017.
 */

public class SellEvent extends AbsEvent {

	private IInventoryItem item;

	public SellEvent(IInventoryItem item, int level) {
		super(level);
		this.item = item;
	}

	@Override
	public EEventType getEventType() {
		return EEventType.Question;
	}

	@Override
	protected void executeImpl(Context context) {
		String text = "Sell: " + item.getName() + " (" + item.getCashValue() + "$)";

		Utility.getInstance().showYesNoDialog(context, text, this);
	}

	@Override
	public void callbackImpl(Context context, int hint) {
		if (hint == 1){
			ApplicationClass.getInstance().sellItem(item);
			ApplicationClass.getInstance().updateScore(item.getCashValue()/2);
		}

	}
}
