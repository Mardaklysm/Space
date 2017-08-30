package at.hakkon.space.event.planet;

import android.content.Context;

import java.util.Random;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.inventory.GenericLoot;
import at.hakkon.space.datamodel.inventory.Weapon;
import at.hakkon.space.datamodel.ship.PlayerShip;
import at.hakkon.space.event.AbsEvent;
import at.hakkon.space.event.EEventType;
import at.hakkon.space.datamodel.inventory.EGenricLootType;
import at.hakkon.space.utility.Utility;

/**
 * Created by Markus on 30.07.2017.
 */

public class UnknownMetalBoxEvent extends AbsEvent {

	private static CharSequence[] options;

	static {
		options = new CharSequence[3];

		options[0] = ("Teleport it onto the ship");
		options[1] = ("Shot at it with your weapons");
		options[2] = ("Ignore it");
	}


	public UnknownMetalBoxEvent(int level) {
		super(level);
	}

	@Override
	public EEventType getEventType() {
		return EEventType.Question;
	}

	@Override
	protected void executeImpl(Context context) {
		if (canBeExecuted()) {
			String text = "We have found an unknown metal box floating in space, what do you want to do?";
			Utility.getInstance().showQuestionsDialog(context, text, options, this);
		}
	}

	@Override
	public void callbackImpl(Context context, int hint) {
		PlayerShip ship = ApplicationClass.getInstance().getShip();
		if (hint == 0) {
			Random random = new Random();

			if (random.nextBoolean()) {
				int money = 75 * getLevel();
				int health = 20 + 10 * getLevel();
				String text = "As you try to carefully open the box with a sledgehammer it explodes damaging your ship for " + health + " points of damage.\n\nAt the bright side you could salvage the metallic remains for " + money + "€";

				Utility.getInstance().showTextDialog(context, text);

				ApplicationClass.getInstance().updateShipMoney(money);
				ApplicationClass.getInstance().updateShipHealth(-health);
				ApplicationClass.getInstance().updateScore(getLevel() * 50);
			} else {
				if (random.nextBoolean()) {
					//Found a weapon
					int level = Math.max(1, (int) Math.ceil(getLevel() / 2));
					Weapon weapon = random.nextBoolean() ? Weapon.getLaser(level) : Weapon.getRocket(level);
					String text = "You found a weapon in the box:\n" + weapon.getName();
					Utility.getInstance().showTextDialog(context, text);

					ApplicationClass.getInstance().getShip().addInventory(weapon);
					ApplicationClass.getInstance().updateScore(getLevel() * 150);
				} else {
					int money = 300 + 50 * getLevel();
					int size = 20 + 5 * getLevel();
					String text = "As you try to carefully open the box you can uncover a rare piece of art:\n\nA shiny, glowing statue!";

					Utility.getInstance().showTextDialog(context, text);

					GenericLoot item = new GenericLoot("Shiny, glowing statue (" + size + "cm)", money, "A majestic piece of art: A shiny, glowing statue", EGenricLootType.Statue);

					ApplicationClass.getInstance().getShip().addInventory(item);

					ApplicationClass.getInstance().updateScore(getLevel() * 150);
				}

			}

		} else if (hint == 1) {
			int money = 25 * getLevel();
			ship.updateMoney(money);
			String text = "As you shoot at it a huge explosion occurs... It was a bomb\n You could salvage the remains of the box for " + money + "€";

			ApplicationClass.getInstance().updateShipMoney(money);
			;
			Utility.getInstance().showTextDialog(context, text);
			ApplicationClass.getInstance().updateScore(getLevel() * 50);
		} else if (hint == 2) {
			String text = "As you pass the box it explodes but you take no damage. Looks like it was a bomb after all.";
			Utility.getInstance().showTextDialog(context, text);
			ApplicationClass.getInstance().updateScore(getLevel() * 25);
		}
	}
}
