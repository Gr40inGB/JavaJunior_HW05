package org.gr40in.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConsoleServer implements Server {
    private final ServerSocket serverSocket;

    public ConsoleServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("Подключен новый клиент!");
                ClientManagerImp clientManager = new ClientManagerImp(socket);
                Thread thread = new Thread(clientManager);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            stopServer();
        }
    }

    @Override
    public void stopServer() {
        try{
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
