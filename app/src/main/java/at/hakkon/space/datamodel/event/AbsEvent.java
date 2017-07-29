package at.hakkon.space.datamodel.event;

import java.util.ArrayList;

/**
 * Created by Markus on 29.07.2017.
 */

public abstract class AbsEvent {

	private EEventType eventType;
	private int level;
	private int resourceBonus;

	public ArrayList<String> text = new ArrayList<>();

	public AbsEvent(int level,EEventType eventType) {
		this.level = level;
		this.eventType = eventType;
	}

	public EEventType getEventType() {
		return eventType;
	}

	public int getLevel() {
		return level;
	}


	public int getResourceBonus() {
		return resourceBonus;
	}

	public void setResourceBonus(int resourceBonus){
		this.resourceBonus = resourceBonus;
	}

	public abstract void execute();
}
