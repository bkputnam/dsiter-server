package dsiter.server;

import dsiter.server.exceptions.FailedToParsePipeStringException;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestPipeParser {

    @Test
    public void testChunker1() throws Exception {
        String[] chunks = PipeParser.split("foo|bar|baz");
        assertArrayEquals(
                new String[]{"foo", "bar", "baz"},
                chunks
        );
    }

    @Test
    public void testChunker2() throws Exception {
        String[] chunks = PipeParser.split("foo(|)|bar|baz");
        assertArrayEquals(
                new String[]{"foo(|)", "bar", "baz"},
                chunks
        );
    }

    @Test(expected=FailedToParsePipeStringException.class)
    public void testChunker3() throws Exception {
        String[] chunks = PipeParser.split("foo(|))|bar|baz");
    }

    @Test(expected=FailedToParsePipeStringException.class)
    public void testChunker4() throws Exception {
        String[] chunks = PipeParser.split("foo(|()|bar|baz");
    }
}
