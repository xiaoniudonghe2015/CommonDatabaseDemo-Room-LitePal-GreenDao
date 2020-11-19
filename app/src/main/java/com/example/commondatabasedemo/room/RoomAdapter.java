package com.example.commondatabasedemo.room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commondatabasedemo.R;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder> {
    List<RoomUser> roomUsers = new ArrayList<>();

    @NonNull
    @Override
    public RoomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_adapter_item, parent, false);
        return new MyViewHolder(view);
    }

    public void setRoomUsers(List<RoomUser> roomUsers) {
        this.roomUsers = roomUsers;
    }

    @Override
    public void onBindViewHolder(@NonNull RoomAdapter.MyViewHolder holder, int position) {
        RoomUser user = roomUsers.get(position);
        holder.idTV.setText(String.valueOf(user.id));
        holder.nameTV.setText(user.name);
        holder.numberTV.setText(user.number);
    }

    @Override
    public int getItemCount() {
        return roomUsers.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView idTV, nameTV, numberTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            idTV = itemView.findViewById(R.id.user_id);
            nameTV = itemView.findViewById(R.id.name);
            numberTV = itemView.findViewById(R.id.number);
        }
    }
}
