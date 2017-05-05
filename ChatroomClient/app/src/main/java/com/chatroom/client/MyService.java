package com.chatroom.client;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class MyService extends Service {

    public static boolean flag;
    public static BufferedReader mBufferedReader;
    public static PrintWriter mPrintWriter;
    public static Socket mSocket;

    private String mServerIP = "192.168.1.103";

    @Override
    public void onCreate() {
        super.onCreate();
        mServerIP = MainActivity.mServerIPEditText.getText().toString();
        if (mServerIP.equals("")) {
            Toast.makeText(getApplicationContext(), "Please Input Server IP", Toast.LENGTH_LONG).show();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mSocket = new Socket(mServerIP, 4710);
                        mBufferedReader = new BufferedReader(
                                new InputStreamReader(mSocket.getInputStream()));
                        mPrintWriter = new PrintWriter(
                                new OutputStreamWriter(mSocket.getOutputStream()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    flag = true;
                    MainActivity.mTextView.post(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.mConnect.setEnabled(false);
                        }
                    });
                    while (flag) {
                        try {
                            final String s = mBufferedReader.readLine();
                            MainActivity.mTextView.post(new Runnable() {
                                @Override
                                public void run() {
                                    MainActivity.mTextView.append(s + "\n");
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
