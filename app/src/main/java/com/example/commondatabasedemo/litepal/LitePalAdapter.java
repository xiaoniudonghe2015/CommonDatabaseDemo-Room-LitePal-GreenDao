package com.example.commondatabasedemo.litepal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commondatabasedemo.BR;
import com.example.commondatabasedemo.R;
import com.example.commondatabasedemo.databinding.LitepalAdapterItemBinding;

import java.util.ArrayList;
import java.util.List;

public class LitePalAdapter extends RecyclerView.Adapter<LitePalAdapter.MyViewHolder> {
    List<LitePalUser> users = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.litepal_adapter_item, parent, false);
        return new MyViewHolder(binding);
    }

    public void setUsers(List<LitePalUser> users) {
        this.users.clear();
        if (users != null) {
            this.users.addAll(users);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LitepalAdapterItemBinding binding= (LitepalAdapterItemBinding) holder.getBinding();
        LitePalUser user = users.get(position);
        binding.setVariable(BR.item, user);
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class MyViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
        T mBinding;

        public MyViewHolder(@NonNull T binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public T getBinding() {
            return mBinding;
        }
    }
}

