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
    private final Map<Integer, Integer> mWhiteSpace = new HashMap<>();
    private final Charset mCharset = Charset.forName("UTF-8");
    private Collection<File> mFilePool;

    public Analyzer(String repoPath) {
        mFilePool = FileUtils.listFiles(new File(repoPath), EXTENTIONS, true);
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

    public Collection<File> getFilePool() {
        return mFilePool;
    }

    public void analyzePool() {
        for (File f : getFilePool()) {
            analyze(f.toPath());
        }
    }

    public void printStats() {
        System.out.println("Number of Spaces : Occurrence");
        for (int spaceNum : mWhiteSpace.keySet()) {
            System.out.println(spaceNum + " : " + mWhiteSpace.get(spaceNum));
        }
    }

}
