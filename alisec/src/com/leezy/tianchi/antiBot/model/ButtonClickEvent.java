package com.leezy.tianchi.antiBot.model;

public class ButtonClickEvent implements Comparable<ButtonClickEvent>{
	private int x;//the x coordinate
	private int y;//the y coordinate
	private int time;//the time of event occurred
	private String type;//left or right
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public int compareTo(ButtonClickEvent o) {
		// TODO Auto-generated method stub
		return this.time-o.getTime();
	}
	public ButtonClickEvent(int x, int y, int time, String type) {
		this.x = x;
		this.y = y;
		this.time = time;
		this.type = type;
	}
	

}
