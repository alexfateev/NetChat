package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class UserHandler extends Thread {
    private Socket clientSocket;
    private String userName;
    private boolean registered = false;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            Message message;

            while (!registered) {
                sendMessage(new Message("Для регистрации введите имя: "));
                message = (Message) in.readObject();
                userName = message.getMessage();
                Server.userRegistered(this);
            }

            while (!Thread.currentThread().isInterrupted()) {
                message = (Message) in.readObject();
                message.setUserName(userName);
                if (!message.getMessage().trim().equals("")) {
                    if (message.getMessage().startsWith("/")) {
                        switch (message.getMessage()) {
                            case "/help":
                                showHelp();
                                break;
                            case "/exit":
                                sendMessage(new Message("/exit"));
                                break;
                            case "/ulist":
                                Server.showAllClient(this);
                                break;
                            default:
                                sendMessage(new Message("Команда не распознана"));
                        }
                    } else {
                        Server.sendToAllClient(message, this);
                    }
                }
            }


        } catch (SocketException e) {
            try {
                clientSocket.close();
            } catch (IOException ex) {
                e.printStackTrace();
            }
            if (registered) Server.userUnregistered(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public UserHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        start();
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
        sendMessage(new Message("Добро пожаловать, " + userName
                + "! Будьте вежливы с другими участниками чата."));
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
        } catch (IOException e) {
            System.out.println("Ошибка отправки сообщения пользователю. " + e.getMessage());
        }
    }

    public void showHelp() {
        Message message = new MessageBuilder()
                .messageAppend("Список всех доступных комманд\n")
                .messageAppend("/help - Отобразить все доступные команды\n")
                .messageAppend("/exit - выход из чата. Завершение работы приложения\n")
                .messageAppend("/ulist - Показать всех пользователей в чате\n")
                .build();
        sendMessage(message);
    }
}
