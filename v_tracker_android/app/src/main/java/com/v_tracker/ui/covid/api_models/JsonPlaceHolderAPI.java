package com.v_tracker.ui.covid.api_models;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderAPI {
    @GET ("summary")
    Call<Post> getPosts();

}
