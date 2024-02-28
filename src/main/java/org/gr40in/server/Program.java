package org.gr40in.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Program {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        Server server = new ConsoleServer(serverSocket);
        server.startServer();
    }

}
