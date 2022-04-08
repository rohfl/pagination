package com.rohfl.pagination.retrofit;

import com.rohfl.pagination.models.ResponseGetImageList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ImageListInterface {
    // get query to fetch the image list
    @GET("albums/{albumId}/photos")
    Call<ResponseGetImageList> getImageList(@Path("albumId") int albumId);
}
