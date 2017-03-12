package com.leezy.tianchi.antiBot.model;

public class MouseMoveEvent  implements Comparable<MouseMoveEvent> {
	private int x;//the x coordinate
	private int y;//the y coordinate
	private int time;//the time of event occurred
	public MouseMoveEvent(int x, int y, int time) {
		this.x = x;
		this.y = y;
		this.time = time;
	}
	public MouseMoveEvent(int time) {
		this.time = time;
	}
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
	@Override
	public int compareTo(MouseMoveEvent o) {
		// TODO Auto-generated method stub
		
		return this.time - o.getTime() ;
	}

}
