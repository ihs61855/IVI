package com.example.hansol.sosohanhan.view;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.hansol.sosohanhan.R;
import com.example.hansol.sosohanhan.model.AppInfo;
import com.example.hansol.sosohanhan.utility.SocketUtils;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import dagger.android.DaggerActivity;

public class
PopupActivity extends DaggerActivity implements View.OnClickListener{

    @Inject SocketUtils socketUtils;

    Button endbt;
    ImageButton  paly, stop;
    ConstraintLayout controlrayout;
    Boolean endflag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.control_popup);

        //Listener
        endbt = findViewById(R.id.finishbt); endbt.setOnClickListener(this);
        controlrayout = findViewById(R.id.controlrayout); controlrayout.setOnClickListener(this);
        paly = findViewById(R.id.play); paly.setOnClickListener(this);
        stop = findViewById(R.id.stop); stop.setOnClickListener(this);

        //사진일 경우 컨트롤러 부분을 보이지 않게 한다.
        if(AppInfo.work.equals("Picture")){ controlrayout.setVisibility(View.GONE);}

        //Start
        JSONObject msg = new JSONObject();
        try {
            msg.put("Query", "RequestStart");
            msg.put("Kind", AppInfo.work);
            msg.put("Title", getIntent().getStringExtra("title"));
        } catch (JSONException e) { e.printStackTrace(); }
        socketUtils.sendMessage(msg);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!endflag) {
            JSONObject msg = new JSONObject();
            try {
                msg.put("Query", "RequestStop");
                msg.put("Kind", AppInfo.work);
                socketUtils.sendMessage(msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() { return; }

    @Override
    public void onClick(View v) {
        JSONObject msg = new JSONObject();
        try {
            switch (v.getId()) {
                case R.id.finishbt:
                    finish();
                    break;
                case R.id.stop:
                    endflag =true;
                    msg.put("Query", "RequestPause");
                    msg.put("Kind", AppInfo.work);
                    socketUtils.sendMessage(msg);
                    break;

                case R.id.play:
                    msg.put("Query", "RequestPlay");
                    msg.put("Kind", AppInfo.work);
                    socketUtils.sendMessage(msg);
                    break;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}