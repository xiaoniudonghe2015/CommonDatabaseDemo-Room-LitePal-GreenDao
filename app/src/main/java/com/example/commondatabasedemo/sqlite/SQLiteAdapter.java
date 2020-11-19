package com.example.commondatabasedemo.sqlite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commondatabasedemo.R;

import java.util.ArrayList;
import java.util.List;

public class SQLiteAdapter  extends RecyclerView.Adapter<SQLiteAdapter.MyViewHolder> {
    List<SQLiteUser> users = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_adapter_item, parent, false);
        return new MyViewHolder(view);
    }

    public void setUsers(List<SQLiteUser> users) {
        this.users = users;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SQLiteUser user = users.get(position);
        holder.idTV.setText(String.valueOf(user.id));
        holder.nameTV.setText(user.name);
        holder.numberTV.setText(user.number);
    }

    @Override
    public int getItemCount() {
        return users.size();
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

