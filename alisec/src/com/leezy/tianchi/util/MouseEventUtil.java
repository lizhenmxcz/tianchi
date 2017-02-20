package com.leezy.tianchi.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class MouseEventUtil {
	
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