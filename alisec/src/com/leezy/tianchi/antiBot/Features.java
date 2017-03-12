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
import com.leezy.tianchi.util.MouseEventUtil;

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
			long buttonnum = 0;
			long buttontotaltime = 0;
			long buttontotaldistance = 0;
			long buttonavgspeed = 0;
			long buttonspeedvariance = 0;
			long buttonaccvariance = 0; 
			//key feature
			long keynum = 0;   // the count of press key
			long keytotaltime = 0;// the average interval time between key press 
			long keyvariance = 0;
			//slider feature
			long slidernum = 0;
			long slidertotaltime = 0;
			long slidertotaldistance = 0;
			long slideryvariance = 0;
			long slideravgspeed = 0;
			long sliderspeedvariance = 0;
			long slideraccvariance = 0;
			//move feature
			long movenum = 0;
			long movetotaltime = 0;
			long movetotaldistance = 0;
			long moveavgspeed = 0;
			long movespeedvariance = 0;
			long moveaccvariance = 0;
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
				int bigTime = 0;
				//JsonArray elementArray = new JsonParser().parse(elementInfo).getAsJsonArray();
				for(int i = 0;i< list.size();i++){
		            JsonObject temp=  new JsonParser().parse(list.get(i)).getAsJsonObject();
		            if(temp.get("type").getAsInt() == 0){//lost focus add the time
		            	elementWaitTime +=temp.get("time").getAsInt();
		            }
		            if(temp.get("type").getAsInt() == 1){// on focus increase the element numbet and minus the time 
		            	elementnum ++;
		            	elementWaitTime -= temp.get("time").getAsInt();
		            	if(temp.get("time").getAsInt() > bigTime)
		            		bigTime = temp.get("time").getAsInt();
		            }
		        }
				if(elementnum % 2 == 1){
					elementWaitTime += bigTime;
				}
				if(elementnum != 0){
					elementavgwaittime = elementWaitTime/elementnum;
				}
				
				/**
				 * extract the button click feature
				 */
				String buttonInfo = record.getString("a3");
				MouseEventUtil  buttonUtil = new MouseEventUtil();
				buttonUtil.parseButtonEvent(buttonInfo);
				buttonnum = buttonUtil.getEventSize();
				buttontotaltime = buttonUtil.getTotalTime();
				buttontotaldistance = buttonUtil.getTotalDistance();
				buttonavgspeed = buttonUtil.getAvgSpeed();
				buttonspeedvariance = buttonUtil.speedVariance();
				//buttonaccvariance = buttonUtil.accelerationVariance();
				//System.out.println(buttonaccvariance);
				/**
				 * extract the key press feature
				 */
				String keyInfo = record.getString("a7");
				MouseEventUtil keyUtil = new MouseEventUtil();
				keyUtil.parseKeyEvent(keyInfo); 
				keynum = keyUtil.getEventSize();
				keytotaltime = keyUtil.getTotalTime();
				keyvariance = keyUtil.keySpeedVariance();
				System.out.println(keyvariance);
				/**
				 * extract the slider feature
				 */
				String sliderInfo = record.getString("a4");
				MouseEventUtil mouseEventUtil = new MouseEventUtil();
				mouseEventUtil.parseSliderEvent(sliderInfo); 
				slidernum = mouseEventUtil.getEventSize();
				slidertotaltime = mouseEventUtil.getTotalTime();
				slidertotaldistance = mouseEventUtil.getTotalDistance();
				slideryvariance = mouseEventUtil.sliderYVariance();
				slideravgspeed = mouseEventUtil.getAvgSpeed();
				sliderspeedvariance = mouseEventUtil.speedVariance();
				slideraccvariance = mouseEventUtil.accelerationVariance();
				//System.out.println(slideraccvariance);
				/**
				 * extract the move feature
				 */
				String moveInfo = record.getString("a5");
				MouseEventUtil mouseMoveUtil = new MouseEventUtil();

				mouseMoveUtil.parseMouseMoveEvent(moveInfo);
				movenum = mouseMoveUtil.getEventSize();
				movetotaltime = mouseMoveUtil.getTotalTime();
				movetotaldistance = mouseMoveUtil.getTotalDistance();
				moveavgspeed = mouseMoveUtil.getAvgSpeed();
				movespeedvariance = mouseMoveUtil.speedVariance();
				moveaccvariance = mouseMoveUtil.accelerationVariance();
			}catch(Exception e){
				//System.out.println(e.getStackTrace());
				e.printStackTrace();
			}			
			result.setBigint("id", record.getBigint("id"));
			result.setBigint("buttonnum", buttonnum);
			result.setBigint("buttontotaltime", buttontotaltime);
			result.setBigint("buttontotaldistance", buttontotaldistance);
			result.setBigint("buttonavgspeed",buttonavgspeed);
			//result.setBigint("buttonspeedvariance",buttonspeedvariance);
			result.setBigint("buttonaccvariance",buttonaccvariance);
			result.setBigint("elementnum", elementnum);
			result.setBigint("elementavgwaittime", elementavgwaittime);
			result.setBigint("keynum", keynum);
			result.setBigint("keytotaltime",keytotaltime);
			result.setBigint("keyvariance",keyvariance);
			result.setBigint("slidernum", slidernum);
			result.setBigint("slidertotaltime", slidertotaltime);
			result.setBigint("slidertotaldistance", slidertotaldistance);
			result.setBigint("slideryvariance", slideryvariance);
			result.setBigint("slideravgspeed", slideravgspeed);
			result.setBigint("sliderspeedvariance", sliderspeedvariance);
			result.setBigint("slideraccvariance", slideraccvariance);
			result.setBigint("movenum", movenum);
			result.setBigint("movetotaltime", movetotaltime);
			result.setBigint("movetotaldistance", movetotaldistance);
			result.setBigint("moveavgspeed", moveavgspeed);
			result.setBigint("movespeedvariance", movespeedvariance);
			result.setBigint("moveaccvariance", moveaccvariance);
			//long label = record.getString("label").equals("1")?1:0;
			////result.setString("label", label);
			//long label = Integer.valueOf(record.getString("label"));
			//result.setBigint("label", label);
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