package Server;

import algorithms.mazeGenerators.*;
import algorithms.search.BestFirstSearch;
import algorithms.search.BreadthFirstSearch;
import algorithms.search.DepthFirstSearch;
import algorithms.search.ISearchingAlgorithm;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Singleton Configurations file class, getters for config.properties for server information.
 */
public class Configurations {
    private static Configurations singleInstance = null;

    private Configurations()
    {

    }

    public static Configurations getInstance()
    {
        if(singleInstance==null)
        {
            singleInstance = new Configurations();
        }
        return singleInstance;
    }

    public int getThreadPoolSize()
    {
        // 1 by default if null in config
        int n = 1;
        try (InputStream input = Server.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            n = Integer.parseInt(prop.getProperty("threadPoolSize"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return n;
    }
    public void setThreadPoolSize(int n)
    {
        if(n<1)
            n=1;

        try (OutputStream output = new FileOutputStream("config.properties")) {
            Properties prop = new Properties();
            prop.setProperty("threadPoolSize", String.valueOf(n));
            prop.store(output, null);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public IMazeGenerator getMazeGenerator()
    {
        IMazeGenerator generator = null;
        try (InputStream input = Server.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            String genString = prop.getProperty("mazeGeneratingAlgorithm");
            //if wrong name, return an "My" maze generator
            if(genString.equals("EmptyMazeGenerator"))
            {
                generator = new EmptyMazeGenerator();

            }else if(genString.equals("SimpleMazeGenerator"))
            {
                generator = new SimpleMazeGenerator();
            }else{
                generator = new MyMazeGenerator();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return generator;
    }
    public void setMazeGenerator(String str)
    {
        try (OutputStream output = new FileOutputStream("config.properties")) {
            Properties prop = new Properties();
            prop.setProperty("mazeGeneratingAlgorithm", str);
            prop.store(output, null);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public ISearchingAlgorithm getSearchingAlgorithm()
    {
        ISearchingAlgorithm algo = null;
        try (InputStream input = Server.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            String genString = prop.getProperty("mazeSearchingAlgorithm");

            //if wrong name, return BestFS
            if(genString.equals("DepthFirstSearch"))
            {
                algo = new DepthFirstSearch();

            }else if(genString.equals("BreadthFirstSearch"))
            {
                algo = new BreadthFirstSearch();
            }else{
                algo = new BestFirstSearch();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return algo;
    }
    public void setSearchingAlgorithm(String str)
    {
        try (OutputStream output = new FileOutputStream("config.properties")) {
            Properties prop = new Properties();
            prop.setProperty("mazeSearchingAlgorithm", str);
            prop.store(output, null);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}
