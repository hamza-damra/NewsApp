package com.hamza.newsapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hamza.newsapp.Models.NewsHeadLines;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder>{
    private Context mContext;
    private List<NewsHeadLines> newsHeadLinesList;
    private SelectListener selectListener;
    public CustomAdapter(Context mContext, List<NewsHeadLines> newsHeadLinesList, SelectListener selectListener) {
        this.mContext = mContext;
        this.newsHeadLinesList = newsHeadLinesList;
        this.selectListener = selectListener;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.headlines_list_item, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        NewsHeadLines newsHeadLines = newsHeadLinesList.get(position);
        if (newsHeadLines.getTitle().equals("[Removed]")) {
            // If the title is "[Removed]", hide the CardView
            holder.card_view.removeView(holder.card_view);
        } else {
            // Else, populate the data as usual and make sure the CardView is visible
            holder.card_view.setVisibility(View.VISIBLE);
            holder.text_title.setText(newsHeadLines.getTitle());
            holder.text_source.setText(newsHeadLines.getSource().getName());
            holder.card_view.setOnClickListener(v -> {
                selectListener.onNewsClicked(newsHeadLinesList.get(position));
            });
            if(newsHeadLines.getUrlToImage() != null) {
                Picasso.get().load(newsHeadLines.getUrlToImage()).into(holder.image_headline);
            } else {
                // Handle case where there is no image URL
                holder.image_headline.setImageResource(R.drawable.not_available); // Replace with your default image resource
            }
        }
    }


    @Override
    public int getItemCount() {
        if (newsHeadLinesList != null) {
            return newsHeadLinesList.size();
        } else {
            return 0;
        }
    }

}
