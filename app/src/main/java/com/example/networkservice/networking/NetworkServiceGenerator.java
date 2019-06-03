package com.example.networkservice.networking;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.networkservice.error_handling.APIError;
import com.example.networkservice.BuildConfig;
import com.example.networkservice.error_handling.ErrorUtils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkServiceGenerator {
    private static final String TAG = "NetworkServiceGenerator";
    private static String apiBaseUrl = "https://api.github.com/";
    private static Retrofit.Builder builder = new Retrofit.Builder()            // static because one instance for entire app
            .baseUrl(apiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create());
    private static Retrofit retrofit = builder.build();
    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

    public static Retrofit retrofit() {
        return builder.build();
    }

    public interface NetworkServiceReturns {
        <T> void onNetworkResponse(NetworkRequestCode requestCode, Response<T> response);
    }

    private static NetworkServiceReturns networkServiceReturns;

    public void setNetworkServiceReturns(Context context) {
        networkServiceReturns = (NetworkServiceReturns) context;
    }

    public interface ProgressCallback {
        void startProgress();

        void dismissProgress();
    }

    private static ProgressCallback progressCallback;

    public void setProgressCallback(Context context) {
        NetworkServiceGenerator.progressCallback = (ProgressCallback) context;
    }

    public static void changeApiBaseUrl(String newApiBaseUrl) {
        apiBaseUrl = newApiBaseUrl;

        builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(apiBaseUrl);
    }

    public static <S> S createNetworkClient(Class<S> networkClient) {
        if (!okHttpClientBuilder.interceptors().contains(loggingInterceptor) && BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(loggingInterceptor);
            builder = builder.client(okHttpClientBuilder.build());
            retrofit = builder.build();
        }
        return retrofit.create(networkClient);
    }

    public static <T> void callNetworkService(final NetworkRequestCode requestCode, Call<T> call) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
//                Note : response.code() -> successful (status 200-299) or erroneous (status 400-599).
                if (response.isSuccessful()) {
                    if (networkServiceReturns != null)
                        networkServiceReturns.onNetworkResponse(requestCode, response);
                } else {
//                    errorHandlingType1(response.code());
//                    errorHandlingType2(response);
                    errorHandlingType3(response);
                }
                if (progressCallback != null) {
                    progressCallback.dismissProgress();
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable throwable) {
                if (progressCallback != null) {
                    progressCallback.dismissProgress();
                }
                if (throwable instanceof IOException)
                    Log.e(TAG, "No internet connection!");
                else
                    Log.e(TAG, "Conversion issue!");
            }
        });
        if (progressCallback != null) {
            progressCallback.startProgress();
        }
    }

    private static void errorHandlingType1(int code) {
        switch (code) {
            case 404:
                Log.e(TAG, "Server returned error: user not found ");
                break;
            case 500:
                Log.e(TAG, "Server returned error: server is broken ");
                break;
            default:
                Log.e(TAG, "Server returned error: unknown error ");
                break;
        }
    }

    private static <T> void errorHandlingType2(Response<T> response) {
        try {
            Log.e(TAG, "Server returned error: " + (response.errorBody() != null ? response.errorBody().string() : ""));
        } catch (IOException e) {
            Log.e(TAG, "Server returned error: unknown error ");
            e.printStackTrace();
        }
    }

    private static <T> void errorHandlingType3(Response<T> response) {
        APIError error = ErrorUtils.parseError(response);
        Log.e(TAG, "Server returned error: " + error.message());
    }
}
