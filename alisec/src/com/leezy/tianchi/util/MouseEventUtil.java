package com.leezy.tianchi.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.leezy.tianchi.antiBot.model.MouseEvent;

public class MouseEventUtil {
	private List<MouseEvent> mouthList;
	
	
	public MouseEventUtil(List<MouseEvent> mouthList) {
		Collections.sort(mouthList);
		this.mouthList = mouthList;
	}
	public int speedVariance(){
		return (int) variance(getSpeedList(mouthList));
	}
	public int accelerationVariance(){
		List<Integer> speedList = getSpeedList(mouthList);
		List<Integer> timeList = getTimeList(this.mouthList);
		List<Integer> accelerationList = new ArrayList<Integer>();
		for(int i =0,j= 1;i< speedList.size()-1&&j<timeList.size()-1;i++,j++){
			int speedInterval = Math.abs(speedList.get(i+1)-speedList.get(i));
			int timeInterval = Math.abs(timeList.get(i+1)-timeList.get(i));
			accelerationList.add(speedInterval/timeInterval);
		}
		return (int) variance(accelerationList);
	}
	public  List<Integer> getSpeedList(List<MouseEvent> list){		
		List<Integer> speedList = new ArrayList<Integer>();
 		for(int i = 0; i< list.size()-1; i++){
 			MouseEvent m1 = list.get(i);
 			MouseEvent m2 = list.get(i+1);
			int timeInterval = Math.abs(m2.getTime()-m1.getTime());
			int distanceInterval = (int) Math.sqrt(Math.pow(m2.getX()-m1.getX(), 2)+
					Math.pow(m2.getY()-m1.getY(), 2));
			speedList.add(distanceInterval/timeInterval);
		}
 		return speedList;
	}
	public  List<Integer> getTimeList(List<MouseEvent> list){
		List<Integer> timeList = new ArrayList<Integer>();
		for(int i = 0; i< list.size(); i++){
			timeList.add(list.get(i).getTime());
		}
		return timeList;
	}
	public static int tryTimes(List<Integer> xList){
		int times = 1;
		for(int i = 1;i< xList.size();i++){
			if(xList.get(i) < xList.get(i-1)) 
				times ++;
		}
		return times;
	}
	public static int isConstantSpeed(List<Integer> tList,List<Integer> xList){
		for(int i = 1;i< xList.size()-1;i++){
			int distance1 = Math.abs(xList.get(i)-xList.get(i-1));
			int distance2 = Math.abs(xList.get(i+1)-xList.get(i));
			int time1 = tList.get(i)-tList.get(i-1);
			int time2 = tList.get(i+1)-tList.get(i);
			if(distance1/time1 != distance2/time2) 
				return 0;
		}
		return 1;
	}
	public static int avgSpeed(List<Integer> tList,List<Integer> xList){
		int distance = 0;
		for(int i = 1;i< xList.size();i++){
			distance += Math.abs(xList.get(i)-xList.get(i-1));
		}
		int timeInterval = tList.get(tList.size()-1)-tList.get(0);
		return distance/timeInterval;
	}
	public static int isYSteady(List<Integer> yList){
		HashSet<Integer> hs = new HashSet<Integer>(yList);
		return hs.size()>1?0:1;
	}
	public static int totalDistance(List<Integer> xList){
		int distance = 0;
		for(int i = 1;i< xList.size();i++){
			distance += Math.abs(xList.get(i)-xList.get(i-1));
		}
		return distance;
	}
	public static int totalTime(List<Integer> tList){
		return tList.get(tList.size()-1)-tList.get(0);
	}
	public static double variance(List<Integer> yList) {   
	        int m=yList.size();  
	        double sum=0;  
	        for(int i=0;i<m;i++){//求和  
	            sum+=(double)yList.get(i);  
	        }  
	        double dAve=sum/m;//求平均值  
	        double dVar=0;  
	        for(int i=0;i<m;i++){//求方差  
	            dVar+=(yList.get(i)-dAve)*(yList.get(i)-dAve);  
	        }  
	        return dVar/m;  
	}  
    public static void main(String[] args){
    	List<Integer> tList=Arrays.asList(0,12,34,45,56,78);
    	List<Integer> t1List=Arrays.asList(0,1,3,5,6,8);
    	List<Integer> x1List=Arrays.asList(0,2,6,10,12,16);
    	List<Integer> y1List=Arrays.asList(2,2,2,2,2);
    	List<Integer> xList=Arrays.asList(1208,1322,1300,1450,1565,1789);
    	List<Integer> yList=Arrays.asList(100,112,134,145,156,178);
    	//System.out.println(avgSpeed(t1List,x1List));
    	System.out.println(totalTime(t1List));
    }
}