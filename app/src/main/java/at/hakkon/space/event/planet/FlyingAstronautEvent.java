package at.hakkon.space.event.planet;

import android.content.Context;

import java.util.Random;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.person.Person;
import at.hakkon.space.datamodel.room.AbsRoom;
import at.hakkon.space.datamodel.ship.PlayerShip;
import at.hakkon.space.event.AbsEvent;
import at.hakkon.space.event.EEventType;
import at.hakkon.space.utility.Utility;

/**
 * Created by Markus on 30.07.2017.
 */

public class FlyingAstronautEvent extends AbsEvent {

	private boolean canBeExecuted = true;

	private static CharSequence[] options;

	static {
		options = new CharSequence[3];

		options[0] = ("Teleport the monkey on your ship");
		options[1] = ("Shoot the monkey");
		options[2] = ("Ignore it");
	}

	public FlyingAstronautEvent(int level) {
		super(level);
	}

	@Override
	public EEventType getEventType() {
		return EEventType.Question;
	}

	@Override
	protected void executeImpl(Context context) {
		if (canBeExecuted()) {
			String text = "You can't belive your eyes as you see a space suit flying ... in space ... The passenger looks like a monkey.";
			Utility.getInstance().showQuestionsDialog(context, text, options, this);
		}
	}


	@Override
	public void callbackImpl(Context context, int hint) {
		PlayerShip ship = ApplicationClass.getInstance().getShip();

		if (hint == 0) {
			Random random = new Random();
			boolean positiveResult = random.nextBoolean();

			if (positiveResult) {

				//Upgrade lowest room
				AbsRoom lowestRoom = ship.getRooms().get(0);
				for (AbsRoom room : ship.getRooms()) {
					if (room.getLevel() < lowestRoom.getLevel()) {
						lowestRoom = room;
					}
				}

				lowestRoom.upgrade(false);

				//Add Crew member
				Person person = new Person("Hairy Mory");
				ApplicationClass.getInstance().addShipMember(person);

				String text = "As you get face to face with the survivor you notice it's not a monkey but a human engineer who hasn't shaved in a while.\nYou make sure that your new crew member gets some proper shaving equipment.\n\nYou can use his knowledge about " + lowestRoom.getName() + " to get a free upgrade!";
				Utility.getInstance().showTextDialog(context, text);
				ApplicationClass.getInstance().updateScore(getLevel() * 100);
			} else {
				int damage = 15 + 5 * getLevel();
				ApplicationClass.getInstance().updateShipHealth(-damage);
				String text = "As you open the rescue pod the monkey seems to be as dangerous as confused and immediately runs off with a shrieking scream\n\nIt takes an hour to get ride of the crazy monkey. The ship takes " + damage + " points of damage.";
				Utility.getInstance().showTextDialog(context, text);
				ApplicationClass.getInstance().updateScore(getLevel() * 50);
			}
		} else if (hint == 1) {
			String text = "You fire your weapons at the helpless astronaut and turn him into dust.\nAs you move along you hope that noone has seen what you did.";
			Utility.getInstance().showTextDialog(context, text);
			ApplicationClass.getInstance().updateScore(getLevel() * 25);
		} else if (hint == 2) {
			String text = "As you pass by the helpless astronaut you are not so sure anymore if this is a monkey after all.";
			Utility.getInstance().showTextDialog(context, text);
			ApplicationClass.getInstance().updateScore(getLevel() * 10);
		}
	}
}
