package dsiter.server;

import dsiter.IterUtils;
import dsiter.iterator.IDatasetIterator;
import dsiter.iterator.RangeIterator;
import dsiter.pipe.IPipe;
import dsiter.server.exceptions.ClientErrorException;
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

    @Test
    public void testParser1() throws ClientErrorException {
        IPipe[] pipes = new PipeParser().parsePipes("filter(value%2=0)|skip(2)|first");
        IDatasetIterator it = new RangeIterator(10);
        for(IPipe p : pipes) {
            it = it.pipe(p);
        }
        IterUtils.assertValues(
                it,
                "value",
                new Integer[] { 4 }
        );
    }
}
