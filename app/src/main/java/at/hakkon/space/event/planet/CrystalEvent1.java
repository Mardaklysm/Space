package at.hakkon.space.event.planet;

import android.content.Context;

import java.util.ArrayList;
import java.util.Random;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.EFaction;
import at.hakkon.space.datamodel.inventory.BlueCrystal;
import at.hakkon.space.datamodel.inventory.IInventoryItem;
import at.hakkon.space.datamodel.inventory.RedCrystal;
import at.hakkon.space.datamodel.inventory.weapon.AbsWeapon;
import at.hakkon.space.datamodel.inventory.weapon.EWeaponType;
import at.hakkon.space.event.AbsEvent;
import at.hakkon.space.event.EEventType;
import at.hakkon.space.utility.Utility;

/**
 * Created by Markus on 29.08.2017.
 */

public class CrystalEvent1 extends AbsEvent {

	public CrystalEvent1(int level) {
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

			ArrayList<CharSequence> optionsList = new ArrayList<>();

			String text = "As you get closer to the planet you notice two battleships, one is shaped like a red bird and the other one like a blue fish.\n\nYou get two incoming messages. Both ship captains ask you to help them destroy the other one.";

			optionsList.add("Help the red ship");
			optionsList.add("Help the blue ship");
			optionsList.add("Ignore the requests and move on");

			if (getShip().hasWeapon(EWeaponType.Nuke)) {
				optionsList.add("Press the big red button and drop a Nuke to solve the conflict.");
			}


			CharSequence[] options = new CharSequence[optionsList.size()];

			for (int i = 0; i < optionsList.size(); i++) {
				options[i] = optionsList.get(i);
			}

			Utility.getInstance().showQuestionsDialog(context, text, options, this);
		}
	}

	@Override
	public void callbackImpl(Context context, int hint) {


		Random random = new Random();


		switch (hint) {
			case 0:
				ApplicationClass.getInstance().updateScore(getLevel() * 50);
				String text = "";

				IInventoryItem item = new RedCrystal(500);
				getShip().addInventory(item);

				text = "As the blue ship explodes you get a communication message.\n\n\"Thank you for your support, take this. I'm sure we will meet again.\"\n\nLoot\n -" + item.getName() + " (" + item.getDescription() + ")";
				Utility.getInstance().showTextDialog(context, text);
				ApplicationClass.getInstance().updateKarma(EFaction.Red, 10);
				ApplicationClass.getInstance().updateKarma(EFaction.Blue, -10);
				break;
			case 1:
				ApplicationClass.getInstance().updateScore(getLevel() * 50);

				item = new BlueCrystal(500);
				getShip().addInventory(item);

				text = "As the red ship explodes you get a communication message.\n\n\"Thank you for your support, take this. I'm sure we will meet again.\"\n\nLoot\n -" + item.getName() + " (" + item.getDescription() + ")";
				Utility.getInstance().showTextDialog(context, text);

				ApplicationClass.getInstance().updateKarma(EFaction.Blue, 10);
				ApplicationClass.getInstance().updateKarma(EFaction.Red, -10);

				break;
			case 2:
				ApplicationClass.getInstance().updateScore(getLevel() * 50);

				text = "As you get farther away from the battle scene you notice that the two ships managed to destroy each other.\n\nYou are not sure if this was the wisest decission";

				Utility.getInstance().showTextDialog(context, text);
				break;
			case 3:
				ApplicationClass.getInstance().updateScore(getLevel() * 50);

				for (AbsWeapon weapon : getShip().getWeapons()) {
					if (weapon.getWeaponType() == EWeaponType.Nuke) {
						getShip().removeInventory(weapon);
						break;
					}
				}

				ApplicationClass.getInstance().updateKarma(EFaction.Blue, -10);
				ApplicationClass.getInstance().updateKarma(EFaction.Red, -10);
				ApplicationClass.getInstance().updateKarma(EFaction.Green, 10);

				IInventoryItem crystal1 = new BlueCrystal(500);
				IInventoryItem crystal2 = new RedCrystal(500);

				getShip().addInventory(crystal1);
				getShip().addInventory(crystal2);

				text = "The obviously surprised ships both blow up in the blast of the Nuke.\n\nYou discover some interresting looking Crystals in the debrise.\n\nLoot\n -" + crystal1.getName() + " (" + crystal1.getDescription() + ")\n -" + crystal2.getName() + " (" + crystal2.getDescription() + ")";


				Utility.getInstance().showTextDialog(context, text);
				break;
		}


	}
}
