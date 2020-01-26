package com.wyattpath.hobbyspot.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyattpath.hobbyspot.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
    }
}
