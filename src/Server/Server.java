package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Multithreaded Server Class using Strategy Design Pattern
 * Thread for each start, each thread has a thread pool to handle clients
 */
public class Server {
    private int port;
    private int listeningIntervalMS;
    private IServerStrategy strategy;
    private ExecutorService threadPool;
    private volatile boolean stop;

    public Server(int port, int listenInterval, IServerStrategy strategy)
    {
        this.port=port;
        this.listeningIntervalMS=listenInterval;
        this.strategy=strategy;
    }
    public void start(){
       new Thread(this::run).start();
    }

    public void run()
    {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(listeningIntervalMS);
            Configurations config = Configurations.getInstance();
            this.threadPool = Executors.newFixedThreadPool(config.getThreadPoolSize());
            while (!stop) {
                try{
                    Socket clientSocket = serverSocket.accept();
                    this.threadPool.submit(new Thread( ()->{
                        handleClient(clientSocket);
                         }  ));

                } catch (SocketTimeoutException e){
                    //e.printStackTrace();
                }
            }
            serverSocket.close();
            threadPool.shutdownNow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try {
            strategy.applyStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
            clientSocket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void stop(){
        stop = true;
    }


}
