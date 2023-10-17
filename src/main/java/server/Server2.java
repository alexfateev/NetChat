package server;

import log.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;

public class Server2 {

    private static int port = 4444;
    private static ConcurrentHashMap<String, UserHandler2> userList = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        startServer(port);
    }

    public static void userRegistered(UserHandler2 userHandler) {
        if (userList.containsKey(userHandler.getUserName())) {
            userHandler.sendMessage(new Message("Пользователь с таким именем уже зарегистрирован."));
        } else if (userHandler.getUserName().contains(" ")) {
            userHandler.sendMessage(new Message("Имя содержит недопустимые символы"));
        } else {
            Server2.sendToAllClient(new Message(userHandler.getUserName()
                    + " - присоединяется к чату"));
            userList.put(userHandler.getUserName(), userHandler);
            userHandler.setRegistered(true);
        }
    }

    public static void sendToClient(Message message, UserHandler2 from, String recipient) {
        if (userList.containsKey(recipient)) {
            userList.get(recipient).sendMessage(message);
        } else {
            from.sendMessage(new Message("Пользователь с именем " + recipient + " не найден"));
        }
    }

    public static void userUnregistered(UserHandler2 userHandler) {
        if (userList.containsKey(userHandler.getUserName())) {
            userList.remove(userHandler.getUserName(), userHandler);
            sendToAllClient(new Message(userHandler.getUserName() + " - покинул чат"));
        }
    }

    public static void sendToAllClient(Message message) {
        sendToAllClient(message, null);
    }

    public static void sendToAllClient(Message message, UserHandler2 sender) {
        if (sender == null) {
            for (UserHandler2 userHandler : userList.values()) {
                userHandler.sendMessage(message);
            }
        } else {
            if (userList.size() == 1) sender.sendMessage(new Message("В этом чате никого кроме вас нет."));
            for (UserHandler2 userHandler : userList.values()) {
                if (userHandler != sender)
                    userHandler.sendMessage(message);
            }
        }
        Log.getInstance().log(message.getText());
    }

    public static void showAllClient(UserHandler2 userHandler) {
        StringBuilder text = new StringBuilder();
        text.append("Список всех польлзовтелей:\n");
        for (String userName : userList.keySet()) {
            text.append(userName + "\n");
        }
        userHandler.sendMessage(new Message(text.toString()));
    }

    public static void startServer(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started");
            while (true) {
                new UserHandler2(serverSocket.accept());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
