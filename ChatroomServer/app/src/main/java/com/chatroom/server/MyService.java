package com.chatroom.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyService extends Service {

    public static ServerSocket mServerSocket;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mServerSocket = new ServerSocket(4710);
                    MainActivity.getmTextView().post(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.getmTextView().append("Server start up" + "\n");
                        }
                    });
                    while (true) {//ServerSocket关闭后直接捕获异常退出
                        Socket socket = mServerSocket.accept();
                        new ServerHandleThread(socket);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static ServerSocket getmServerSocket() {
        return mServerSocket;
    }
}
