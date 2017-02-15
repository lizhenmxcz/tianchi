package com.leezy.tianchi.antiBot;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;
import com.leezy.tianchi.util.ArrayUtil;

public class Features {
	public static class FeatureMapper extends MapperBase {
		private Record key;
	    private Record value;
	    
		@Override
		public void map(long recordNum, Record record, TaskContext context) throws IOException {
			// TODO Auto-generated method stub
			//elecment feature
			long elementNum = 0; //the number of the element that visited
			long elementAvgWaitTime = 0;// the stay time for visit a element
			//button click feature
			long allButtonNum = 0;
			long rightButtonNum = 0; // the count of click the right button
			long leftButtonNum = 0;// the count of click the left button
			long avgButtonInterval = 0;// the average interval time between button click 
			Record result = context.createOutputRecord();
			result.setBigint("id", record.getBigint("id"));
			result.setString("label", record.getString("label"));
			/**
			 * extract the element feature
			 */
			String elementInfo = record.getString("a1");
			int elementWaitTime = 0;
			//System.out.println("---------------" + elementInfo);
			JSONArray elementArray = JSON.parseArray(elementInfo);
			for(int i=0,len=elementArray.size();i<len;i++){
	            JSONObject temp=  elementArray.getJSONObject(i);
	            if(temp.getInteger("type") == 0){//lost focus add the time
	            	elementWaitTime +=temp.getInteger("time");
	            }
	            if(temp.getInteger("type") == 1){// on focus increase the element numbet and minus the time 
	            	elementNum ++;
	            	elementWaitTime -= temp.getInteger("time");
	            }
	        }
			result.setBigint("elementNum", elementNum);
			if(elementNum != 0){
				elementAvgWaitTime = elementWaitTime/elementNum;
			}
			result.setBigint("elementAvgWaitTime", elementAvgWaitTime);
			/**
			 * extract the button click feature
			 */
			String buttonInfo = record.getString("a3");
			
			JSONArray buttonArray = JSON.parseArray(buttonInfo);
			allButtonNum = buttonArray.size();
			int[] timeArray = new int[(int)allButtonNum];// the button time array
			for(int i = 0;i < allButtonNum;i++){
				JSONObject temp=  buttonArray.getJSONObject(i);
				timeArray[i] = temp.getInteger("time");				
				if(temp.getString("button").equals("left")){
					leftButtonNum++;
				}
				else {
					rightButtonNum++;
				}
			}
			avgButtonInterval = ArrayUtil.avgInterval(timeArray);
			result.setBigint("leftButtonNum", leftButtonNum);
			result.setBigint("rightButtonNum", rightButtonNum);
			result.setBigint("allButtonNum", allButtonNum);
			result.setBigint("avgButtonInterval",avgButtonInterval);
	        context.write(result);
		}	
	}
	public static void main(String[] args) throws Exception {
        if (args.length != 2 && args.length != 3) {
          System.err.println("Usage: OnlyMapper <in_table> <out_table> [setup|map|cleanup]");
          System.exit(2);
        }

        JobConf job = new JobConf();
        job.setMapperClass(FeatureMapper.class);
        job.setNumReduceTasks(0);

        InputUtils.addTable(TableInfo.builder().tableName(args[0]).build(), job);
        OutputUtils.addTable(TableInfo.builder().tableName(args[1]).build(), job);

        

        JobClient.runJob(job);

	}
}