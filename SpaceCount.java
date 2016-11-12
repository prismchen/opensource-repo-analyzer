import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class SpaceCount {

    public static final String[] COMMENT_SIGNS = new String[]{"/**", "*", "*/", "//" };

    private static int indexOfAnyBut(String line, char c)
    {
        if (line == null)
        {
            return -1;
        }
        for (int i = 0; i < line.length(); i++)
        {
            if (line.charAt(i) == c)
            {
                continue;
            }
            return i;
        }
        return line.length();
    }

    private static int countMatches(String str, char c) {
        if (str == null) {
            return 0;
        }
        int count = 0;
        for (char ch : str.toCharArray()) {
            if (ch == c) {
                count++;
            }
        }
        return count;
    }
    
    private static String firstNonSpaceElem(String line) {
        int start = indexOfAnyBut(line, ' ');
        if (start == -1 || start == line.length()) {
            return "";
        }
        int end = start;

        while (end < line.length() && line.charAt(end) != ' ') {
            end++;
        }
        return line.substring(start, end);
    }


    private static boolean isComment(String line) {
        String firstElem = firstNonSpaceElem(line);
        for (String commentSign : COMMENT_SIGNS) {
            if (firstElem.startsWith(commentSign)) {
                return true;
            }
        }
        return false;
    }

    public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text numSpaces = new Text();
        private String line;

        public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
            line = value.toString();
            if (!isComment(line)) {
                line = line.substring(0, indexOfAnyBut(line, ' '));
                int count = countMatches(line, ' ');
                if (count > 0) {
                    numSpaces.set(String.valueOf(count));
                    output.collect(numSpaces, one);
                }
            }
        }
    }

    public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
        public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
            int sum = 0;
            while (values.hasNext()) {
                sum += values.next().get();
            }
            output.collect(key, new IntWritable(sum));
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

