package com.example.networkservice.error_handling;

import com.example.networkservice.networking.NetworkServiceGenerator;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {
    public static APIError parseError(Response<?> response) {
        Converter<ResponseBody, APIError> converter =
                NetworkServiceGenerator.retrofit().responseBodyConverter(APIError.class, new Annotation[0]);
        APIError error;
        try {
            if (response.errorBody() != null) {
                error = converter.convert(response.errorBody());
            } else {
                return new APIError();
            }
        } catch (IOException e) {
            return new APIError();
        }
        return error;
    }
}
