package Helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileIO {

    public static void writeData(String path, String data, boolean doAppend) throws IOException
    {
        FileWriter writer = new FileWriter(path);
        if(doAppend)
        {
            writer.append(data);
            return;
        }
        writer.write(data);
        writer.close();
    }

    public static String readData(String path) throws FileNotFoundException
    {
        Scanner scan = new Scanner(new File(path));
        String readData = "";
        while(scan.hasNextLine())
        {
            readData += scan.nextLine() + "\n";
        }
        scan.close();
        return readData;
    }

    public static boolean fileExists(String path)
    {
        return new File(path).exists();
    }

    public static void createEmptyFile(String path) throws IOException
    {
        if(!FileIO.fileExists(path))
        {
            FileIO.writeData(path,"",false);
        }
    }
}
