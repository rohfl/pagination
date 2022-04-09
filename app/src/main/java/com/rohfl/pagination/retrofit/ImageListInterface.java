package com.rohfl.pagination.retrofit;

import com.rohfl.pagination.models.ResponseGetImageList;
import com.rohfl.pagination.models.ResponseGetImageListItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ImageListInterface {
    // get query to fetch the image list
    @GET("{albumId}/photos")
    Call<List<ResponseGetImageListItem>> getImageList(@Path("albumId") int albumId);
}
