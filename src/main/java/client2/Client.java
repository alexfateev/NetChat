package client2;

import server.Message;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

public class Client {

    private String host = "localhost";
    private int port = 4444;
    private Thread threadListener;
    private Thread threadWriter;
    private Socket clientSocket;

    public Client() {
        try {
            loadProperties();

            clientSocket = new Socket(host, port);
            threadWriter = new Thread(writer);
            threadListener = new Thread(listener);

            threadWriter.start();
            threadListener.start();
        } catch (IOException e) {
            System.out.println("Ошибка подключения к серверу. " + e.getMessage());
        }

    }

    Runnable writer = () -> {
        Scanner scanner = new Scanner(System.in, "cp866");
        try {
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            while (!Thread.currentThread().isInterrupted()) {
                if (scanner.hasNextLine())
                    out.writeObject(new Message(scanner.nextLine()));
            }
        } catch (IOException e) {
            System.out.println("Ошибка передачи сообщения серверу. " + e.getMessage());
        }
    };

    Runnable listener = () -> {
        try {
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            Message message;
            while (!Thread.currentThread().isInterrupted()) {
                message = (Message) in.readObject();
                if (message.getMessage().equals("/exit")) {
                    System.exit(0);
                }
                System.out.println(message.getText());
            }
        } catch (IOException e) {
            System.out.println("Упс! Сервер куда-то пропал.");
            System.exit(0);
        } catch (ClassNotFoundException e) {
            System.out.println("Ошибка получения данных с сервера. " + e.getMessage());
        }

    };

    public void loadProperties() {
        Properties props = new Properties();
        try {
            if (Files.exists(Paths.get("config_client.ini"))) {
                props.load(new FileInputStream(new File("config_client.ini")));
                host = props.getProperty("HOST", "localhost");
                port = Integer.parseInt(props.getProperty("PORT", "4444"));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void main(String[] args) {
        new Client();
    }
}
