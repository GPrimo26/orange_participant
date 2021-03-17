package com.maksat.participantapp.interfaces;

import com.maksat.participantapp.models.AcrStatus;
import com.maksat.participantapp.models.BadgeStatus;
import com.maksat.participantapp.models.Category;
import com.maksat.participantapp.models.Profile;
import com.maksat.participantapp.models.Sport;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Dictionary {
    @GET("/api/statuses/acr")
    Call<List<AcrStatus>> getAcrStatuses();

    @GET("/api/events/{eventId}/categories")
    Call<List<Category>> getCategories(@Path("eventId") Integer id);

    @GET("/api/dictionary/sports")
    Call<List<Sport>> getSports();

    @GET("/api/statuses/badge")
    Call<List<BadgeStatus>> getBadgeStatuses();

    @GET("/api/dictionary/spheres")
    Call<List<Profile.Company.Sphere>> getSpheres();
}
