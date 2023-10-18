package server;

import java.io.Serializable;

public class Message implements Serializable {
    private String userName;
    private String message;

    public Message(String userName, String message) {
        this.userName = userName;
        this.message = message;
    }

    public Message(String message) {
        this("[Сервер]", message);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public String getText() {
        return userName + ": " + message;
    }


}
