package com.hamza.newsapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hamza.newsapp.Models.NewsHeadLines;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    NewsHeadLines newsHeadLines;
    TextView title;
    TextView description;
    TextView author;
    TextView publishedAt;
    ImageView image;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        image = findViewById(R.id.image);
        button = findViewById(R.id.back);
        author = findViewById(R.id.author);
        publishedAt = findViewById(R.id.date);
        newsHeadLines = (NewsHeadLines) getIntent().getSerializableExtra("news");
        assert newsHeadLines != null;
        title.setText(newsHeadLines.getTitle());
        description.setText(newsHeadLines.getDescription());
        author.setText(newsHeadLines.getAuthor());
        publishedAt.setText(newsHeadLines.getPublishedAt());
        if(newsHeadLines.getUrlToImage() != null) {
            Picasso.get().load(newsHeadLines.getUrlToImage()).into(image);
        } else {
            // Handle case where there is no image URL
            image.setImageResource(R.drawable.not_available); // Replace with your default image resource
        }

        button.setOnClickListener(v -> {
            finish();
        });







    }
}