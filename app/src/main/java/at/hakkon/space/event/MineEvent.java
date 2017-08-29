package at.hakkon.space.event;

import android.content.Context;

import java.util.ArrayList;
import java.util.Random;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.inventory.GenericLoot;
import at.hakkon.space.datamodel.inventory.IInventoryItem;
import at.hakkon.space.datamodel.inventory.Weapon;
import at.hakkon.space.utility.Utility;

/**
 * Created by Markus on 29.08.2017.
 */

public class MineEvent extends AbsEvent {

	public MineEvent(int level) {
		super(level);
	}

	@Override
	public EEventType getEventType() {
		return EEventType.Question;
	}

	private int payedMinerals;

	boolean[] hasOption = new boolean[3];

	@Override
	protected void executeImpl(Context context) {
		if (canBeExecuted()) {
			hasOption[0] = true;

			payedMinerals = 200 + getLevel() * 50;


			int optionsSize = 1;

			if (getShip().getMoney() >= payedMinerals) {
				optionsSize++;
				hasOption[1] = true;
			}

			GenericLoot statue = statue();

			if (statue != null) {
				optionsSize++;
				hasOption[2] = true;
			}


			ArrayList<CharSequence> optionsList = new ArrayList<>();

			String text = "As you get closer to the surface you can see a bunch of human sized creatures working a mine that goes deep under the surface.\n\nOne of the bigger furry creatures gets close to you and stares at you with his big six black eyes silently, seemingly awaiting orders.";

			if (hasOption[0]) {
				optionsList.add("Slowly walk backwards... Enter your space craft and try to get away before you can end up as dinner");
			}
			if (hasOption[1]) {
				optionsList.add("Drop " + payedMinerals + " in front of the creature and stare back without moving a muscle.");
			}
			if (hasOption[2]) {
				optionsList.add("Pull out your " + statue.getName() + " name and offer it to the creature");
			}

			CharSequence[] options = new CharSequence[optionsList.size()];

			for (int i = 0; i < optionsList.size(); i++) {
				options[i] = optionsList.get(i);
			}

			Utility.getInstance().showQuestionsDialog(context, text, options, this);
		}
	}

	private GenericLoot statue() {
		GenericLoot statue = null;

		for (IInventoryItem item : getShip().getInventory()) {
			if (item instanceof GenericLoot) {
				GenericLoot loot = (GenericLoot) item;
				if (loot.getType() == EGenricLootType.Statue) {
					if (statue == null || loot.getCashValue() < statue.getCashValue()) {
						statue = loot;
					}
				}
			}
		}

		return statue;
	}

	@Override
	public void callbackImpl(Context context, int hint) {


		Random random = new Random();

		int modHint = hint;

		if (hint == 1 && !hasOption[1]) {
			modHint = 2;
		}


		switch (modHint) {
			case 0:
				ApplicationClass.getInstance().updateScore(getLevel() * 50);
				String text = "";
				if (random.nextBoolean()) {
					int damage = 5 * getLevel();
					int minerals = 25 * getLevel();
					text = "As you get back to the ship you seem to have enraged the local population which starts throwing rocks at the ship with their tiny paws as you take off.\n\nIt looks like some of the rocks contain rare minerals.\n\nYou take " + damage + " points of damage and gained " + minerals + "$ from the minerals.";
					ApplicationClass.getInstance().updateShipHealth(-damage);
					ApplicationClass.getInstance().updateShipMoney(minerals);
				} else {
					text = "The creatures quickly loose interrest into you and start to descend into the cave again.\n\nAs you take off you wonder what would have been inside...";
				}
				Utility.getInstance().showTextDialog(context, text);
				break;
			case 1:
				ApplicationClass.getInstance().updateScore(getLevel() * 100);
				ApplicationClass.getInstance().updateShipMoney(-payedMinerals);

				if (random.nextBoolean()) {
					text = "A lot more creatures start showing up and start carrying away your minerals away in their mouth. One by one he pick them up and then dissapear into seemingly random directions.\n\nIt took you some time but now you are sure you are getting robbed here.\n\nYou can still recover " + payedMinerals / 2 + "$";
					ApplicationClass.getInstance().updateShipMoney(payedMinerals / 2);
				} else {
					text = "A few more creatures come to pick up your donation and then descend into the cave again.\n\nAbout half an hour later the bigger creatures comes back from the cave and hands you something.";

					Weapon weapon = Weapon.getNuke((int) (1 + Math.floor(getLevel() / 2)));
					text += "\n\nA Weapon of Mass Destruction:\n " + weapon.getName();
					ApplicationClass.getInstance().getShip().addInventory(weapon);
				}
				Utility.getInstance().showTextDialog(context, text);
				break;
			case 2:
				ApplicationClass.getInstance().updateScore(getLevel() * 200);
				int healthIncrease = 25 + getLevel() * 5;
				ApplicationClass.getInstance().getShip().removeInventory(statue());

				text = "A few more creatures come to pick up the golden statue chanting a repeating song and then descending into the cave again.\n\nAbout half an hour later they come back from the cave and hand you something.";

				if (random.nextBoolean()) {
					Weapon weapon = Weapon.getNuke((int) (2 + Math.floor(getLevel() / 2)));
					text += "\n\nA Weapon of Mass Destruction:\n" + weapon.getName();
					ApplicationClass.getInstance().getShip().addInventory(weapon);
				} else {
					text += "\n\nA bunch of rare biological crystals which get absorbed by your ship and improve its hull by " + healthIncrease + " points.";
					ApplicationClass.getInstance().getShip().updateMaxHealth(healthIncrease);
				}

				Utility.getInstance().showTextDialog(context, text);
				break;
		}

	}
}
