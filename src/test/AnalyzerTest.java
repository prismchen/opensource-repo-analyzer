import org.junit.*;

import java.io.File;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Created by xiaochen on 10/10/16.
 */
public class AnalyzerTest {
    @Test
    public void analyzeTest() {
        Analyzer a = new Analyzer("data");
        Collection<File> files = a.getFilePool();
        assert(files.size() > 0);
    }
}