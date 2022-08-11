package Server;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Strategy Design Pattern
 */
public interface IServerStrategy {
    void applyStrategy(InputStream inFromClient, OutputStream outToClient);
}
