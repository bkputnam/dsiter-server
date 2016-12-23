package dsiter.server.exceptions;

public class ClientErrorException extends Exception {
    protected int statusCode = 400;

    public ClientErrorException(String msg) {
        super(msg);
    }

    public int getStatusCode() {
        return statusCode;
    }
}
