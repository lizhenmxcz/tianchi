package com.leezy.tianchi;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class Features {
	public static class FeatureMapper extends MapperBase {
		private Record key;
	    private Record value;
	    double elementNum = 0; //the number of the element that visited
		double elementAvgWaitTime = 0;// the time for visit a element
		@Override
		public void map(long recordNum, Record record, TaskContext context) throws IOException {
			// TODO Auto-generated method stub
			Record result = context.createOutputRecord();
			result.setBigint("id", record.getBigint("id"));
			result.setString("label", record.getString("label"));
			int elementWaitTime = 0;
			String elementInfo = record.getString("0");
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
			result.setDouble("elementNum", elementNum);
			result.setDouble("elementAvgWaitTime", elementWaitTime/elementNum);
	        context.write(result);
		}

		@Override
		public void setup(TaskContext context) throws IOException {
			// TODO Auto-generated method stub
			Record record = context.createOutputRecord();
		      StringBuilder importdata = new StringBuilder();
		      BufferedInputStream bufferedInput = null;
		      Pattern p = Pattern.compile("(\\[[^\\]]*\\])");
		      try {
		        byte[] buffer = new byte[1024];
		        int bytesRead = 0;

		        //String filename = context.getJobConf().get("stopwords.txt");
		        bufferedInput = context.readResourceFileAsStream("data");

		        while ((bytesRead = bufferedInput.read(buffer)) != -1) {
		          String chunk = new String(buffer, 0, bytesRead);
		          importdata.append(chunk);
		        }

		        String lines[] = importdata.toString().split("\n");
		        for (int i = 0; i < lines.length; i++) {
		        	int firstIndex = lines[i].indexOf(',');
		        	int lastIndex = lines[i].lastIndexOf(',')+1;
		        	record.setBigint("id",Long.valueOf(lines[i].substring(0, firstIndex)));
		        	record.setString("label",lines[i].substring(lastIndex));
		        	//record.set("", lines[i].);
		        	Matcher m = p.matcher(lines[i]);
		        	int j = 0;
		        	while(m.find()){
		        		//record.setString(j,m.group().substring(0, m.group().length()));
		        		System.out.println(m.group().substring(0, m.group().length()));
		        		j++;
		        		if(j == 1){
		        			//record.setString(arg0, arg1);
		        			record.setString("a1",m.group().substring(0, m.group().length()));
		        		}
		        		else if(j == 2){
		        			record.setString("a3",m.group().substring(0, m.group().length()));
		        		}
		        		else if(j == 3){
		        			record.setString("a4",m.group().substring(0, m.group().length()));
		        		}
		        		else if(j == 4){
		        			record.setString("a5",m.group().substring(0, m.group().length()));
		        		}
		        		else {
		        			record.setString("a7",m.group().substring(0, m.group().length()));
		        		}
		        		
		        	}
		          
		          context.write(record);
		        }
		       // System.out.println(record.toString());
		      } catch (FileNotFoundException ex) {
		        throw new IOException(ex);
		      } catch (IOException ex) {
		        throw new IOException(ex);
		      } finally {
		      }

			
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
