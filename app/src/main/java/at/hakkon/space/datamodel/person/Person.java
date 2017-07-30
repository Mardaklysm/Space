package at.hakkon.space.datamodel.person;

import java.util.ArrayList;

/**
 * Created by Markus on 29.07.2017.
 */

public class Person {

	private final static int START_HEALTH = 100;

	private String name;
	private int health;
	private ArrayList<Skill> skills = new ArrayList<>();


	public Person(String name, int health) {
		this.name = name;
		this.health = health;
	}

	public Person(String name) {
		this.name = name;
		this.health = START_HEALTH;
	}


	public String getInformationDump() {
		String retString = "";

		retString += name + " (" + health + " HP)\n";

		retString += "Skill List (" + skills.size() + ")";
		for (Skill skill : skills) {
			retString += skill.getInformationDump();
			retString += "\n";
		}

		return retString;
	}
}
