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

public class ApacheProject extends Configured implements Tool {

    private static final String JSON_FIELD = "content";
    private static String unwantedPrefix = "{\"" + JSON_FIELD + "\":\"";
    private static String unwantedSuffix = "\"}";
    private static final String IMPORT_PREFIX = "import org.apache."; // Common import prefix for all Apache projects
    private static final int IMPORT_PREFIX_LEN = IMPORT_PREFIX.length();

    static public class ApacheProjectMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
        final private static LongWritable ONE = new LongWritable(1);
        PrefixTree projectTrie = new PrefixTree(); // PrefixTree (Trie) for storing all Apache project names

        @Override
        protected void map(LongWritable key, Text text, Context context) throws IOException, InterruptedException {
            String script = StringUtils.clearScript(text.toString(), unwantedPrefix, unwantedSuffix); // clear JSON format to pure text
            
            if (script != null) {
                script = script.replaceAll("\\/\\*([\\S\\s]+?)\\*\\/",""); // Remove comments
                List<String> lines = StringUtils.splitByLine(script);
                Set<String> results = new HashSet<>();

                for (String line : lines) {
                    if (line.startsWith(IMPORT_PREFIX)) {
                        line = line.substring(IMPORT_PREFIX_LEN).replaceAll("\\;", ""); // remove imports' trailing semicolon 
                        String searchResult = projectTrie.search(line.split("\\.")); // match import with a valid Apache project name; if not valid return null
                        if (searchResult != null) {
                            results.add(searchResult);
                        }
                    }
                }

                for (String result : results) {
                    context.write(new Text(result), ONE);
                }

            }     
        }
    }

    static public class ApacheProjectReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
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

        Job job = Job.getInstance(super.getConf(), "ApacheProject");
        job.setJarByClass(ApacheProject.class);

        job.setMapperClass(ApacheProjectMapper.class);
        job.setCombinerClass(ApacheProjectReducer.class);
        job.setReducerClass(ApacheProjectReducer.class);

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
        System.exit(ToolRunner.run(conf, new ApacheProject(), args));
    }
}