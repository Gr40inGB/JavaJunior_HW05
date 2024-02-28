package org.gr40in.client;

import org.gr40in.chat.Message;
import org.gr40in.chat.User;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ConsoleClientImp implements Client {
    private transient Socket socket;
    private transient ObjectInputStream objectInputStream;
    private transient ObjectOutputStream objectOutputStream;
    private User user;

    public ConsoleClientImp() {
    }

    public ConsoleClientImp(Socket socket, User user) {
        this.socket = socket;
        this.user = user;

        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            //TODO login

        } catch (IOException e) {
            close();
        }
    }

    @Override
    public void listenMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message;
                while (socket.isConnected()) {
                    try {
                        message = (Message) objectInputStream.readObject();
                        if (message.isSystemMessage()) {
                            System.out.println(
                                    " " + message.getData()
                            );
                        } else {
                            System.out.println(
                                    message.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " " + message.getSenderClient() + ": " + message.getData());
                        }
                    } catch (Exception e) {
                        close();
                    }
                }
            }
        }).start();
    }

    @Override
    public void sendMessage() {
        try {
            objectOutputStream.writeObject(new Message(user, true, new ArrayList<>(), LocalDateTime.now(), user + " присоединился к чату!"));
            objectOutputStream.flush();
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                boolean systemMessage = false;
                List<String> privatList = new ArrayList<>();
                String message = scanner.nextLine();
                StringBuilder data = new StringBuilder(message);

                if (message.equals("@list")) systemMessage = true;

                if (message.startsWith("@only")) {
                    data = new StringBuilder("(private message) ");
                    var allWords = message.split(" ");
                    for (String word : allWords) {
                        if (word.equals("@only")) continue;
                        if (word.startsWith("@")) {
                            privatList.add(word.substring(1));
                        } else data.append(word).append(" ");
                    }
                }

                objectOutputStream.writeObject(new Message(user, systemMessage, privatList, LocalDateTime.now(), data.toString()));
                objectOutputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            close();
        }
    }

    @Override
    public void close() {
        try {
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(user);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.user = (User) in.readObject();
    }
}
