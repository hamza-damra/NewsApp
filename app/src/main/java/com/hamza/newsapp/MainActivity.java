package com.hamza.newsapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.button.MaterialButton;
import com.hamza.newsapp.Models.NewsApiResponse;
import com.hamza.newsapp.Models.NewsHeadLines;

import java.util.List;
import java.util.Objects;

// implement setOnClickListener for buttons
public class MainActivity extends AppCompatActivity implements SelectListener , View.OnClickListener {
    RecyclerView recyclerView;
    CustomAdapter customAdapter;
    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    SearchView searchView;
    MaterialButton btn_business, btn_entertainment, btn_general, btn_health, btn_science, btn_sports, btn_technology;

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
        searchView = findViewById(R.id.search_view);
        btn_business = findViewById(R.id.btn_business);
        btn_entertainment = findViewById(R.id.btn_entertainment);
        btn_general = findViewById(R.id.btn_general);
        btn_health = findViewById(R.id.btn_health);
        btn_science = findViewById(R.id.btn_science);
        btn_sports = findViewById(R.id.btn_sports);
        btn_technology = findViewById(R.id.btn_technology);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        //add toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        recyclerView = findViewById(R.id.recycler_main);
        window.setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        getNews();

        btn_business.setOnClickListener(this);
        btn_entertainment.setOnClickListener(this);
        btn_general.setOnClickListener(this);
        btn_health.setOnClickListener(this);
        btn_science.setOnClickListener(this);
        btn_sports.setOnClickListener(this);
        btn_technology.setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getNews();
            swipeRefreshLayout.setRefreshing(false);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressDialog.show();
                progressDialog.setMessage("Fetching News Headlines...");
                RequestManager requestManager = new RequestManager(MainActivity.this);
                OnFetchDataListener<NewsApiResponse> onFetchDataListener = new OnFetchDataListener<NewsApiResponse>() {
                    @Override
                    public void onFetchData(List<NewsHeadLines> newsHeadLines, String message) {
                        showNews(newsHeadLines);
                        progressDialog.dismiss();
                    }

                    private void showNews(List<NewsHeadLines> newsHeadLines) {
                        customAdapter = new CustomAdapter(MainActivity.this, newsHeadLines, MainActivity.this);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        recyclerView.setAdapter(customAdapter);
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                };
                // sheck if query is empty
                if(query.isEmpty()) {
                    requestManager.getNewsHeadLines(onFetchDataListener, "general", "");
                    Toast.makeText(MainActivity.this, "No News Found", Toast.LENGTH_SHORT).show();
                    return false;
                }
                   requestManager.getNewsHeadLines(onFetchDataListener, "", query);
                return false;

            }


            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


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
                customAdapter = new CustomAdapter(MainActivity.this, newsHeadLines, MainActivity.this);
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

    @Override
    public void onNewsClicked(NewsHeadLines newsHeadLines) {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra("news", newsHeadLines);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        progressDialog.show();
        progressDialog.setMessage("Fetching News Headlines...");
        if(v.getId() == R.id.btn_business) {
            getNewsByCategory("business");
        }
        if(v.getId() == R.id.btn_entertainment) {
            getNewsByCategory("entertainment");
        }
        if(v.getId() == R.id.btn_general) {
            getNewsByCategory("general");
        }
        if(v.getId() == R.id.btn_health) {
            getNewsByCategory("health");
        }
        if(v.getId() == R.id.btn_science) {
            getNewsByCategory("science");
        }
        if(v.getId() == R.id.btn_sports) {
            getNewsByCategory("sports");
        }
        if(v.getId() == R.id.btn_technology) {
            getNewsByCategory("technology");
        }



    }

    private void getNewsByCategory(String general) {
        RequestManager requestManager = new RequestManager(this);
        OnFetchDataListener<NewsApiResponse> onFetchDataListener = new OnFetchDataListener<NewsApiResponse>() {
            @Override
            public void onFetchData(List<NewsHeadLines> newsHeadLines, String message) {
                showNews(newsHeadLines);
                progressDialog.dismiss();
            }

            private void showNews(List<NewsHeadLines> newsHeadLines) {
                customAdapter = new CustomAdapter(MainActivity.this, newsHeadLines, MainActivity.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(customAdapter);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        };
        requestManager.getNewsHeadLines(onFetchDataListener, general, "");

    }
}