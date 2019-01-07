package com.example.lenovo.myapplicationdemo.Interface;

import com.example.lenovo.myapplicationdemo.Model.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("read.php")
    Call<List<Item>> getSearchCategory(@Body Block block);
}
