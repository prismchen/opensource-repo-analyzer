import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
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

    static public class SpaceCountMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
        final private static LongWritable ONE = new LongWritable(1);
        private String unwantedPrefix = "{\"" + JSON_FIELD + "\":\"";
        private String unwantedSuffix = "\"}";
        private Map<Integer, Integer> spaceStats;

        private String clearScript(String script) {
            int scriptStart = script.indexOf(unwantedPrefix);
            if (scriptStart < 0) {
                return null;
            }
            else {
                scriptStart += unwantedPrefix.length();
            }
            int scriptEnd = script.lastIndexOf(unwantedSuffix);
            if (scriptEnd != script.length() - unwantedSuffix.length()) {
                return null;
            }
            script = script.substring(scriptStart, scriptEnd);
            return script;
        }

        private List<String> splitByLine(String script) {
            List<String> result = new ArrayList<>();
            int index = script.indexOf("\\n");
            while (index >= 0) {
                result.add(script.substring(0, index));
                script = script.substring(index + 2);
                index = script.indexOf("\\n");
            }
            result.add(script);
            return result;
        }

        private int indexOfAnyBut(String line, char c) {
            if (line == null) {
                return -1;
            }
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == c) {
                    continue;
                }
                return i;
            }
            return line.length();
        }

        private int countMatches(String str, char c) {
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

        @Override
        protected void map(LongWritable key, Text text, Context context) throws IOException, InterruptedException {
            String script = clearScript(text.toString());
            
            if (script != null) {

                script = script.replaceAll("\\/\\*([\\S\\s]+?)\\*\\/","");

                List<String> lines = splitByLine(script);
                int count = 0;
                spaceStats = new HashMap<>();

                for (String line : lines) {
                    if (line.startsWith("\\t")) {
                        context.write(new Text("-1"), ONE);
                        return;
                    }

                    line = line.substring(0, indexOfAnyBut(line, ' '));
                    count = countMatches(line, ' ');

                    if (!spaceStats.containsKey(count)) {
                        spaceStats.put(count, 1);
                    }
                    else {
                        spaceStats.put(count, spaceStats.get(count) + 1);
                    }
                }

                int totalNum = 0;
                int leastMajority = Integer.MAX_VALUE;

                for (int spaceNum : spaceStats.keySet()) {
                    if (spaceNum != 0) {
                        totalNum += spaceStats.get(spaceNum);
                    }
                }

                for (int spaceNum : spaceStats.keySet()) {
                    if (spaceNum != 0 && spaceStats.get(spaceNum) > totalNum / 20) {
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

        Configuration configuration = getConf();

        Job job = Job.getInstance(configuration, "SpaceCount");
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
        System.exit(ToolRunner.run(new SpaceCount(), args));
    }
}