package com.hamza.newsapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hamza.newsapp.Models.NewsApiResponse;
import com.hamza.newsapp.Models.NewsHeadLines;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CustomAdapter customAdapter;
    Toolbar toolbar;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setMessage("Fetching News Headlines...");


        //add toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        recyclerView = findViewById(R.id.recycler_main);
        window.setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        getNews();
    }

    private void getNews() {
        RequestManager requestManager = new RequestManager(this);
        OnFetchDataListener<NewsApiResponse> onFetchDataListener = new OnFetchDataListener<NewsApiResponse>() {
            @Override
            public void onFetchData(List<NewsHeadLines> newsHeadLines, String message) {
                showNews(newsHeadLines);
                progressDialog.dismiss();
            }

            private void showNews(List<NewsHeadLines> newsHeadLines) {
                customAdapter = new CustomAdapter(MainActivity.this, newsHeadLines);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(customAdapter);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        };
        requestManager.getNewsHeadLines(onFetchDataListener, "general", "");
    }
}