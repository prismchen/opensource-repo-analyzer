package main;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by xiaochen on 10/10/16.
 */
public class Analyzer {
    public static final String[] EXTENTIONS = new String[]{"java"};
    private String mRopePath;
    private final Map<Integer, Integer> mWhiteSpace = new HashMap<>();
    private final Charset mCharset = Charset.forName("UTF-8");
    private Collection<File> mFilePool;

    public Analyzer(String repoPath) {
        mRopePath = repoPath;
        mFilePool = FileUtils.listFiles(new File(mRopePath), EXTENTIONS, true);
    }

    /**
     * Ctor
     * @param file
     */
    public void analyze(Path file) {
        try (BufferedReader reader = Files.newBufferedReader(file, mCharset)) {
            String line = null;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                if (StringUtils.isComment(line)) {
                    continue;
                }
                line = line.substring(0, StringUtils.indexOfAnyBut(line, ' '));
                count = StringUtils.countMatches(line, ' ');
                mWhiteSpace.put(count, mWhiteSpace.getOrDefault(count, 0) + 1);
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    public void genConcatFile() throws IOException {
        File output = new File(mRopePath + "Concat.java");
        FileUtils.writeByteArrayToFile(output, new byte[0]);
        for (File f : mFilePool) {
            FileUtils.writeByteArrayToFile(output, FileUtils.readFileToByteArray(f), true);
        }
    }


    public Collection<File> getFilePool() {
        return mFilePool;
    }

    public void analyzePool() {
        for (File f : getFilePool()) {
            analyze(f.toPath());
        }
    }

    public void printStats() {

        int totalNum = 0;
        int leastMajority = Integer.MAX_VALUE;
        int violates = 0;

        System.out.println("Number of Spaces : Occurrence");
        for (int spaceNum : mWhiteSpace.keySet()) {
            if (spaceNum != 0) {
                totalNum += mWhiteSpace.get(spaceNum);
            }
            System.out.println(spaceNum + " : " + mWhiteSpace.get(spaceNum));
        }

        for (int spaceNum : mWhiteSpace.keySet()) {
            if (spaceNum != 0 && mWhiteSpace.get(spaceNum) > totalNum / 20) {
                leastMajority = Math.min(leastMajority, spaceNum);
            }
        }

        System.out.println("Potential indent style: " + leastMajority);

        for (int spaceNum : mWhiteSpace.keySet()) {
            if (spaceNum % leastMajority != 0) {
                violates += mWhiteSpace.get(spaceNum);
            }
        }

        System.out.println("Number of violation : " + violates + "/" + totalNum);
    }

    public static void main(String[] args) {
        Analyzer a = new Analyzer(args[0]);
        try {
            a.genConcatFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        a.analyzePool();
        a.printStats();
    }
}
