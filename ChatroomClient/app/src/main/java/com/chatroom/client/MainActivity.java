package com.chatroom.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends Activity {

    private Intent mStartServiceIntent;

    public static Button mConnect, mSend, mClear;
    public static TextView mTextView;
    public static EditText mServerIPEditText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView);
        mServerIPEditText = (EditText) findViewById(R.id.serverIP);

        mConnect = (Button) findViewById(R.id.connect);
        mConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartServiceIntent = new Intent(MainActivity.this,MyService.class);
                startService(mStartServiceIntent);
            }
        });

        final EditText editText = (EditText) findViewById(R.id.editText);

        mSend = (Button) findViewById(R.id.send);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                editText.setText("");
                if (!s.equals("bye")) {
                    MyService.mPrintWriter.println(s);
                    MyService.mPrintWriter.flush();
                } else {
                    MyService.mPrintWriter.println(s);
                    MyService.mPrintWriter.flush();
                    MyService.flag = false;
                    try {
                        MyService.mBufferedReader.close();
                        MyService.mSocket.close();
                        MyService.mPrintWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stopService(mStartServiceIntent);
                    mConnect.setEnabled(true);
                }
            }
        });

        mClear = (Button) findViewById(R.id.clear);
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView.setText("");
            }
        });

    }

}
