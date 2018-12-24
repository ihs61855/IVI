package com.example.hansol.sosohanhan.controller;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Singleton;

@Singleton
public interface ConnectionProcessor {
    void connect();
    void sendMessage(JSONObject msg);
    JSONObject receiveMessage() throws JSONException;
    void disconnect();
}
