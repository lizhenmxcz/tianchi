package com.leezy.tianchi.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.leezy.tianchi.antiBot.model.MouseMoveEvent;

public class MouseEventUtil {
	private List<MouseMoveEvent> mouseEventList;
	final Pattern p = Pattern.compile("(\\{([^\\}]+)\\})"); 
	
	public MouseEventUtil(List<MouseMoveEvent> mouseEventList) {
		Collections.sort(mouseEventList);
		this.mouseEventList = mouseEventList;
	}
	public MouseEventUtil(){
		//this.mouseEventList = parseMouseMoveEvent(str);
	}
             
	  
	public int speedVariance(){
		return variance(getSpeedList(mouseEventList));
	}
	public void parseMouseMoveEvent(String str){
		List<MouseMoveEvent> list = new ArrayList<MouseMoveEvent>();
		List<String> listSlider=new ArrayList<String>();  
        Matcher mslider = p.matcher(str);  
	    while(mslider.find()){  
	    	listSlider.add(mslider.group().substring(0, mslider.group().length()));  
	    } 
		for(int j = 0;j< listSlider.size();j++){
			JsonObject temp=  new JsonParser().parse(listSlider.get(j)).getAsJsonObject();
			int x = temp.get("x").getAsInt();
			int y = temp.get("y").getAsInt();
			int time = temp.get("time").getAsInt();
			list.add(new MouseMoveEvent(x,y,time));
		}
		Collections.sort(list);
		this.mouseEventList = list;  
	}
	public void parseSliderEvent(String str){
		List<MouseMoveEvent> list = new ArrayList<MouseMoveEvent>();
		List<String> listSlider=new ArrayList<String>();  
        Matcher mslider = p.matcher(str);  
	    while(mslider.find()){  
	    	listSlider.add(mslider.group().substring(0, mslider.group().length()));  
	    } 
		for(int j = 0;j< listSlider.size();j++){
			JsonObject temp=  new JsonParser().parse(listSlider.get(j)).getAsJsonObject();
			list.add(new MouseMoveEvent(temp.get("x").getAsInt(),
					temp.get("y").getAsInt(),temp.get("t").getAsInt()));
		}
		Collections.sort(list);
		this.mouseEventList= list;
	}
	public int accelerationVariance(){
		List<Integer> speedList = getSpeedList(mouseEventList);
		//System.out.println(speedList.toString());
		List<Integer> timeList = getTimeList(this.mouseEventList);
		List<Integer> accelerationList = new ArrayList<Integer>();
		for(int i =0,j= 1;i< speedList.size()-1&&j<timeList.size()-1;i++,j++){
			int speedInterval = Math.abs(speedList.get(i+1)-speedList.get(i));
			int timeInterval = Math.abs(timeList.get(i+1)-timeList.get(i));
			if(timeInterval == 0) accelerationList.add(0);
			else accelerationList.add(speedInterval/timeInterval);
		}
		return variance(accelerationList);
	}
	public int getTotalTime(){
		int length = mouseEventList.size();
		return mouseEventList.get(length-1).getTime()-mouseEventList.get(0).getTime();
	}
	public int getTotalDistance(){
		List<Integer> distanceList = getDistanceList(mouseEventList);
		int totalD = 0;
		for(int distance:distanceList){
			totalD+=distance;
		}
		return totalD;
	}
	public List<Integer> getDistanceList(List<MouseMoveEvent> list){
		List<Integer> distanceList = new ArrayList<Integer>();
		for(int i = 0; i< list.size()-1; i++){
			MouseMoveEvent m1 = list.get(i);
 			MouseMoveEvent m2 = list.get(i+1);
			int distanceInterval = (int) Math.sqrt(Math.pow(m2.getX()-m1.getX(), 2)+
					Math.pow(m2.getY()-m1.getY(), 2));
			distanceList.add(distanceInterval);
		}
		return distanceList;
	}
	public  List<Integer> getSpeedList(List<MouseMoveEvent> list){		
		List<Integer> speedList = new ArrayList<Integer>();
 		for(int i = 0; i< list.size()-1; i++){
 			MouseMoveEvent m1 = list.get(i);
 			MouseMoveEvent m2 = list.get(i+1);
			int timeInterval = Math.abs(m2.getTime()-m1.getTime());
			double distanceInterval = (int) Math.sqrt(Math.pow(m2.getX()-m1.getX(), 2)+
					Math.pow(m2.getY()-m1.getY(), 2));
			if(timeInterval == 0) speedList.add(0);
			else speedList.add((int)(1000*distanceInterval/timeInterval));
		}
 		return speedList;
	}
	public  List<Integer> getTimeList(List<MouseMoveEvent> list){
		List<Integer> timeList = new ArrayList<Integer>();
		for(int i = 0; i< list.size(); i++){
			timeList.add(list.get(i).getTime());
		}
		return timeList;
	}
	public  int getAvgSpeed(){
		List<Integer> speedList = getSpeedList(mouseEventList);
		int totalSpeed = 0;
		int speedSize = 0;
		for(int speed:speedList){
			totalSpeed += speed;
			speedSize += 1;
		}
		return speedSize == 0?0:totalSpeed/speedSize;
	}
	public int sliderYVariance(){
		List<Integer> yList = new ArrayList<Integer>();
		for(int i = 0; i< mouseEventList.size(); i++){
			yList.add(mouseEventList.get(i).getY());
		}
		return variance(yList);
	}
	public int variance(List<Integer> list) {   
	        int m = list.size();  
	        double sum=0;  
	        for(int i=0;i<m;i++){//求和  
	            sum += list.get(i);  
	        }  
	        double dAve=sum/m;//求平均值  
	        int dVar=0;  
	        for(int i=0;i<m;i++){//求方差  
	            dVar+=(list.get(i)-dAve)*(list.get(i)-dAve);  
	        }  
	        return m==0?0:dVar/m;  
	}  
    public static void main(String[] args){
    	MouseMoveEvent m1 = new MouseMoveEvent(1,2,3);
    	MouseMoveEvent m2 = new MouseMoveEvent(2,3,4);
    	MouseMoveEvent m3 = new MouseMoveEvent(4,2,6);
    	MouseMoveEvent m4 = new MouseMoveEvent(5,6,8);
    	MouseMoveEvent m5 = new MouseMoveEvent(6,6,9);
    	List<MouseMoveEvent> list = new ArrayList<MouseMoveEvent>();
    	list.add(m1);
    	list.add(m2);
    	list.add(m3);
    	list.add(m4);
    	list.add(m5);
    	MouseEventUtil u = new MouseEventUtil(list);
    	//System.out.println(avgSpeed(t1List,x1List));
    	System.out.println(u.getTotalTime());
    	System.out.println(u.getTotalDistance());
    	System.out.println(u.sliderYVariance());
    	System.out.println(u.accelerationVariance());
    	System.out.println(u.speedVariance());
    }
}