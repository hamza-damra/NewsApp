package com.hamza.newsapp;

import com.hamza.newsapp.Models.NewsHeadLines;

import java.util.List;

public interface OnFetchDataListener<NewsApiResponse> {
    void onFetchData(List<NewsHeadLines> newsHeadLines, String message);
    void onError(String message);

}
