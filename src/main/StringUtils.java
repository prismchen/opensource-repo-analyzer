package main;

import org.junit.Test;

import java.io.File;
import java.util.Collection;

/**
 * Created by xiaochen on 10/10/16.
 */
public class StringUtils {

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

    static int indexOfAnyBut(String str, char c) {
        if (str == null) {
            return -1;
        }
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                continue;
            }
            return i;
        }
        return str.length();
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
