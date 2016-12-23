package dsiter.server;

import dsiter.pipe.IPipe;
import dsiter.server.exceptions.ClientErrorException;
import dsiter.server.exceptions.FailedToParsePipeException;
import dsiter.server.exceptions.FailedToParsePipeStringException;

import java.util.ArrayList;
import java.util.List;

public class PipeParser {

    private IPipeMap pipeMap;

    public PipeParser(IPipeMap pipeMap) {
        this.pipeMap = pipeMap;
    }

    public PipeParser() {
        this(new DefaultPipeMap());
    }

    public IPipe[] parsePipes(String pipeStr) throws ClientErrorException {
        String[] chunks = split(pipeStr);
        IPipe[] pipes = new IPipe[chunks.length];

        for(int i=0; i<chunks.length; i++) {
            String chunk = chunks[i];
            int firstBracketIndex = chunk.indexOf('(');
            if (chunk.charAt(chunk.length()-1) != ')') {
                throw new FailedToParsePipeException(chunk);
            }

            String pipeName = chunk.substring(0, firstBracketIndex);
            String pipeArgs = chunk.substring(firstBracketIndex+1, chunk.length()-1);

            pipes[i] = pipeMap.getPipe(pipeName, pipeArgs);
        }

        return pipes;
    }

    static String[] split(String pipeStr) throws ClientErrorException {
        int parenCount = 0;
        int curChunkStart = 0;

        List<String> chunks = new ArrayList<>();

        for(int i=0; i<pipeStr.length(); i++) {
            char c = pipeStr.charAt(i);

            if (c == '(') {
                parenCount++;
            }
            else if (c == ')') {
                parenCount--;
                if (parenCount < 0) {
                    throw new FailedToParsePipeStringException(pipeStr);
                }
            }
            else if (c == '|' && parenCount==0) {
                chunks.add(pipeStr.substring(curChunkStart, i));
                curChunkStart = i+1;
            }
        }

        if (parenCount != 0) {
            throw new FailedToParsePipeStringException(pipeStr);
        }

        chunks.add(pipeStr.substring(curChunkStart));

        return chunks.toArray(new String[0]);
    }
}
