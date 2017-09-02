package at.hakkon.space.event.battle;

import android.content.Context;

import java.util.ArrayList;

import at.hakkon.space.datamodel.inventory.BlueCrystal;
import at.hakkon.space.datamodel.inventory.EGenricLootType;
import at.hakkon.space.datamodel.inventory.GenericLoot;
import at.hakkon.space.datamodel.inventory.RedCrystal;
import at.hakkon.space.datamodel.room.AbsRoom;
import at.hakkon.space.event.AbsEvent;
import at.hakkon.space.event.EEventType;
import at.hakkon.space.utility.Utility;

/**
 * Created by Markus on 10.08.2017.
 */

public class UseCrystalEvent extends AbsEvent {

	private int exitHint;
	private AbsRoom room;


	private RedCrystal redCrystal = null;
	private BlueCrystal blueCrystal = null;


	public UseCrystalEvent(AbsRoom room, int level) {
		super(level);
		this.room = room;
		setCanBeOverwritten(false);
	}

	@Override
	public EEventType getEventType() {
		return EEventType.UseItem;
	}

	@Override
	protected void executeImpl(Context context) {


		ArrayList<CharSequence> choices = new ArrayList<>();


		for (GenericLoot item : getShip().getGenericLoot()) {
			if (item.getType() == EGenricLootType.Crystal_Blue) {
				blueCrystal = (BlueCrystal) item;
			}
			if (item.getType() == EGenricLootType.Crystal_Red) {
				redCrystal = (RedCrystal) item;
			}
		}

		String text = room.getName() + " - Use Items";

		if (redCrystal != null) {
			choices.add(redCrystal.getName() + " (" + redCrystal.getDescription() + ")");
		}

		if (blueCrystal != null) {
			choices.add(blueCrystal.getName() + " (" + blueCrystal.getDescription() + ")");
		}

		choices.add("Close Inventory");
		exitHint = choices.size() - 1;

		CharSequence[] charSequences = new CharSequence[choices.size()];
		for (int i = 0; i < choices.size(); i++) {
			charSequences[i] = choices.get(i);
		}

		Utility.getInstance().showQuestionsDialog(context, text, charSequences, this);
	}

	@Override
	public void callbackImpl(Context context, int hint) {
		//modify hit
		int modHint = hint;
		if (redCrystal == null && blueCrystal == null){
			return;
		}

		if (redCrystal!= null){
			if (hint == 0){
				getShip().useRedCrystal(room, redCrystal);
			}
		}else if (blueCrystal!= null){
			if ((redCrystal != null && hint == 1) || (redCrystal == null && hint == 0)){
				getShip().useBlueCrystal(room, blueCrystal);
			}
		}

	}
}
