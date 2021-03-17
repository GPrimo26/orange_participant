package com.maksat.participantapp.interfaces;

import android.icu.text.CaseMap;

import com.maksat.participantapp.models.AuthBody;
import com.maksat.participantapp.models.EditableProfile;
import com.maksat.participantapp.models.EventProgram;
import com.maksat.participantapp.models.ForgotPassword;
import com.maksat.participantapp.models.Title;
import com.maksat.participantapp.models.TokenBody;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Profile {

    @POST("/api/token")
    Call<TokenBody> getToken(@Body AuthBody authBody);

    @GET("/api/events/{eventId}/participants/{participantId}")
    Call<com.maksat.participantapp.models.Profile> getProfile(@Path("eventId") Integer eventId,
                                                              @Path("participantId") Integer participantId);

    @PUT("/api/v2/events/{eventId}/participants/{participantId}/personal-data")
    Call<ResponseBody> editPersonalData(@Path("eventId") Integer eventId,
                                        @Path("participantId") Integer participantId,
                                        @Body EditableProfile.PersonalData personalData);

    @PUT("/api/events/{eventId}/participants/{participantId}/company")
    Call<ResponseBody> editCompany(@Path("eventId") Integer eventId,
                                   @Path("participantId") Integer participantId,
                                   @Body EditableProfile.Company company);

    @PUT("/api/events/{eventId}/participants/{participantId}/contact")
    Call<ResponseBody> editContact(@Path("eventId") Integer eventId,
                                   @Path("participantId") Integer participantId,
                                   @Body EditableProfile.Contact contact);

    @PUT("/api/events/{eventId}/participants/{participantId}/visa")
    Call<ResponseBody> editVisa(@Path("eventId") Integer eventId,
                                @Path("participantId") Integer participantId,
                                @Body EditableProfile.Visa visa);

    @PUT("/api/events/{eventId}/participants/{participantId}/arrival-departure")
    Call<ResponseBody> editArrDep(@Path("eventId") Integer eventId,
                                  @Path("participantId") Integer participantId,
                                  @Body EditableProfile.ArrivalDeparture arrivalDeparture);


    @GET("/api/events/{eventId}/participant/{participantId}/event-program")
    Call<List<EventProgram>> getProgram(@Path("eventId") Integer eventId,
                                        @Path("participantId") Integer participantId,
                                        @Query("Name") String Name,
                                        @Query("DateTimeStart") String DateTimeStart,
                                        @Query("DateTimeFinish") String DateTimeFinish,
                                        @Query("SportId") Integer SportId,
                                        @Query("Place") String Place/*,
                                        @Query("StatusId") Integer StatusId*/);
    @POST("/api/forgot-password")
    Call<ResponseBody> sendCode(@Body ForgotPassword forgotPassword);

    @GET("/api/dictionary/titles")
    Call<List<Title>> getTitles();

    @Multipart
    @POST("/api/events/{eventId}/participants/{participantId}/feedback")
    Call<ResponseBody> sendFeedBack(@Path("eventId") Integer eventId,
                                    @Path("participantId") Integer participantId,
                                    @Query("Title") String title,
                                    @Query("Message") String message,
                                    @Part("Image") RequestBody image);
}
