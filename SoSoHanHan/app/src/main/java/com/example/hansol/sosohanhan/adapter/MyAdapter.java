package com.example.hansol.sosohanhan.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.hansol.sosohanhan.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<String> mList;
    private OnItemClick mCallback;

    public MyAdapter(ArrayList<String> list, OnItemClick listner) {
        mList = list;
        mCallback = listner;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ((CustomViewHolder) holder).indexvideo.setText(String.valueOf(position+1));
        ((CustomViewHolder) holder).namevideo.setText(mList.get(position));
        ((CustomViewHolder) holder).playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onRecycleItemClick(position, 1);
            }
        });
        ((CustomViewHolder) holder).deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onRecycleItemClick(position, 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView indexvideo;
        TextView namevideo;
        ImageButton playbutton;
        ImageButton deletebutton;

        public CustomViewHolder(View view) {
            super(view);
            indexvideo = view.findViewById(R.id.number);
            namevideo = view.findViewById(R.id.videoname);
            playbutton = view.findViewById(R.id.playbutton);
            deletebutton = view.findViewById(R.id.deletebutton);
        }
    }

    public void setData(ArrayList<String> arrayList){
        mList.clear();
        mList = arrayList;
        System.out.println(mList);
        notifyDataSetChanged();
    }
}
