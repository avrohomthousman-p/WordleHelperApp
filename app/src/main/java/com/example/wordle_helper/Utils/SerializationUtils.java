package com.example.wordle_helper.Utils;

import java.io.*;

/**
 * Provides utility methods for serializing and deserializing objects.
 */
public class SerializationUtils {

    /**
     * Deserializes an object that was previously written to a file.
     *
     * @return the object desierialized from the file.
     */
    public static Object deserializeObject(final String filepath){
        Object item = null;
        try{
            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            item = in.readObject();
            in.close();
            fileIn.close();

        }
        catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }

        return item;
    }


    public static void serializeObject(Object o, final String filepath){
        try {
            FileOutputStream fileOut = new FileOutputStream(filepath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(o);
            out.close();
            fileOut.close();

        } catch (IOException i) {
            i.printStackTrace();
        }

    }
}
