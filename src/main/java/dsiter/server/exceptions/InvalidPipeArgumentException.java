package dsiter.server.exceptions;

public class InvalidPipeArgumentException extends ClientErrorException {

    public InvalidPipeArgumentException(String pipeName, String args) {
        super("Invalid argument for pipe \"" + pipeName + "\": \"" + args + "\"");
    }
}
