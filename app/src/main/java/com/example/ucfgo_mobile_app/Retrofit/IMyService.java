package com.example.ucfgo_mobile_app.Retrofit;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import io.reactivex.Observable;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IMyService {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("email") String email,
                                    @Field("name") String name,
                                    @Field("password") String password);
    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
                                    @Field("password") String password);

    @GET("garage")
    @FormUrlEncoded
    Observable<String> garageOccupancy();

    @POST("grabName")
    @FormUrlEncoded
    Single<String> grabName(@Field("email") String name);

}
