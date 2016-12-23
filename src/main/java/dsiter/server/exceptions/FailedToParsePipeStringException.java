package dsiter.server.exceptions;

public class FailedToParsePipeStringException extends ClientErrorException {
    public FailedToParsePipeStringException(String pipeStr) {
        super("Failed to parse pipe string: \"" + pipeStr + "\"");
    }
}
