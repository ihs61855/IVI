package com.example.hansol.sosohanhan.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.example.hansol.sosohanhan.R;
import com.example.hansol.sosohanhan.model.AppInfo;
import com.example.hansol.sosohanhan.utility.SocketUtils;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;



public class MainActivity extends DaggerAppCompatActivity implements View.OnClickListener{

    @Inject SocketUtils _socket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button videobt = findViewById(R.id.videobt);
        videobt.setOnClickListener(this);
        Button audiobt = findViewById(R.id.audiobt);
        audiobt.setOnClickListener(this);
        Button picturebt = findViewById(R.id.picturebt);
        picturebt.setOnClickListener(this);

        //Connect
        _socket.connect();
        try {
            JSONObject msg = _socket.receiveMessage();
            AppInfo.id = msg.get("id").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.videobt:
                AppInfo.work = "Video";
                startActivity(new Intent(MainActivity.this, ListActivity.class));
                break;
            case R.id.audiobt :
                AppInfo.work = "Audio";
                startActivity(new Intent(MainActivity.this, ListActivity.class));
                break;
            case R.id.picturebt :
                AppInfo.work = "Picture";
                startActivity(new Intent(MainActivity.this, ListActivity.class));
                break;
        }
    }
}
