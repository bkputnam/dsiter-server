package dsiter.server;

import dsiter.pipe.*;
import dsiter.server.exceptions.ClientErrorException;
import dsiter.server.exceptions.InvalidPipeArgumentException;

public class DefaultPipeMap implements IPipeMap {
    @Override
    public IPipe getPipe(String pipeName, String args) throws ClientErrorException {
        switch (pipeName) {
            case "filter": return getFilterPipe(args);
            case "first": return getFirstPipe(args);
            case "last": return getLastPipe(args);
            case "rename": return getRenamePipe(args);
            case "skip": return getSkipPipe(args);
            case "skipWhile": return getSkipWhilePipe(args);
            case "stride": return getStridePipe(args);
            case "take": return getTakePipe(args);
            case "takeWhile": return getTakeWhilePipe(args);
            default:
                throw new IllegalArgumentException("Unrecognized pipe: \"" + pipeName + "\"");
        }
    }

    private static IPipe getFilterPipe(String args) throws ClientErrorException {
        return new FilterPipe(args);
    }

    private static IPipe getFirstPipe(String args) throws ClientErrorException {
        if (!args.equals("")) {
            throw new InvalidPipeArgumentException("first", args);
        }
        return new FirstPipe();
    }

    private static IPipe getLastPipe(String args) throws ClientErrorException {
        if (!args.equals("")) {
            throw new InvalidPipeArgumentException("last", args);
        }
        return new LastPipe();
    }

    private static IPipe getRenamePipe(String args) throws ClientErrorException {
        String[] chunks = args.split(",");
        if (chunks.length != 2) {
            throw new InvalidPipeArgumentException("rename", args);
        }
        String fromName = chunks[0].trim();
        String toName = chunks[1].trim();

        return new RenamePipe(fromName, toName);
    }

    private static IPipe getSkipPipe(String args) throws ClientErrorException {
        int skip;
        try {
            skip = Integer.parseInt(args);
        }
        catch (NumberFormatException e) {
            throw new InvalidPipeArgumentException("skip", args);
        }
        return new SkipPipe(skip);
    }

    private static IPipe getSkipWhilePipe(String args) throws ClientErrorException {
        return new SkipWhilePipe(args);
    }

    private static IPipe getStridePipe(String args) throws ClientErrorException {
        int stride;
        try {
            stride = Integer.parseInt(args);
        }
        catch (NumberFormatException e) {
            throw new InvalidPipeArgumentException("stride", args);
        }
        return new StridePipe(stride);
    }

    private static IPipe getTakePipe(String args) throws ClientErrorException {
        int take;
        try {
            take = Integer.parseInt(args);
        }
        catch (NumberFormatException e) {
            throw new InvalidPipeArgumentException("take", args);
        }
        return new TakePipe(take);
    }

    private static IPipe getTakeWhilePipe(String args) throws ClientErrorException {
        return new TakeWhilePipe(args);
    }
}
