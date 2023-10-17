package server;

public class MessageBuilder {
    private String userName;
    private StringBuilder message = new StringBuilder();

    public MessageBuilder setName(String name) {
        userName = name;
        return this;
    }

    public MessageBuilder setMessageText(String message) {
        this.message.append(message);
        return this;
    }

    public MessageBuilder messageAppend(String message) {
        this.message.append(message);
        return this;
    }

    public Message build() {
        return new Message(userName, message.toString());
    }
}
