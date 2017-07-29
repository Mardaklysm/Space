package at.hakkon.space.datamodel;

/**
 * Created by Markus on 29.07.2017.
 */

public class Skill {

	private ESkill skill;

	private int level;
	private int xp;


	private Skill(){
	}

	public Skill(ESkill skill){
		this.skill = skill;

		level = 1;
		xp = 0;
	}

	public String getName(){
		return skill.name();
	}

	private void addXp(int xp){
		this.xp += xp;

		if (xp > getNextLevelUpXp()){
			levelUp();
		}
	}

	private void levelUp() {
		nextLevelUpXp+=100;
		level++;
	}

	private int nextLevelUpXp = 0;
	private int getNextLevelUpXp(){

		return nextLevelUpXp;
	}


	public String getInformationDump() {
		String retString = "";

		retString += getName() + "- Level" + level + " (" + xp + "/" + getNextLevelUpXp() +")";

		return retString;
	}
}
