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
        List<IPipe> pipes = new ArrayList<>();

        for(int i=0; i<chunks.length; i++) {
            String chunk = chunks[i].trim();
            if (chunk.length() == 0) {
                continue;
            }

            int firstBracketIndex = chunk.indexOf('(');
            char lastChar = chunk.charAt(chunk.length()-1);

            String pipeName = null;
            String pipeArgs = null;
            if (firstBracketIndex == -1) {
                if (lastChar == ')') {
                    throw new FailedToParsePipeException(chunk);
                }
                pipeName = chunk;
                pipeArgs = "";
            }
            else {
                if (lastChar != ')') {
                    throw new FailedToParsePipeException(chunk);
                }
                pipeName = chunk.substring(0, firstBracketIndex);
                pipeArgs = chunk.substring(firstBracketIndex+1, chunk.length()-1);
            }

            pipes.add(pipeMap.getPipe(pipeName, pipeArgs));
        }

        return pipes.toArray(new IPipe[0]);
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
