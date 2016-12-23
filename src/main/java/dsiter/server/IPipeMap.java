package dsiter.server;

import dsiter.pipe.IPipe;
import dsiter.server.exceptions.ClientErrorException;

public interface IPipeMap {
    IPipe getPipe(String pipeName, String args) throws ClientErrorException;
}
