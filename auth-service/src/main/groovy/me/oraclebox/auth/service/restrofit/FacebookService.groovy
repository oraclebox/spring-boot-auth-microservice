package me.oraclebox.auth.service.restrofit

import me.oraclebox.facebook.FacebookAccount
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service for verify facebook token.
 * Created by oraclebox@gmail.com on 14/9/2016.
 */
interface FacebookService {

    String ENDPOINT = "https://graph.facebook.com/v2.7/";
    String ME_QUERY = "about,age_range,birthday,email,id,gender,first_name,middle_name,last_name,name,work,location,locale,languages,religion,timezone,website,updated_time,friends{about,email,gender,id,name,first_name,last_name,middle_name,locale,location},education";

    @GET("me")
    Call<FacebookAccount> me(@Query("fields") String fields, @Query("access_token") String accessToken);

}
