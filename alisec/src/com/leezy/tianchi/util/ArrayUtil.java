package com.leezy.tianchi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArrayUtil {
	public static int avgInterval(int[] timeArray){
		int big=timeArray[0],small=timeArray[0];
		
		for(int i = 1;i< timeArray.length;i++){
			if(timeArray[i] > big) big = timeArray[i];
			if(timeArray[i] < small) small = timeArray[i];
		}
		return timeArray.length==1?0:(big-small)/(timeArray.length-1);
	}
	public static void main(String args[]){
		//int[] a={2,6,9,23};
		//System.out.println(avgInterval(a));
		String jaStr = "[{\"time\":9402,\"type\":1,\"target\":\"****\"},{\"time\":9386,\"type\":0,\"target\":\"\"},{\"time\":3268,\"type\":1,\"target\":\"\"},{\"time\":717,\"type\":0,\"target\":\"\"},{\"time\":717,\"type\":1,\"target\":\"****\"},{\"time\":3268,\"type\":0,\"target\":\"****\"}]";  
		String ss= "[{\"time\":5810,\"type\":1,\"target\":\"****\"},{\"time\":1100,\"type\":1,\"target\":\"****\"},{\"time\":1090,\"type\":0,\"target\":\"\"},{\"time\":5796,\"type\":0,\"target\":\"****\"}]";
		//String tt = "[{"time":7149,"type":0,"target":"****"},{"time":5718,"type":1,"target":"****"},{"time":7114,"type":0,"target":"****"},{"time":7121,"type":1,"target":"****"},{"time":7153,"type":1,"target":"****"},{"time":13593,"type":0,"target":"****"}]";
        //ss.replace("time", "timet");
       //System.out.println(ss.replaceAll("\"", "\"\""));
		List<String> list=new ArrayList<String>();  
       //Pattern p = Pattern.compile("(\\{{^\\}}\\})"); 
        Pattern p = Pattern.compile("(\\{([^\\}]+)\\})");  

        Matcher m = p.matcher(ss);  
        while(m.find()){  
            list.add(m.group().substring(0, m.group().length()));  
            System.out.println(m.group().substring(0, m.group().length()));
        }  
        //return list; 
		
//		JsonParser parser = new JsonParser();
//		    //��JSON��String ת��һ��JsonArray����
//		JsonArray jsonArray = parser.parse(ss).getAsJsonArray();
//		for(JsonElement je:jsonArray){
//			JsonObject jo = je.getAsJsonObject();
//			String s = jo.get("time").getAsString();
//			System.out.println(s);
//		}
	}
}
