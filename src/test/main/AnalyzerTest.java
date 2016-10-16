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
}