package server;

import client2.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ClientTest {
    @Test
    public void serverNotConnection() {
        Assertions.assertThrows(IOException.class, () -> new Client());
    }


}
