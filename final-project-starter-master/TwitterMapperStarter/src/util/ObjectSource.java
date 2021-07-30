package util;

import java.io.*;

/**
 * Read objects from a file
 */
public class ObjectSource {
    private ObjectInputStream inputStream;

    public ObjectSource(String filename) {
        File file = new File(filename);
        try {
            inputStream = new ObjectInputStream(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object read() {
        try {
            return inputStream.readObject();
        } catch (EOFException ignored) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
