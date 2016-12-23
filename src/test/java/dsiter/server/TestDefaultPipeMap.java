package dsiter.server;

import dsiter.IterUtils;
import dsiter.iterator.IDatasetIterator;
import dsiter.iterator.RangeIterator;
import dsiter.pipe.IPipe;
import dsiter.server.exceptions.ClientErrorException;
import org.junit.Test;

public class TestDefaultPipeMap {

    @Test
    public void testFilterPipe() throws Exception {
        IPipe pipe = new DefaultPipeMap().getPipe("filter", "value%2=0");
        IDatasetIterator it = new RangeIterator(10).pipe(pipe);
        IterUtils.assertValues(
                it,
                "value",
                new Integer[] { 0, 2, 4, 6, 8 }
        );
    }

    @Test
    public void testFirstPipe() throws Exception {
        IPipe pipe = new DefaultPipeMap().getPipe("first", "");
        IDatasetIterator it = new RangeIterator(10).pipe(pipe);
        IterUtils.assertValues(
                it,
                "value",
                new Integer[] { 0 }
        );
    }

    @Test
    public void testLastPipe() throws Exception {
        IPipe pipe = new DefaultPipeMap().getPipe("last", "");
        IDatasetIterator it = new RangeIterator(10).pipe(pipe);
        IterUtils.assertValues(
                it,
                "value",
                new Integer[] { 9 }
        );
    }

    @Test
    public void testRenamePipe() throws Exception {
        IPipe pipe = new DefaultPipeMap().getPipe("rename", "value, foo");
        IDatasetIterator it = new RangeIterator(10).pipe(pipe);
        IterUtils.assertValues(
                it,
                "foo",
                new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }
        );
    }

    @Test
    public void testSkipPipe() throws Exception {
        IPipe pipe = new DefaultPipeMap().getPipe("skip", "5");
        IDatasetIterator it = new RangeIterator(10).pipe(pipe);
        IterUtils.assertValues(
                it,
                "value",
                new Integer[] { 5, 6, 7, 8, 9 }
        );
    }

    @Test
    public void testStridePipe() throws Exception {
        IPipe pipe = new DefaultPipeMap().getPipe("stride", "5");
        IDatasetIterator it = new RangeIterator(10).pipe(pipe);
        IterUtils.assertValues(
                it,
                "value",
                new Integer[] { 0, 5 }
        );
    }

    @Test
    public void testTakePipe() throws Exception {
        IPipe pipe = new DefaultPipeMap().getPipe("take", "5");
        IDatasetIterator it = new RangeIterator(10).pipe(pipe);
        IterUtils.assertValues(
                it,
                "value",
                new Integer[] { 0, 1, 2, 3, 4 }
        );
    }
}


