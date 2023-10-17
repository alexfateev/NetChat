import java.io.IOException;
import java.net.ServerSocket;

public class Test {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
