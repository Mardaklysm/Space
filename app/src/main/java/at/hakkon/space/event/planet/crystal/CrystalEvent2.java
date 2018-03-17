package at.hakkon.space.event.planet.crystal;

import android.content.Context;

import java.util.ArrayList;
import java.util.Random;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.EFaction;
import at.hakkon.space.datamodel.inventory.BlueCrystal;
import at.hakkon.space.datamodel.inventory.IInventoryItem;
import at.hakkon.space.datamodel.inventory.RedCrystal;
import at.hakkon.space.datamodel.inventory.weapon.AbsWeapon;
import at.hakkon.space.datamodel.inventory.weapon.WeaponLaser;
import at.hakkon.space.datamodel.inventory.weapon.WeaponRocket;
import at.hakkon.space.event.AbsEvent;
import at.hakkon.space.event.EEventType;
import at.hakkon.space.utility.Utility;

/**
 * Created by Markus on 29.08.2017.
 */

public class CrystalEvent2 extends AbsEvent {

	public CrystalEvent2(int level) {
		super(level);
	}

	@Override
	public EEventType getEventType() {
		return EEventType.Question;
	}

	private int decissionPath = 0;

	@Override
	protected void executeImpl(Context context) {
		Random random = new Random();
		if (ApplicationClass.getInstance().getCrystalSeriesPosition() != 2) {
			IInventoryItem crystal = random.nextBoolean() ? new BlueCrystal(500) : new RedCrystal(500);

			String text = "You know that there is something about to come up..\n\nbut you are just not sure what it is...\n\nYou rest on the thought that time might make things clearer and bring closure.\n\n";
			text += "Loot\n -" + crystal.getName();
			getShip().addInventory(crystal);
			Utility.getInstance().showTextDialog(context, text);
			return;
		}

		if (canBeExecuted()) {
			ApplicationClass app = ApplicationClass.getInstance();
			ArrayList<CharSequence> optionsList = new ArrayList<>();

			int greenKarma = app.getFaction(EFaction.Green).getKarma();

			String text = "As you come into orbit of the planet you get approached by a huge battleship with " + app.getFaction(EFaction.Green).getSymbol() + " symbols all over it.";

			if (greenKarma < 10) {
				decissionPath = 1;
				text += "\n\n**INCOMMING TRANSMISSION**\nWe know what you did! You sided with one of them!";
				optionsList.add("I figured that to be a wise decission at that time.");
				optionsList.add("Uh.. yes that was you know..  a mistake");
			} else if (greenKarma >= 10) {
				decissionPath = 2;
				text += "\n\n**INCOMMING TRANSMISSION**\nWe know what you did! You did not hesitate to blow them up.";
				optionsList.add("I figured that to be a wise decission at that time.");
				optionsList.add("Uh.. yes that was you know..  a mistake");
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
		ApplicationClass.getInstance().updateScore(getLevel() * 100);

		Random random = new Random();


		switch (hint) {
			case 0:
				if (decissionPath == 1) {
					String text = "We agree with your decission and want to reward you with something special for your good deeds.\n\n";

					IInventoryItem crystal = random.nextBoolean() ? new RedCrystal(500) : new BlueCrystal(500);

					text += "Loot\n -";
					text += crystal.getName();

					ApplicationClass.getInstance().getShip().addInventory(crystal);

					Utility.getInstance().showTextDialog(context, text);
					ApplicationClass.getInstance().updateKarma(EFaction.Green, 10);

				} else if (decissionPath == 2) {

					String text = "We agree with your decission and want to reward you with something special for your good deeds.\n\n";

					IInventoryItem crystal = random.nextBoolean() ? new RedCrystal(500) : new BlueCrystal(500);
					AbsWeapon weapon = random.nextBoolean() ? new WeaponLaser(getLevel() + 1) : new WeaponRocket(getLevel() + 1);

					text += "Loot\n -";
					text += crystal.getName() + "\n -" + weapon.getName();

					ApplicationClass.getInstance().getShip().addInventory(crystal);
					ApplicationClass.getInstance().getShip().addInventory(weapon);

					Utility.getInstance().showTextDialog(context, text);
					ApplicationClass.getInstance().updateKarma(EFaction.Green, 10);
				}

				break;
			case 1:
				if (decissionPath == 1) {
					String text = "We hope you are right. Maybe this will point you into the right direction\n\n";

					int money = 200 + getLevel() * 100;

					text += "Loot\n -";
					text += "Cash: " + money + "$";

					ApplicationClass.getInstance().updateShipMoney(money);

					Utility.getInstance().showTextDialog(context, text);
					ApplicationClass.getInstance().updateKarma(EFaction.Green, 10);

				} else if (decissionPath == 2) {
					String text = "To bad that you don't seem to be dermined enough. Still we would like to reward you for good deeds.\n\n";

					IInventoryItem crystal = random.nextBoolean() ? new RedCrystal(500) : new BlueCrystal(500);

					text += "Loot\n -";
					text += crystal.getName();

					ApplicationClass.getInstance().getShip().addInventory(crystal);

					Utility.getInstance().showTextDialog(context, text);
					ApplicationClass.getInstance().updateKarma(EFaction.Green, 10);
				}


				break;
		}

		ApplicationClass.getInstance().incCrystalSeriesPositon();


	}
}
