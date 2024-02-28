package org.gr40in.client;

import org.gr40in.chat.User;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите своё имя: ");
        String name = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        InetAddress address = InetAddress.getLocalHost();
        Socket socket = new Socket(address, 9999);
        Client client = new ConsoleClientImp(socket, new User(name, password));

        InetAddress inetAddress = socket.getInetAddress();

//        System.out.println("InetAddress: " + inetAddress);
//        String remoteIp = inetAddress.getHostAddress();
//        System.out.println("Remote IP: " + remoteIp);
//        System.out.println("LocalPort:" + socket.getLocalPort());


        client.listenMessage();
        client.sendMessage();
    }

}
