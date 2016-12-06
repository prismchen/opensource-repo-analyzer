import java.util.*;
import java.io.IOException;

public class StringUtils {

    public static String clearScript(String script, String unwantedPrefix, String unwantedSuffix) {
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

    public static List<String> splitByLine(String script) {
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

    public static int indexOfAnyBut(String line, char c) {
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
    
    public static int indexOf(String line, char c) {
        if (line == null) {
            return -1;
        }
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == c) {
                return i;
            }
        }
        return -1;
    }

    public static int countMatches(String str, char c) {
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
}