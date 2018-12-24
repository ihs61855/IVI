package com.example.hansol.sosohanhan.utility;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.hansol.sosohanhan.controller.ConnectionProcessor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SocketUtils implements ConnectionProcessor{
    private final String _ip = "192.168.0.4";
    private final int _port = 3000;
    private Socket _socketServer = null;
    private OutputStreamWriter _networkWriter = null;
    private InputStreamReader _networkReader= null;
    private LinkedList<String> _receiveQueue = new LinkedList<>();
    private LinkedList<String> _sendQueue = new LinkedList<>();
    private Disposable disposable = null;

    public void connect(){
        Observable<String> connectRX;
        connectRX = Observable.just("connect")
                .map(NotUsed -> {
                    //Socket Creat
                    _socketServer = new Socket(_ip,_port);
                    _networkWriter = new OutputStreamWriter(_socketServer.getOutputStream());
                    _networkReader = new InputStreamReader(_socketServer.getInputStream());

                    //Start Query send
                    JSONObject obj = new JSONObject();
                    obj.put("Query", "RequestID");
                    _networkWriter.write(String.valueOf(obj));
                    _networkWriter.flush();
                    return "Connect success";})
                .retry((retryCnt, e) ->{
                    Log.d("Debug","Connect Retry Cnt " + retryCnt);
                    return true;})
                .subscribeOn(Schedulers.io());
        disposable = connectRX.subscribe( message -> {
            Log.d("Debug",message);
            Thread.sleep(1000L);
            Rx();});
    }

    private void Rx(){
        Observable<String> source;
        source = Observable.interval(100L, TimeUnit.MILLISECONDS)
                .map(data ->{
                    char[] b = new char[1024];
                    return new String(b, 0, _networkReader.read(b)); })
                .subscribeOn(Schedulers.newThread());
        disposable = source.subscribe(data -> {_receiveQueue.push(data); System.out.println("Recive Data : " + data);},
                throwable ->{disconnect(); connect();});
    }

    public void disconnect(){
        disposable.dispose();
        try {
            _socketServer.close();
            _networkReader.close();
            _networkWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject receiveMessage() throws JSONException {
        while(_receiveQueue.isEmpty()){}
        String recvmessage = _receiveQueue.getFirst();
        _receiveQueue.pop();
        JSONObject temp;
        temp = new JSONObject(recvmessage);
        return temp;
    }


    @Override
    @SuppressLint("CheckResult")
    public void sendMessage(JSONObject sendmessage){
        _sendQueue.push(String.valueOf(sendmessage));
        Observable.just("sendmessage")
                .map( data ->{
                    while(!_sendQueue.isEmpty()) {
                        _networkWriter.write(_sendQueue.getFirst());
                        _networkWriter.flush();
                        _sendQueue.pop();
                    }
                    return data; })
                .subscribeOn(Schedulers.io())
                .subscribe(System.out::println,
                        Throwable::printStackTrace);
    }
}
