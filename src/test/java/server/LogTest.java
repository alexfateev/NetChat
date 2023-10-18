package server;

import log.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LogTest {

    private final String filename = "history.log";

    @Test
    public void logTest() {
        Log.getInstance();
        Assertions.assertEquals(true, Files.exists(Paths.get(filename)));
    }

    @Test
    public void readLog() throws IOException {
        long expected;
        BufferedReader bf = new BufferedReader(new FileReader(filename));
        expected = bf.lines().count();
        bf.close();
        bf = new BufferedReader(new FileReader(filename));
        Log.getInstance().log("Test");
        Assertions.assertEquals(expected + 1, bf.lines().count());

    }

}
