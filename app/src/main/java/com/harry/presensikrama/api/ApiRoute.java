package com.harry.presensikrama.api;

import com.harry.presensikrama.model.AuthResponse;
import com.harry.presensikrama.model.DetailPresensiResponse;
import com.harry.presensikrama.model.KramaProfileGetResponse;
import com.harry.presensikrama.model.PresensiDetailResponse;
import com.harry.presensikrama.model.PresensiGetResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiRoute {

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("krama/login")
    Call<AuthResponse> loginUser(
            @Field("email") String username,
            @Field("password") String password
    );

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("krama/get-data-krama")
    Call<KramaProfileGetResponse> getProfileKrama(
            @Field("token") String token
    );

    @Headers({"Accept: application/json"})
    @GET("presensi/get-open-krama")
    Call<PresensiGetResponse> getPresensiOpen(
            @Query("nic") String nic
    );


    @Headers({"Accept: application/json"})
    @GET("presensi/get-filled")
    Call<PresensiGetResponse> getPresensiTerdaftar(
            @Query("nic") String nic
    );
}
