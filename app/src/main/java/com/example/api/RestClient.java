package com.example.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/8/1.
 */
public class RestClient {
    public static final String BASE_URL = "http://api.vseatest.com/";

    private static ApiClient apiClient;

    private RestClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        apiClient = retrofit.create(ApiClient.class);
    }

    private static class RestClientHolder {
        private static RestClient restClient = new RestClient();
    }

    public static RestClient getInstance() {
        return RestClientHolder.restClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }
}
