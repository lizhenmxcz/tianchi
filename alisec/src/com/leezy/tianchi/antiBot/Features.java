package com.leezy.tianchi.antiBot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.leezy.tianchi.util.ArrayUtil;

public class Features {
	public static class FeatureMapper extends MapperBase {
	    Pattern p = Pattern.compile("(\\{([^\\}]+)\\})"); 
		@Override
		public void map(long recordNum, Record record, TaskContext context) throws IOException {
			// TODO Auto-generated method stub
			//elecment feature
			long elementnum = 0; //the number of the element that visited
			long elementavgwaittime = 0;// the stay time for visit a element
			//button click feature
			long allbuttonnum = 0; // the count of click button
			long rightbuttonnum = 0; // the count of click the right button
			long leftbuttonnum = 0;// the count of click the left button
			long avgbuttoninterval = 0;// the average interval time between button click 
			//key feature
			long allkeynum = 0;   // the count of press key
			long avgkeyinterval = 0;// the average interval time between key press 
			Record result = context.createOutputRecord(); 
			try{
				//result.setString("label", record.getString("label"));
				/**
				 * extract the element feature
				 */
				String elementInfo = record.getString("a1");
				//elementInfo = elementInfo.replaceAll("\"", "\"\"");
				List<String> list=new ArrayList<String>();  
			       //Pattern p = Pattern.compile("(\\{{^\\}}\\})"); 
			     
	            Matcher m = p.matcher(elementInfo);  
			    while(m.find()){  
			        list.add(m.group().substring(0, m.group().length()));  
			        //System.out.println(m.group().substring(0, m.group().length()));
			    } 
				int elementWaitTime = 0;
				//JsonArray elementArray = new JsonParser().parse(elementInfo).getAsJsonArray();
				for(int i = 0;i< list.size();i++){
		            JsonObject temp=  new JsonParser().parse(list.get(i)).getAsJsonObject();
		            if(temp.get("type").getAsInt() == 0){//lost focus add the time
		            	elementWaitTime +=temp.get("time").getAsInt();
		            }
		            if(temp.get("type").getAsInt() == 1){// on focus increase the element numbet and minus the time 
		            	elementnum ++;
		            	elementWaitTime -= temp.get("time").getAsInt();
		            }
		        }
				
				if(elementnum != 0){
					elementavgwaittime = elementWaitTime/elementnum;
				}
				
				/**
				 * extract the button click feature
				 */
				String buttonInfo = record.getString("a3");
				List<String> listButton=new ArrayList<String>();  
			       //Pattern p = Pattern.compile("(\\{{^\\}}\\})"); 
			     
	            Matcher mButton = p.matcher(buttonInfo);  
			    while(mButton.find()){  
			    	listButton.add(mButton.group().substring(0, mButton.group().length()));  
			        //System.out.println(m.group().substring(0, m.group().length()));
			    } 
				allbuttonnum = listButton.size();
				int[] timeArray = new int[(int)allbuttonnum];// the button time array
				for(int j = 0;j< listButton.size();j++){
					JsonObject temp=  new JsonParser().parse(listButton.get(j)).getAsJsonObject();
					timeArray[j] = temp.get("time").getAsInt();
					if(temp.get("button").getAsString().equals("left")){
						leftbuttonnum++;
					}
					else {
						rightbuttonnum++;
					}
				}
				if(allbuttonnum==0) avgbuttoninterval=-1;
				else if(allbuttonnum==1) avgbuttoninterval=0;
				else avgbuttoninterval = ArrayUtil.avgInterval(timeArray);
				
				/**
				 * extract the key press feature
				 */
				String keyInfo = record.getString("a7");
				List<String> listKey=new ArrayList<String>();  
			       //Pattern p = Pattern.compile("(\\{{^\\}}\\})"); 
			     
	            Matcher mkey = p.matcher(keyInfo);  
			    while(mkey.find()){  
			    	listKey.add(mkey.group().substring(0, mkey.group().length()));  
			    } 
				allkeynum = listKey.size();
				int[] keytimeArray = new int[(int)allkeynum];// the button time array
				for(int j = 0;j< listKey.size();j++){
					JsonObject temp=  new JsonParser().parse(listKey.get(j)).getAsJsonObject();
					keytimeArray[j] = temp.get("time").getAsInt();
				}
				if(allkeynum==0) avgkeyinterval=-1;
				else if(allkeynum==1) avgkeyinterval=0;
				else avgkeyinterval = ArrayUtil.avgInterval(keytimeArray);
				
			}catch(Exception e){
				
			}			
			result.setBigint("id", record.getBigint("id"));
			result.setBigint("leftbuttonnum", leftbuttonnum);
			result.setBigint("rightbuttonnum", rightbuttonnum);
			result.setBigint("allbuttonnum", allbuttonnum);
			result.setBigint("avgbuttoninterval",avgbuttoninterval);
			result.setBigint("elementnum", elementnum);
			result.setBigint("elementavgwaittime", elementavgwaittime);
			result.setBigint("allkeynum", allkeynum);
			result.setBigint("avgkeyinterval",avgkeyinterval);
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