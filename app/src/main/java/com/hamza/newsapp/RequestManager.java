package com.hamza.newsapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.hamza.newsapp.Models.NewsApiResponse;
import com.hamza.newsapp.Models.NewsHeadLines;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestManager {
    Context mContext;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context mContext) {
        this.mContext = mContext;
    }

    public void getNewsHeadLines(OnFetchDataListener<NewsApiResponse> listener, String category, String query) {
        CallNewsApi callNewsApi = retrofit.create(CallNewsApi.class);
        Call<NewsApiResponse> call = callNewsApi.callHeadLines("us", category, query, mContext.getString(R.string.api_key));
        call.enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsApiResponse> call, @NonNull Response<NewsApiResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    listener.onFetchData(response.body().getArticles(), "Success");
                } else {
                    listener.onFetchData(null, "Failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsApiResponse> call, @NonNull Throwable t) {
                listener.onError("Request Failed: "+t.getMessage());
            }
        });
    }

    public interface CallNewsApi {
        @GET("top-headlines")
        Call<NewsApiResponse> callHeadLines(
                @Query("country") String country,
                @Query("category") String category,
                @Query("q") String query,
                @Query("apiKey") String apiKey
        );
    }
}
