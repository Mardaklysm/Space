package at.hakkon.space.activity;

/**
 * Created by Markus on 01.09.2017.
 */

public class MessageData {

	private int position;
	private int color;
	private String text;

	public MessageData(int position, int color, String text){
		this.position = position;
		this.color = color;
		this.text = text;
	}

	public int getPosition() {
		return position;
	}

	public int getColor() {
		return color;
	}

	public String getText() {
		return text;
	}
}
