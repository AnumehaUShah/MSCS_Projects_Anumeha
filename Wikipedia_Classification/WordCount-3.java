/**************************************
*  CS 267 Project                     *
*  @author : Anumeha shah             *
*  count word frquency for class label*
***************************************
*/


import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class WordCount {

  public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
    private Text mapreduce_output= new Text();

    public void map(LongWritable map_key, Text key_value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
      String line = key_value.toString();
	String[] row = line.split(",");
	if(row.length != 2)
		return;
	
	String data = row[0];
	String label = row[1];
	String [] words = data.split("[\\W]");
	for(String s: words)
	{
		if(s == null || s.trim().equals("") || isNumeric(s))
			continue;	
		String key_name = label + "," +s.trim() + ",";
		mapreduce_output.set(key_name);
		output.collect(mapreduce_output,one);
	}
      }
	public static boolean isNumeric(String s)  
	{  
  		try  
  		{  
    			double ddl = Double.parseDouble(s);  
  		}  
  		catch(NumberFormatException nfex)  
  		{  
    			return false;  
  		}  
  		return true;  
	}
    }

  public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
    public void reduce(Text map_key, Iterator<IntWritable> key_values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
      int sum_of_words = 0;
      while (key_values.hasNext()) {
        sum_of_words += key_values.next().get();
      }
      output.collect(map_key, new IntWritable(sum_of_words ));
    }
  }

  public static void main(String[] args) throws Exception {
    JobConf conf = new JobConf(WordCount.class);
    conf.setJobName("wordcount");

    conf.setOutputKeyClass(Text.class);
    conf.setOutputValueClass(IntWritable.class);

    conf.setMapperClass(Map.class);
    conf.setCombinerClass(Reduce.class);
    conf.setReducerClass(Reduce.class);

    conf.setInputFormat(TextInputFormat.class);
    conf.setOutputFormat(TextOutputFormat.class);

    FileInputFormat.setInputPaths(conf, new Path(args[0]));
    FileOutputFormat.setOutputPath(conf, new Path(args[1]));

    JobClient.runJob(conf);
  }
}
