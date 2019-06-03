package com.example.networkservice.networking;

import com.example.networkservice.GitHubRepo;
import com.example.networkservice.User;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NetworkClient {
    @GET("/users/{user}/repos")    //API End Point
    Call<List<GitHubRepo>> getGithubRepositories(@Path("user") String user);

    @POST("api/users")
    Call<User> postObject(@Body User user);

    @PUT("api/users/2")
    Call<User> putObject(@Body User user);
}
