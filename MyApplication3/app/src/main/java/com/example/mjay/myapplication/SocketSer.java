package com.example.mjay.myapplication;

import java.net.Socket;

/**
 * Created by MJay on 4/2/2017.
 */

public class SocketSer {
    private static Socket socket;

    public static void setSocket(Socket obj)
    {
        SocketSer.socket=obj;
    }

    public static Socket getSocket()
    {
        return SocketSer.socket;
    }
}
