package com.leezy.tianchi.util;

import java.util.Arrays;
import java.util.List;

public class ArrayUtil {
	public static int avgInterval(int[] timeArray){
		int big=timeArray[0],small=timeArray[0];
		
		for(int i = 1;i< timeArray.length;i++){
			if(timeArray[i] > big) big = timeArray[i];
			if(timeArray[i] < small) small = timeArray[i];
		}
		return (big-small)/(timeArray.length-1);
	}
	public static void main(String args[]){
		int[] a={2,6,9,23};
		System.out.println(avgInterval(a));
	}
}
