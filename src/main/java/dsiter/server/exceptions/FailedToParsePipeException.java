package dsiter.server.exceptions;

public class FailedToParsePipeException extends ClientErrorException {

    public FailedToParsePipeException(String pipeStr) {
        super("Failed to parse pipe: \"" + pipeStr + "\"");
    }
}
