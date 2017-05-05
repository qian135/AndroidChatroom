package com.chatroom.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerHandleThread extends Thread {

    static List<ServerHandleThread> mServerHandleThreadList = new ArrayList<>();

    private Socket mSocket;
    private BufferedReader mBufferedReader;
    private PrintWriter mPrintWriter;
    private String mClientNickname;

    public ServerHandleThread(Socket socket) {
        mSocket = socket;
        start();
    }

    @Override
    public void run() {
        try {
            mBufferedReader = new BufferedReader(
                    new InputStreamReader(mSocket.getInputStream()));
            mPrintWriter = new PrintWriter(
                    new OutputStreamWriter(mSocket.getOutputStream()));
            mPrintWriter.println("请输入昵称！");
            mPrintWriter.flush();
            mClientNickname = mBufferedReader.readLine();
            mServerHandleThreadList.add(this);
            sendMsgToAll(mClientNickname + "加入聊天室");
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                String s = mBufferedReader.readLine();
                if (s.equals("bye")) {
                    sendMsgToAll(mClientNickname + "退出了聊天室");
//                    mPrintWriter.println(s);
//                    mPrintWriter.flush();
                    mBufferedReader.close();
                    mPrintWriter.close();
                    mSocket.close();
                    break;
                }
                for (int i = 0;i < mServerHandleThreadList.size();i++) {
                    if (!mServerHandleThreadList.get(i).getClientNickname().equals(
                            mClientNickname)) {
                        mServerHandleThreadList.get(i).getPrintWriter().println(
                                mClientNickname + "：" + s);
                        mServerHandleThreadList.get(i).getPrintWriter().flush();
                    } else {
                        mServerHandleThreadList.get(i).getPrintWriter().println("Me：" + s);
                        mServerHandleThreadList.get(i).getPrintWriter().flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void sendMsgToAll(final String msg) {
        MainActivity.getmTextView().post(new Runnable() {
            @Override
            public void run() {
                MainActivity.getmTextView().append(msg + "\n");
            }
        });
        for (int i = 0;i < mServerHandleThreadList.size();i++) {
            mServerHandleThreadList.get(i).getPrintWriter().println(msg);
            mServerHandleThreadList.get(i).getPrintWriter().flush();
        }
    }

    public PrintWriter getPrintWriter() {
        return mPrintWriter;
    }

    public String getClientNickname() {
        return mClientNickname;
    }
}
