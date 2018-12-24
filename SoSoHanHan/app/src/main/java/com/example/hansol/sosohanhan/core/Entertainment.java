package com.example.hansol.sosohanhan.core;

import java.util.ArrayList;
import java.util.Observable;

public class Entertainment{
    ArrayList<String> mList;

    public Entertainment(){
        mList = new ArrayList<String>();
    }

    public ArrayList<String> getList(){
        return mList;
    }

    public void setList(ArrayList<String> list){
        mList.clear();
        mList = list;
    }

    public String getIndex(int index){
        return mList.get(index);
    }

    public void setIndex(int index, String string){
        mList.set(index,string);
    }

    public int getcount(){
        return mList.size();
    }
}