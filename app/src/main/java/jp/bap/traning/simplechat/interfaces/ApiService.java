package jp.bap.traning.simplechat.interfaces;

import jp.bap.traning.simplechat.response.UserResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("/user/login")
    Call<UserResponse> getUser(@Field("userName") String userName,
            @Field("password") String password);

}