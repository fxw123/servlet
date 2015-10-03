package com.example.rk.servlettest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTextViewName;
    private TextView mTextViewPassWord;
    private Button mButtonSignin;
    private Button mButtonSignup;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTextViewName= (EditText) findViewById(R.id.user_name);
        mTextViewPassWord= (EditText) findViewById(R.id.psaa_word);
        mTextView= (TextView) findViewById(R.id.out);
        mButtonSignin= (Button) findViewById(R.id.signin);
        mButtonSignup= (Button) findViewById(R.id.signup);
        mButtonSignin.setOnClickListener(this);
        mButtonSignup.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    String action;
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.signin:action="signin";break;
            case R.id.signup:action="signup";break;

        }
        final String userName=  mTextViewName.getText().toString();
        final String passWord=  mTextViewPassWord.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedReader bufferedReader;
                    String path="http://192.168.191.1:8080/myhttp/Login?name="
                            +userName+"&password="+passWord+"&action="+action;
                    URL url=new URL(path);
                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);

                    if (connection.getResponseCode()==200){
                        bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        final StringBuilder stringBuilder=new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine())!= null)
                        {
                            stringBuilder.append(line);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTextView.setText(stringBuilder.toString());
                            }
                        });

                    }

                }  catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
