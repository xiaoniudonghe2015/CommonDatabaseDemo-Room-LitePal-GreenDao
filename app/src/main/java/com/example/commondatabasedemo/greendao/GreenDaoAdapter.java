package com.example.commondatabasedemo.greendao;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commondatabasedemo.R;

import java.util.ArrayList;
import java.util.List;

public class GreenDaoAdapter extends RecyclerView.Adapter<GreenDaoAdapter.MyViewHolder> {
    List<GreenDaoUser> users = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_adapter_item, parent, false);
        return new MyViewHolder(view);
    }

    public void setUsers(List<GreenDaoUser> users) {
        this.users = users;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GreenDaoUser user = users.get(position);
        holder.idTV.setText(String.valueOf(user.getId()));
        holder.nameTV.setText(user.getName());
        holder.numberTV.setText(user.getNumber());
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
