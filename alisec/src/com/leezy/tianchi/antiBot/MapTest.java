package com.leezy.tianchi.antiBot;

import java.io.IOException;

import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;

public class MapTest {
	public static class FeatureMapper extends MapperBase {
		private Record key;
	    private Record value;
	    
		@Override
		public void map(long recordNum, Record record, TaskContext context) throws IOException {
			// TODO Auto-generated method stub
			//elecment feature
			Record result = context.createOutputRecord();
			result.setBigint("id", record.getBigint("id"));
			result.setString("a1", record.getString("a1"));
			result.setString("a3", record.getString("a3"));
			result.setString("a4", record.getString("a4"));
			result.setString("a5", record.getString("a5"));
			result.setString("a7", record.getString("a7"));
			result.setString("label", record.getString("label"));
			context.write(result);
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
}
