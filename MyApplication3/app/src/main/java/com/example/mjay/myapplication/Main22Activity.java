package com.example.mjay.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Main22Activity extends AppCompatActivity {

    public ProgressBar p2;
    public String message;
    public Socket s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main22);

        p2 = (ProgressBar) findViewById(R.id.progress_id);
        //p2.getIndeterminateDrawable().setColorFilter(Color.BLUE, android.graphics.PorterDuff.Mode.MULTIPLY);

        s = SocketSer.getSocket();
        getConnected();
    }

    void getConnected ()
    {
        new MyTask().execute();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            try
            {
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                dout.writeUTF("~!@#$%i@#am@#closing~!@#$%");
                s.close();
                Toast.makeText(Main22Activity.this, "Connection Closed", Toast.LENGTH_SHORT).show();
            }
            catch (IOException e)
            {
                Toast.makeText(Main22Activity.this, "Unable to close Connection", Toast.LENGTH_SHORT).show();
            }
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    class MyTask extends AsyncTask
    {

        @Override
        protected Object doInBackground(Object[] params) {
            message = null;
            try
            {
                DataInputStream din = new DataInputStream(s.getInputStream());

                message = din.readUTF();
                if (message.equals("!@#$a!@n!@ot!@#$"))
                {
                    Intent i=new Intent(Main22Activity.this,Main2Activity.class);
                    finish();
                    startActivity(i);
                }
            }
            catch (Exception e)
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Main22Activity.this, "Error in communicating with Server\nExiting Now", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
            return null;
        }
    }
}
