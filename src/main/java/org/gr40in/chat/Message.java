package org.gr40in.chat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import java.io.*;

public class Message implements Externalizable {
    private User senderClient;
    private List<String> privateGroupNames;
    private LocalDateTime time;
    private String data;
    private boolean systemMessage;

    public Message() {
    }

    public Message(User senderClient, List<String> privateGroup, LocalDateTime time, String data) {
        this.systemMessage = false;
        this.senderClient = senderClient;
        this.privateGroupNames = privateGroup;
        this.time = time;
        this.data = data;
    }

    public Message(User senderClient, boolean systemMessage, List<String> privateGroup, LocalDateTime time, String data) {
        this.senderClient = senderClient;
        this.systemMessage = systemMessage;
        this.privateGroupNames = privateGroup;
        this.time = time;
        this.data = data;
    }

    public User getSenderClient() {
        return senderClient;
    }

    public void setSenderClient(User senderClient) {
        this.senderClient = senderClient;
    }

    public List<String> getPrivateGroupNames() {
        return privateGroupNames;
    }

    public void setPrivateGroupNames(List<String> privateGroupNames) {
        this.privateGroupNames = privateGroupNames;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Message{" + "senderClient=" + senderClient + ", privateGroup=" + privateGroupNames + ", time=" + time + ", data='" + data + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message message)) return false;
        return Objects.equals(senderClient, message.senderClient) && Objects.equals(privateGroupNames, message.privateGroupNames) && Objects.equals(time, message.time) && Objects.equals(data, message.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderClient, privateGroupNames, time, data);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(systemMessage);
        out.writeObject(senderClient);
        out.writeObject(privateGroupNames);
        out.writeObject(time);
        out.writeObject(data);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.systemMessage = (Boolean) in.readObject();
        this.senderClient = (User) in.readObject();
        this.privateGroupNames = (List<String>) in.readObject();
        this.time = (LocalDateTime) in.readObject();
        this.data = (String) in.readObject();
    }

    public boolean isSystemMessage() {
        return systemMessage;
    }

    public void setSystemMessage(boolean systemMessage) {
        this.systemMessage = systemMessage;
    }
}
