package com.hamza.newsapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hamza.newsapp.R;

public class CustomViewHolder extends RecyclerView.ViewHolder {
    TextView text_title;
    TextView text_source;
    ImageView image_headline;
    CardView card_view;

    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);
        text_title = itemView.findViewById(R.id.text_title);
        text_source = itemView.findViewById(R.id.text_source);
        image_headline = itemView.findViewById(R.id.image_headline);
        card_view = itemView.findViewById(R.id.card_headline);
    }
}
