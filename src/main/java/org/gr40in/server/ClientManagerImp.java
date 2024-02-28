package org.gr40in.server;

import org.gr40in.chat.Message;
import org.gr40in.chat.User;
import org.gr40in.client.Client;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ClientManagerImp implements ClientManager {

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private User user;
    public static ArrayList<ClientManagerImp> clients = new ArrayList<>();

    public ClientManagerImp(Socket socket) {
        try {
            this.socket = socket;
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            clients.add(this);
            //TODO: ...
            user = ((Message) objectInputStream.readObject()).getSenderClient();
            System.out.println(user + " подключился к чату.");
            broadcastMessage(new Message(user, true, null, LocalDateTime.now(),
                    " Server: " + user + " подключился к чату."));
            privateMessage(new Message(user, true, null, LocalDateTime.now(),
                    " Server: Добро пожаловать!\n" +
                            "@list - список подключенных к чату\n" +
                            "@only + @имя пользователя (можно несколько) - личное сообщение"));
        } catch (Exception e) {
            e.printStackTrace();
            close();
        }
    }


    public void close() {
        // Удаление клиента из коллекции
        removeClient();
        try {
            // Завершаем работу буфера на чтение данных
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            // Завершаем работу буфера для записи данных
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            // Закрытие соединения с клиентским сокетом
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Удаление клиента из коллекции
     */
    private void removeClient() {
        broadcastMessage(new Message(user, true, null, LocalDateTime.now(), " Server: " + user + " покинул чат."));
        clients.remove(this);
        System.out.println(user + " покинул чат.");
    }

    /**
     * Отправка сообщения всем слушателям
     *
     * @param message сообщение
     */
    private void broadcastMessage(Message message) {
        for (ClientManagerImp client : clients) {
            try {
                if (!client.equals(this) && message != null) {
                    //if (!client.name.equals(name) && message != null) {
                    client.objectOutputStream.writeObject(message);
                    client.objectOutputStream.flush();
                }
            } catch (Exception e) {
                close();
            }
        }
    }

    @Override
    public void run() {
        Message messageFromClient;
        while (!socket.isClosed()) {
            try {
                // Чтение данных
                messageFromClient = (Message) objectInputStream.readObject();
                if (messageFromClient.getData().equals("@list")) {
                    privateMessage(new Message(user, true, null,LocalDateTime.now(), clients.stream().map(c -> c.user.toString()).collect(Collectors.joining(", "))));
                } else {
                    // Отправка данных всем слушателям
                    List<String> privateList = messageFromClient.getPrivateGroupNames();
                    if (privateList != null && !privateList.isEmpty()) privateMessage(messageFromClient, privateList);
                    else broadcastMessage(messageFromClient);
                }
            } catch (Exception e) {
                close();
                break;
            }
        }
    }

    private void privateMessage(Message message, List<String> privatList) {
        for (ClientManagerImp client : clients) {
            try {
                if (!client.equals(this) && message != null) {
                    if (privatList.contains(client.user.toString())) {
                        client.objectOutputStream.writeObject(message);
                        client.objectOutputStream.flush();
                    }
                }
            } catch (Exception e) {
                close();
            }
        }
    }

    private void privateMessage(Message message) {
        try {
            if (message != null) {
                objectOutputStream.writeObject(message);
                objectOutputStream.flush();
            }
        } catch (Exception e) {
            close();
        }

    }


}