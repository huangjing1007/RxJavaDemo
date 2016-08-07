package com.example.api;

import com.example.bean.AddressListDataResult;
import com.example.bean.UserInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/8/1.
 */
public interface ApiClient {

    @FormUrlEncoded
    @POST("V2/login/memberlogin")
    Call<ResponseBody> login(@Field("user") String mobile, @Field("password") String password);

    @FormUrlEncoded
    @POST("V2/login/memberlogin")
    Observable<UserInfo> loginInfo(@Field("user") String mobile, @Field("password") String password);

    @GET("User/get_address_list")
    Call<AddressListDataResult> getAddressList(@Query("user_id") String user_id);

    @GET("User/get_address_list")
    Observable<AddressListDataResult> getList(@Query("user_id") String user_id);

}
