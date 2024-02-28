package org.gr40in.client;

import org.gr40in.chat.User;

import java.io.Externalizable;

public interface Client extends Externalizable {
    public void sendMessage();
    public void listenMessage();
    public void close();
    public User getUser();

}
