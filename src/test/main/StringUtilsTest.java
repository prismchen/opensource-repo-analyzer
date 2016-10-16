package main;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by xiaochen on 10/11/16.
 */
public class StringUtilsTest {
    @Test
    public void countMatches() throws Exception {
        String testStr = "a*b*c*d";
        assertEquals(3, StringUtils.countMatches(testStr, '*'));
    }

    @Test
    public void indexOfAnyBut() throws Exception {
        String testStr0 = "    abc";
        String testStr1 = "    a b c ";
        assertEquals(4, StringUtils.indexOfAnyBut(testStr0, ' '));
        assertEquals(4, StringUtils.indexOfAnyBut(testStr1, ' '));
    }

}