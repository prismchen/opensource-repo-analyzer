package main;

import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by xiaochen on 10/10/16.
 */
public class StringUtils {

    public static final String[] COMMENTSIGNS = new String[]{"/**", "*", "*/", "//" };


    static int countMatches(String str, char c) {
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

    static int indexOfAnyBut(String line, char c) {
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

    static boolean isComment(String line) {
        String firstElem = firstNonSpaceElem(line);
        for (String commentSign : COMMENTSIGNS) {
            if (firstElem.startsWith(commentSign)) {
                return true;
            }
        }
        return false;
    }

    static String firstNonSpaceElem(String line) {
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

    /**
     * Created by xiaochen on 10/11/16.
     */
    public static class AnalyzerTest {
        @Test
        public void ctorTest() throws Exception {
            Analyzer a = new Analyzer("data");
            Collection<File> files = a.getFilePool();
            assert(files.size() > 0);
        }



    }
}
