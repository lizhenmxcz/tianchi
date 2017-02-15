package com.leezy.tianchi.antispam;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.TaskContext;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;
import com.aliyun.odps.mapred.utils.SchemaUtils;

public class Upload {

      public static class UploadMapper extends MapperBase {
        @Override
        public void setup(TaskContext context) throws IOException {
          Record record = context.createOutputRecord();
          StringBuilder importdata = new StringBuilder();
          BufferedInputStream bufferedInput = null;

          try {
            byte[] buffer = new byte[1024];
            int bytesRead = 0;

            String filename = context.getJobConf().get("stopwords.txt");
            bufferedInput = context.readResourceFileAsStream(filename);

            while ((bytesRead = bufferedInput.read(buffer)) != -1) {
              String chunk = new String(buffer, 0, bytesRead);
              importdata.append(chunk);
            }

            String lines[] = importdata.toString().split("\n");
            for (int i = 0; i < lines.length; i++) {
              //String[] ss = lines[i].split(",");
              //record.set(0, Long.parseLong(ss[0].trim()));
              record.set(0, lines[i].trim());
              context.write(record);
            }
          } catch (FileNotFoundException ex) {
            throw new IOException(ex);
          } catch (IOException ex) {
            throw new IOException(ex);
          } finally {
          }

        }

        @Override
        public void map(long recordNum, Record record, TaskContext context)
            throws IOException {

        }

      }

      public static void main(String[] args) throws Exception {
        if (args.length != 2) {
          System.err.println("Usage: Upload <import_txt> <out_table>");
          System.exit(2);
        }

        JobConf job = new JobConf();

        job.setMapperClass(UploadMapper.class);

        job.set("stopwords.txt", args[0]);

        job.setNumReduceTasks(0);

        //job.setMapOutputKeySchema(SchemaUtils.fromString("key:bigint"));
        //job.setMapOutputValueSchema(SchemaUtils.fromString("value:string"));

       // InputUtils.addTable(TableInfo.builder().tableName("mr_empty").build(), job);
        OutputUtils.addTable(TableInfo.builder().tableName(args[1]).build(), job);

        JobClient.runJob(job);
      }

    }