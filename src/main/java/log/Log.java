package log;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    private static Log instance = null;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private FileWriter writer;

    public Log() {
        try {
            writer = new FileWriter("history.log", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Log getInstance() {
        if (instance == null) {
            return new Log();
        } else {
            return instance;
        }
    }

    public void log(String message) {
        try {
            writer.write(dateFormat.format(new Date()) + ": " + message + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
