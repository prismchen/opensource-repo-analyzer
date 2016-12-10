import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import com.alexholmes.json.mapreduce.MultiLineJsonInputFormat;

import java.util.*;
import java.io.IOException;

public class SpaceCount extends Configured implements Tool {

    private static final String JSON_FIELD = "content";
    private static String unwantedPrefix = "{\"" + JSON_FIELD + "\":\"";
    private static String unwantedSuffix = "\"}";

    static public class SpaceCountMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
        final private static LongWritable ONE = new LongWritable(1);
        private Map<Integer, Integer> spaceStats;

        @Override
        protected void map(LongWritable key, Text text, Context context) throws IOException, InterruptedException {
            String script = StringUtils.clearScript(text.toString(), unwantedPrefix, unwantedSuffix);
            
            if (script != null) {

                script = script.replaceAll("\\/\\*([\\S\\s]+?)\\*\\/","");

                List<String> lines = StringUtils.splitByLine(script);
                int count = 0;
                spaceStats = new HashMap<>();

                for (String line : lines) {
                    if (line.startsWith("\\t")) {
                        context.write(new Text("tab"), ONE);
                        return;
                    }

                    line = line.substring(0, StringUtils.indexOfAnyBut(line, ' '));
                    count = StringUtils.countMatches(line, ' ');

                    if (count == 0) {
                        continue;
                    }

                    if (!spaceStats.containsKey(count)) {
                        spaceStats.put(count, 1);
                    }
                    else {
                        spaceStats.put(count, spaceStats.get(count) + 1);
                    }
                }

                if (spaceStats.size() == 0) {
                    context.write(new Text("no indent"), ONE);
                    return;
                }

                int totalNum = 0;
                int leastMajority = Integer.MAX_VALUE;

                for (int spaceNum : spaceStats.keySet()) {
                    totalNum += spaceStats.get(spaceNum);
                }

                for (int spaceNum : spaceStats.keySet()) {
                    if (spaceStats.get(spaceNum) > totalNum / 50) {
                        leastMajority = Math.min(leastMajority, spaceNum);
                    }
                }

                context.write(new Text("" + leastMajority), ONE);
            }     
        }
    }

    static public class SpaceCountReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
        private LongWritable total = new LongWritable();

        @Override
        protected void reduce(Text token, Iterable<LongWritable> counts, Context context) throws IOException, InterruptedException {
            long n = 0;
            for (LongWritable count : counts) {
                n += count.get();
            }

            total.set(n);
            context.write(token, total);
        }
    }

    public int run(String[] args) throws Exception {

        String input = args[0];
        Path outputPath = new Path(args[1]);

        Job job = Job.getInstance(super.getConf(), "SpaceCount");
        job.setJarByClass(SpaceCount.class);

        job.setMapperClass(SpaceCountMapper.class);
        job.setCombinerClass(SpaceCountReducer.class);
        job.setReducerClass(SpaceCountReducer.class);

        // use the JSON input format
        job.setInputFormatClass(MultiLineJsonInputFormat.class);
        MultiLineJsonInputFormat.setInputJsonMember(job, JSON_FIELD);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.setInputPaths(job, input);
        FileOutputFormat.setOutputPath(job, outputPath);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        return job.waitForCompletion(true) ? 0 : -1;
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        System.exit(ToolRunner.run(conf, new SpaceCount(), args));
    }
}