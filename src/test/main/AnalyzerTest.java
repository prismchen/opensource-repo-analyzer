package main;

import org.junit.Test;

import java.io.File;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Created by xiaochen on 10/11/16.
 */
public class AnalyzerTest {
    @Test
    public void ctorTest() throws Exception {
        Analyzer a = new Analyzer("data");
        Collection<File> files = a.getFilePool();
        assertTrue(files.size() > 0);
    }

    @Test
    public void analyzeTest() throws Exception {
        Analyzer a = new Analyzer("data");
        a.analyzePool();
        a.printStats();
    }

    @Test
    public void firstNonSpaceElemTest() {
        String str1 = " daf daf daf";
        String str2 = null;
        String str3 = "     ";
        assertEquals("daf", StringUtils.firstNonSpaceElem(str1));
        assertEquals("", StringUtils.firstNonSpaceElem(str2));
        assertEquals("", StringUtils.firstNonSpaceElem(str3));
    }

    @Test
    public void isCommentTest() {
        String str1 = "//aklglkjalg";
        String str2 = "/** aklglkjalg";
        String str3 = "* aklglkjalg";
        String str4 = "*/";
        String str5 = "System";
        assertTrue(StringUtils.isComment(str1));
        assertTrue(StringUtils.isComment(str2));
        assertTrue(StringUtils.isComment(str3));
        assertTrue(StringUtils.isComment(str4));
        assertTrue(!StringUtils.isComment(str5));
    }
}