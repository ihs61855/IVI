package com.example.hansol.sosohanhan.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;


import com.example.hansol.sosohanhan.R;
import com.example.hansol.sosohanhan.adapter.MyAdapter;
import com.example.hansol.sosohanhan.adapter.OnItemClick;
import com.example.hansol.sosohanhan.model.AppInfo;
import com.example.hansol.sosohanhan.model.EntertainmentItem;
import com.example.hansol.sosohanhan.utility.FTPUtils;
import com.example.hansol.sosohanhan.utility.SocketUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import javax.inject.Inject;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;


public class ListActivity extends DaggerAppCompatActivity implements OnItemClick,View.OnClickListener {
    final static int REQUEST_TAKE_GALLERY = 1;

    @Inject EntertainmentItem mItem;
    @Inject SocketUtils mSocketUtils;
    RecyclerView mRecyclerView;
    MyAdapter mMyAdapter;
    Button mback, madd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mRecyclerView = findViewById(R.id.recyclerView);
        mback = findViewById(R.id.listback); mback.setOnClickListener(this);
        madd = findViewById(R.id.listadd); madd.setOnClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMyAdapter = new MyAdapter(new ArrayList<>(),this);
        mRecyclerView.setAdapter(mMyAdapter);
        LoadList();
    }

    public void LoadList(){
        JSONObject msg = new JSONObject();
        try {
            msg.put("Query", "RequestList");
            msg.put("Kind", AppInfo.work);
            mSocketUtils.sendMessage(msg);
            msg = mSocketUtils.receiveMessage();
            ArrayList<String> list = new ArrayList<>(Arrays.asList(msg.get("List").toString().split(",")));
            mItem.setList(list); mMyAdapter.setData(mItem.getList());
            mMyAdapter.notifyDataSetChanged();
        } catch (JSONException e) { e.printStackTrace(); }
    }

    @Override
    public void onRecycleItemClick(int position, int type) {
        if(type == 0){ // delete
            JSONObject msg = new JSONObject();
            try {
                msg.put("Query", "RequestStart");
                msg.put("Kind", AppInfo.work);
                msg.put("Title", mItem.getIndex(position));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LoadList();
        }
        else if(type == 1){ // play
            Intent nextIntent = new Intent(ListActivity.this, PopupActivity.class);
            nextIntent.putExtra("title", mItem.getIndex(position));
            startActivity(nextIntent);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.listadd:
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_TAKE_GALLERY);
                    }
                }

                Intent intent = new Intent();
                switch (AppInfo.work) {
                    case "Video":
                        intent.setType("video/*");
                        break;
                    case "Audio":
                        intent.setType("audio/*");
                        break;
                    case "Picture":
                        intent.setType("image/*");
                        break;
                }
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY);
            break;
            case R.id.listback:
               finish();
               break;
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY) {

                ProgressDialog asyncDialog = new ProgressDialog(ListActivity.this);
                asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                asyncDialog.setMessage("전송중입니다..");
                asyncDialog.show();

                Observable.just("FTP Send")
                        .map(NotUsed ->{
                            FTPUtils ftpUtils = new FTPUtils();
                            String _ip = "192.168.0.25";
                            int _port = 21;
                            Uri uri = null;
                            if (data != null) { uri = data.getData(); }
                            ftpUtils.ftpConnect(_ip, "user", "12345", _port);
                            ftpUtils.ftpUploadFile(getPath(uri),getFileName(uri),AppInfo.work+"/");
                            ftpUtils.ftpDisconnect();
                            return NotUsed;})
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(NotUsed ->{
                            asyncDialog.dismiss();
                            runOnUiThread(() -> {
                                asyncDialog.dismiss();
                                LoadList();
                            });
                        });
            }
        }
    }

    public String getPath(Uri uri) {
        String[] proj = null;
        switch (AppInfo.work) {
            case "Video":
                proj = new String[]{MediaStore.Video.Media.DATA};
                break;
            case "Audio":
                proj = new String[]{MediaStore.Audio.Media.DATA};
                break;
            case "Picture":
                proj = new String[]{MediaStore.Images.Media.DATA};
                break;
        }
        @SuppressLint("Recycle") Cursor c = getContentResolver().query(uri, proj, null, null, null);
        int index = 0;
        String path = null;
        if(c != null) {
            switch (AppInfo.work) {
                case "Video":
                    index = c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                    break;
                case "Audio":
                    index = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                    break;
                case "Picture":
                    index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    break;
            }
            c.moveToFirst();
            path = c.getString(index);
        }

        return path;
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (Objects.equals(uri.getScheme(), "content")) {
            try (
                    Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }
}
