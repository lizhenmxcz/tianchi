package com.leezy.tianchi;

import java.io.IOException;
import java.util.Iterator;

import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.ReducerBase;
import com.aliyun.odps.mapred.TaskContext;
import com.alibaba.fastjson.*;

public class Features {
	public static class FeatureMapper extends MapperBase {
		private Record key;
	    private Record value;

		@Override
		public void map(long recordNum, Record record, TaskContext context) throws IOException {
			// TODO Auto-generated method stub
			
			key.setString("id", record.getString("id"));
	        value.setString("a1", record.getString("a1"));
	        value.setString("a2", record.getString("a2"));
	        value.setString("a3", record.getString("a3"));
	        value.setString("a4", record.getString("a4"));
	        value.setString("a5", record.getString("a5"));
	        value.setString("a6", record.getString("a6"));
	        value.setString("a7", record.getString("a7"));
	        value.setString("label", record.getString("label"));
	        context.write(key, value);
		}

		@Override
		public void setup(TaskContext context) throws IOException {
			// TODO Auto-generated method stub
			key = context.createMapOutputKeyRecord();
	        value = context.createMapOutputValueRecord();
			
		}
	
	}
	public static class FeatureReducer extends ReducerBase {
		
        private Record result;
		@Override
		public void reduce(Record key, Iterator<Record> values, TaskContext context)
				throws IOException {
			while(values.hasNext()){
				int elementNum = 0; //the number of the element that visited
				int elementWaitTime = 0;// the time for visit a element
				Record value = values.next();
				String elementInfo = value.getString("a1");
				JSONArray elementArray = JSON.parseArray(elementInfo);
				for(int i=0,len=elementArray.size();i<len;i++){
		            JSONObject temp=  elementArray.getJSONObject(i);
		            if(temp.getInteger("type") == 0){//lost focus add the time
		            	elementWaitTime +=temp.getInteger("time");
		            }
		            else {// on focus increase the element numbet and minus the time 
		            	elementNum ++;
		            	elementWaitTime -= temp.getInteger("time");
		            }
		        }
				
			}
			
		}

		@Override
		public void setup(TaskContext context) throws IOException {
			result = context.createOutputRecord();
		}
		
	}
}
