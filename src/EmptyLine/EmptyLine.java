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

public class EmptyLine extends Configured implements Tool {

    private static final String JSON_FIELD = "content";
    private static String unwantedPrefix = "{\"" + JSON_FIELD + "\":\"";
    private static String unwantedSuffix = "\"}";

    static public class EmptyLineMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
        final private static LongWritable ONE = new LongWritable(1);

        @Override
        protected void map(LongWritable key, Text text, Context context) throws IOException, InterruptedException {
            String script = StringUtils.clearScript(text.toString(), unwantedPrefix, unwantedSuffix); // clear JSON format to pure text
            
            if (script != null) {

                List<String> lines = StringUtils.splitByLine(script);
                int totalLines = lines.size();
                if (totalLines == 0) {
                    context.write(new Text("Other"), ONE);
                    return;
                }
                int emptyLines = 0;

                for (String line : lines) {
                    line = line.replaceAll("\\\\t", "");
                    if (StringUtils.indexOfAnyBut(line, ' ') == line.length()) {
                        emptyLines++;
                    }
                }

                double fraction = 100.0 * emptyLines / totalLines;
                if (fraction < 5.0) {
                    context.write(new Text("< 5.0 %"), ONE);
                }
                else if (fraction < 10.0) {
                    context.write(new Text("< 10.0 %"), ONE);
                }
                else if (fraction < 15.0) {
                    context.write(new Text("< 15.0 %"), ONE);
                }
                else if (fraction < 20.0) {
                    context.write(new Text("< 20.0 %"), ONE);
                }
                else if (fraction < 25.0) {
                    context.write(new Text("< 25.0 %"), ONE);
                }
                else if (fraction < 30.0) {
                    context.write(new Text("< 30.0 %"), ONE);
                }
                else {
                    context.write(new Text(">= 30.0 %"), ONE);
                }   
            }
        }
    }

    static public class EmptyLineReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
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

        Job job = Job.getInstance(super.getConf(), "EmptyLine");
        job.setJarByClass(EmptyLine.class);

        job.setMapperClass(EmptyLineMapper.class);
        job.setCombinerClass(EmptyLineReducer.class);
        job.setReducerClass(EmptyLineReducer.class);

        job.setInputFormatClass(MultiLineJsonInputFormat.class); // use the JSON input format
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
        System.exit(ToolRunner.run(conf, new EmptyLine(), args));
    }
}