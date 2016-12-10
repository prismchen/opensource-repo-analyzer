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

public class BracketStyle extends Configured implements Tool {

    private static final String JSON_FIELD = "content";
    private static String unwantedPrefix = "{\"" + JSON_FIELD + "\":\"";
    private static String unwantedSuffix = "\"}";

    static public class BracketStyleMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
        final private static LongWritable ONE = new LongWritable(1);
        private int inline;
        private int nextline;

        @Override
        protected void map(LongWritable key, Text text, Context context) throws IOException, InterruptedException {
            String script = StringUtils.clearScript(text.toString(), unwantedPrefix, unwantedSuffix); // clear JSON format to pure text
            
            if (script != null) {

                script = script.replaceAll("\\/\\*([\\S\\s]+?)\\*\\/",""); // Remove coomments

                List<String> lines = StringUtils.splitByLine(script);

                inline = 0;
                nextline = 0;

                for (String line : lines) {
                    line = line.replaceAll("\\\\t", ""); // clear out all possible tabs

                    int index = StringUtils.indexOf(line, '{');

                    if (index >= 0) {
                        if (index == StringUtils.indexOfAnyBut(line, ' ')) { // if '{' is the first char appearing in present line except for spaces & tabs
                            nextline++;
                        }
                        else {
                            inline++;
                        }
                        inline += StringUtils.countMatches(line, '{') - 1; // count rest of '{'s as inline
                    }
                }
                
                if (inline > nextline) {
                    context.write(new Text("Inline"), ONE);
                }
                else if (inline < nextline) {
                    context.write(new Text("Next line"), ONE);
                }
                else {
                    context.write(new Text("Unclear"), ONE);
                }
            }     
        }
    }

    static public class BracketStyleReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
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

        Job job = Job.getInstance(super.getConf(), "BracketStyle");
        job.setJarByClass(BracketStyle.class);

        job.setMapperClass(BracketStyleMapper.class);
        job.setCombinerClass(BracketStyleReducer.class);
        job.setReducerClass(BracketStyleReducer.class);

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
        System.exit(ToolRunner.run(conf, new BracketStyle(), args));
    }
}