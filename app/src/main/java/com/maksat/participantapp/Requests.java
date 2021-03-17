package com.maksat.participantapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.maksat.participantapp.interfaces.Dictionary;
import com.maksat.participantapp.interfaces.Profile;
import com.maksat.participantapp.interfaces.Server;
import com.maksat.participantapp.models.AcrStatus;
import com.maksat.participantapp.models.AuthBody;
import com.maksat.participantapp.models.BadgeStatus;
import com.maksat.participantapp.models.Category;
import com.maksat.participantapp.models.EditableProfile;
import com.maksat.participantapp.models.ErrorBody;
import com.maksat.participantapp.models.Sport;
import com.maksat.participantapp.models.Title;
import com.maksat.participantapp.models.TokenBody;
import com.maksat.participantapp.ui.SharedProfileViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Requests {
    public Requests(Activity activity) {
        this.context= activity;
        this.viewModelStoreOwner= (ViewModelStoreOwner) activity;
    }

    private final Context context;
    private final ViewModelStoreOwner viewModelStoreOwner;
    private OnProfileLoadedListener mProfileListener;
    private OnLoggedInListener mOnLoggedInListener;
    private OnSentFeedbackListener onSentFeedbackListener;
    private SharedProfileViewModel sharedProfileViewModel;




    // region interfaces
    public interface OnProfileLoadedListener{
        void onLoaded();
    }
    public interface OnLoggedInListener{
        void onLoggedIn();
    }
    public interface OnSentFeedbackListener{
        void onSuccess();
    }
    public void setOnProfileLoadedListener(OnProfileLoadedListener listener){
        mProfileListener=listener;
    }
    public void setOnLoggedInListener(OnLoggedInListener listener){
        mOnLoggedInListener=listener;
    }
    public void setOnSentFeedbackListener(OnSentFeedbackListener listener){
        onSentFeedbackListener=listener;
    }
    // endregion interfaces


    // region Work with profile
    public void getProfile() {
        getTitles();
        if (Variables.token != null) {
            Profile profileApi = Server.GetServerWithToken(Profile.class, Variables.token.access_token);
            Call<com.maksat.participantapp.models.Profile> call = profileApi.getProfile(Variables.token.eventId, Variables.token.participantId);
            call.enqueue(new Callback<com.maksat.participantapp.models.Profile>() {
                @Override
                public void onResponse(@NonNull Call<com.maksat.participantapp.models.Profile> call, @NonNull Response<com.maksat.participantapp.models.Profile> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (sharedProfileViewModel == null) {
                                sharedProfileViewModel = new ViewModelProvider(viewModelStoreOwner).get(SharedProfileViewModel.class);
                            }
                            sharedProfileViewModel.setProfile(response.body());
                            //Variables.profile = response.body();
                            if (mProfileListener != null) {
                                mProfileListener.onLoaded();
                            }
                        }
                    } else {
                        try {
                            if (response.errorBody() != null) {
                                Gson gson = new Gson();
                                ErrorBody errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                                Toast.makeText(context, errorBody.Ru, Toast.LENGTH_SHORT).show();
                                Log.d("NAME_RESP_ERROR", "" + errorBody.Ru);
                            } else {
                                Log.d("NAME_RESP_ERROR", "ErrorBody is null.");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (response.code() == 401) {
                            goToLoginScreen();
                        }
                        Variables.token = null;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<com.maksat.participantapp.models.Profile> call, @NonNull Throwable t) {
                    Toast.makeText(context, "Ошибка сервера.", Toast.LENGTH_SHORT).show();
                    Log.d("PROFILE_RESPONSE_ERROR", "" + t.getMessage());
                    Variables.token = null;
                    goToLoginScreen();
                }
            });
        } else {
            goToLoginScreen();
        }
    }

    public void editPersonalData(EditableProfile.PersonalData editableProfile) {
        if (Variables.token != null && editableProfile != null) {
            Profile profile = Server.GetServerWithToken(Profile.class, Variables.token.access_token);
            if (sharedProfileViewModel == null) {
                sharedProfileViewModel = new ViewModelProvider(viewModelStoreOwner).get(SharedProfileViewModel.class);
            }
            Call<ResponseBody> call = profile.editPersonalData(Variables.token.eventId, Objects.requireNonNull(sharedProfileViewModel.getProfile().getValue()).id, editableProfile);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Сохранено!", Toast.LENGTH_SHORT).show();
                        if (sharedProfileViewModel == null) {
                            sharedProfileViewModel = new ViewModelProvider(viewModelStoreOwner).get(SharedProfileViewModel.class);
                        }
                        com.maksat.participantapp.models.Profile profile1 = new com.maksat.participantapp.models.Profile(Objects.requireNonNull(sharedProfileViewModel.getProfile().getValue()));
                        profile1.setPersonalData(editableProfile);
                        sharedProfileViewModel.setProfile(profile1);
                        if (mProfileListener != null) {
                            mProfileListener.onLoaded();
                        }
                    } else {
                        try {
                            if (response.errorBody() != null) {
                                Gson gson = new Gson();
                                ErrorBody errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                                Toast.makeText(context, errorBody.Ru, Toast.LENGTH_SHORT).show();
                                Log.d("PERSINFO_RESP_ERROR", "" + errorBody.Ru);
                            } else {
                                Log.d("PERSINFO_RESP_ERROR", "ErrorBody is null.");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (response.code() == 401) {
                            goToLoginScreen();
                        }
                        Variables.token = null;
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    Toast.makeText(context, "Ошибка сервера.", Toast.LENGTH_SHORT).show();
                    Log.d("PERSINFO_SERVER_ERROR", "" + t.getMessage());
                }
            });
        }
    }

    public void editContact(EditableProfile.Contact contact) {
        if (Variables.token != null && contact != null) {
            Profile profile = Server.GetServerWithToken(Profile.class, Variables.token.access_token);
            if (sharedProfileViewModel == null) {
                sharedProfileViewModel = new ViewModelProvider(viewModelStoreOwner).get(SharedProfileViewModel.class);
            }
            Call<ResponseBody> call = profile.editContact(Variables.token.eventId, Objects.requireNonNull(sharedProfileViewModel.getProfile().getValue()).id, contact);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Сохранено!", Toast.LENGTH_SHORT).show();
                        if (sharedProfileViewModel == null) {
                            sharedProfileViewModel = new ViewModelProvider(viewModelStoreOwner).get(SharedProfileViewModel.class);
                        }
                        com.maksat.participantapp.models.Profile profile1 = new com.maksat.participantapp.models.Profile(Objects.requireNonNull(sharedProfileViewModel.getProfile().getValue()));
                        profile1.setContact(contact);
                        sharedProfileViewModel.setProfile(profile1);
                        if (mProfileListener != null) {
                            mProfileListener.onLoaded();
                        }
                    } else {
                        try {
                            if (response.errorBody() != null) {
                                Gson gson = new Gson();
                                ErrorBody errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                                Toast.makeText(context, errorBody.Ru, Toast.LENGTH_SHORT).show();
                                Log.d("CONTACT_RESP_ERROR", "" + errorBody.Ru);
                            } else {
                                Log.d("CONTACT_RESP_ERROR", "ErrorBody is null.");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (response.code() == 401) {
                            goToLoginScreen();
                        }
                        Variables.token = null;
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    Toast.makeText(context, "Ошибка сервера.", Toast.LENGTH_SHORT).show();
                    Log.d("CONTACT_SERVER_ERROR", "" + t.getMessage());
                }
            });
        }
    }

    public void editCompany(EditableProfile.Company company) {
        if (Variables.token != null && company != null) {
            Profile profile = Server.GetServerWithToken(Profile.class, Variables.token.access_token);
            if (sharedProfileViewModel == null) {
                sharedProfileViewModel = new ViewModelProvider(viewModelStoreOwner).get(SharedProfileViewModel.class);
            }
            Call<ResponseBody> call = profile.editCompany(Variables.token.eventId, Objects.requireNonNull(sharedProfileViewModel.getProfile().getValue()).id, company);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Сохранено!", Toast.LENGTH_SHORT).show();
                        if (sharedProfileViewModel == null) {
                            sharedProfileViewModel = new ViewModelProvider(viewModelStoreOwner).get(SharedProfileViewModel.class);
                        }
                        com.maksat.participantapp.models.Profile profile1 = new com.maksat.participantapp.models.Profile(Objects.requireNonNull(sharedProfileViewModel.getProfile().getValue()));
                        profile1.setCompany(company);
                        sharedProfileViewModel.setProfile(profile1);
                        if (mProfileListener != null) {
                            mProfileListener.onLoaded();
                        }
                    } else {
                        try {
                            if (response.errorBody() != null) {
                                Gson gson = new Gson();
                                ErrorBody errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                                Toast.makeText(context, errorBody.Ru, Toast.LENGTH_SHORT).show();
                                Log.d("COMPANY_RESP_ERROR", "" + errorBody.Ru);
                            } else {
                                Log.d("COMPANY_RESP_ERROR", "ErrorBody is null.");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (response.code() == 401) {
                            goToLoginScreen();
                        }
                        Variables.token = null;
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    Toast.makeText(context, "Ошибка сервера.", Toast.LENGTH_SHORT).show();
                    Log.d("COMPANY_SERVER_ERROR", "" + t.getMessage());
                }
            });
        }
    }

    public void editVisa(EditableProfile.Visa visa){
        if (Variables.token != null && visa != null) {
            Profile profile = Server.GetServerWithToken(Profile.class, Variables.token.access_token);
            if (sharedProfileViewModel == null) {
                sharedProfileViewModel = new ViewModelProvider(viewModelStoreOwner).get(SharedProfileViewModel.class);
            }
            Call<ResponseBody> call = profile.editVisa(Variables.token.eventId, Objects.requireNonNull(sharedProfileViewModel.getProfile().getValue()).id, visa);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Сохранено!", Toast.LENGTH_SHORT).show();
                        if (sharedProfileViewModel == null) {
                            sharedProfileViewModel = new ViewModelProvider(viewModelStoreOwner).get(SharedProfileViewModel.class);
                        }
                        com.maksat.participantapp.models.Profile profile1 = new com.maksat.participantapp.models.Profile(Objects.requireNonNull(sharedProfileViewModel.getProfile().getValue()));
                        profile1.setVisa(visa);
                        sharedProfileViewModel.setProfile(profile1);
                        if (mProfileListener != null) {
                            mProfileListener.onLoaded();
                        }
                    } else {
                        try {
                            if (response.errorBody() != null) {
                                Gson gson = new Gson();
                                ErrorBody errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                                Toast.makeText(context, errorBody.Ru, Toast.LENGTH_SHORT).show();
                                Log.d("VISA_RESP_ERROR", "" + errorBody.Ru);
                            } else {
                                Log.d("VISA_RESP_ERROR", "ErrorBody is null.");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (response.code() == 401) {
                            goToLoginScreen();
                        }
                        Variables.token = null;
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    Toast.makeText(context, "Ошибка сервера.", Toast.LENGTH_SHORT).show();
                    Log.d("VISA_SERVER_ERROR", "" + t.getMessage());
                }
            });
        }
    }


    //endregion Work with profile

    //region Get dictionary information
    private void getTitles() {
        Profile api = Server.GetServer(Profile.class);
        Call<List<Title>> call = api.getTitles();
        call.enqueue(new Callback<List<Title>>() {
            @Override
            public void onResponse(@NotNull Call<List<Title>> call, @NotNull Response<List<Title>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Variables.titles = response.body();
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            Gson gson = new Gson();
                            ErrorBody errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                            Toast.makeText(context, errorBody.Ru, Toast.LENGTH_SHORT).show();
                            Log.d("NAME_RESP_ERROR", "" + errorBody.Ru);
                        } else {
                            Log.d("NAME_RESP_ERROR", "ErrorBody is null.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Title>> call, @NotNull Throwable t) {

            }
        });
    }

    public void getDictionary() {
        Dictionary dictionaryApi = Server.GetServer(Dictionary.class);
        Call<List<Sport>> call1 = dictionaryApi.getSports();
        Call<List<AcrStatus>> call2 = dictionaryApi.getAcrStatuses();
        Call<List<BadgeStatus>> call3 = dictionaryApi.getBadgeStatuses();
        Call<List<com.maksat.participantapp.models.Profile.Company.Sphere>> call4 = dictionaryApi.getSpheres();
        call1.enqueue(new Callback<List<Sport>>() {
            @Override
            public void onResponse(@NonNull Call<List<Sport>> call, @NonNull Response<List<Sport>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Variables.sports = response.body();
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            Gson gson = new Gson();
                            ErrorBody errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                            Toast.makeText(context, errorBody.Ru, Toast.LENGTH_SHORT).show();
                            Log.d("NAME_RESP_ERROR", "" + errorBody.Ru);
                        } else {
                            Log.d("NAME_RESP_ERROR", "ErrorBody is null.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Sport>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Ошибка сервера.", Toast.LENGTH_SHORT).show();
                Log.d("SPORTS_SERV_ERROR", "" + t.getMessage());
            }
        });
        call2.enqueue(new Callback<List<AcrStatus>>() {
            @Override
            public void onResponse(@NotNull Call<List<AcrStatus>> call, @NotNull Response<List<AcrStatus>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Variables.acrStatuses = response.body();
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            Gson gson = new Gson();
                            ErrorBody errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                            Toast.makeText(context, errorBody.Ru, Toast.LENGTH_SHORT).show();
                            Log.d("NAME_RESP_ERROR", "" + errorBody.Ru);
                        } else {
                            Log.d("NAME_RESP_ERROR", "ErrorBody is null.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<AcrStatus>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Ошибка сервера.", Toast.LENGTH_SHORT).show();
                Log.d("ACRS_SERV_ERROR", "" + t.getMessage());
            }
        });
        call3.enqueue(new Callback<List<BadgeStatus>>() {
            @Override
            public void onResponse(@NotNull Call<List<BadgeStatus>> call, @NotNull Response<List<BadgeStatus>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Variables.badgeStatuses = response.body();
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            Gson gson = new Gson();
                            ErrorBody errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                            Toast.makeText(context, errorBody.Ru, Toast.LENGTH_SHORT).show();
                            Log.d("BADGES_RESP_ERROR", "" + errorBody.Ru);
                        } else {
                            Log.d("BADGES_RESP_ERROR", "ErrorBody is null.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<BadgeStatus>> call, @NotNull Throwable t) {
                Toast.makeText(context, "Ошибка сервера.", Toast.LENGTH_SHORT).show();
                Log.d("BADGES_SERV_ERROR", "" + t.getMessage());
            }
        });
        call4.enqueue(new Callback<List<com.maksat.participantapp.models.Profile.Company.Sphere>>() {
            @Override
            public void onResponse(@NotNull Call<List<com.maksat.participantapp.models.Profile.Company.Sphere>> call, @NotNull Response<List<com.maksat.participantapp.models.Profile.Company.Sphere>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Variables.spheres = response.body();
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            Gson gson = new Gson();
                            ErrorBody errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                            Toast.makeText(context, errorBody.Ru, Toast.LENGTH_SHORT).show();
                            Log.d("SPHERES_RESP_ERROR", "" + errorBody.Ru);
                        } else {
                            Log.d("SPHERES_RESP_ERROR", "ErrorBody is null.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<com.maksat.participantapp.models.Profile.Company.Sphere>> call, @NotNull Throwable t) {
                Toast.makeText(context, "Ошибка сервера.", Toast.LENGTH_SHORT).show();
                Log.d("SPHERES_SERV_ERROR", "" + t.getMessage());
            }
        });
    }

    public void getCategories() {
        Dictionary dictionaryApi = Server.GetServer(Dictionary.class);
        Call<List<Category>> call1 = dictionaryApi.getCategories(Variables.token.eventId);
        call1.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NotNull Call<List<Category>> call, @NotNull Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Variables.allCategories = response.body();
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            Gson gson = new Gson();
                            ErrorBody errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                            Toast.makeText(context, errorBody.Ru, Toast.LENGTH_SHORT).show();
                            Log.d("NAME_RESP_ERROR", "" + errorBody.Ru);
                        } else {
                            Log.d("NAME_RESP_ERROR", "ErrorBody is null.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Ошибка сервера.", Toast.LENGTH_SHORT).show();
                Log.d("CATEGS_SERV_ERROR", "" + t.getMessage());
            }
        });
    }
    // endregion Get dictionary information

    // region Login
    public void logIn(String login, String password) {
        com.maksat.participantapp.interfaces.Profile tokenApi = Server.GetServer(com.maksat.participantapp.interfaces.Profile.class);
        AuthBody authBody = new AuthBody(login, password);
        Call<TokenBody> call = tokenApi.getToken(authBody);
        call.enqueue(new Callback<TokenBody>() {
            @Override
            public void onResponse(@NonNull Call<TokenBody> call, @NonNull Response<TokenBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (context instanceof MainActivity) {
                            try {
                                ((MainActivity) context).preferences.edit().putInt("tokenPId", response.body().participantId).apply();
                                ((MainActivity) context).preferences.edit().putInt("tokenEId", response.body().eventId).apply();
                                ((MainActivity) context).preferences.edit().putString("tokenToken", response.body().access_token).apply();
                                ((MainActivity) context).preferences.edit().putString("tokenUN", response.body().username).apply();
                                Set<String> stringSet = new HashSet<>(response.body().roles);
                                ((MainActivity) context).preferences.edit().putStringSet("tokenRoles", stringSet).apply();
                                ((MainActivity) context).preferences.edit().putInt("tokenLID", response.body().languageId).apply();
                            }catch (Exception e){
                                Toast.makeText(context, "Такой пользователь не существует.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        Variables.token = response.body();
                        if (mOnLoggedInListener != null) {
                            mOnLoggedInListener.onLoggedIn();
                        }
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            Gson gson = new Gson();
                            ErrorBody errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                            Toast.makeText(context, errorBody.Ru, Toast.LENGTH_SHORT).show();
                            Log.d("NAME_RESP_ERROR", "" + errorBody.Ru);
                        } else {
                            Log.d("NAME_RESP_ERROR", "ErrorBody is null.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TokenBody> call, @NonNull Throwable t) {
                Toast.makeText(context, "Ошибка сервера.", Toast.LENGTH_SHORT).show();
                Log.d("TOKEN_SERVER_ERROR", "" + t.getMessage());
            }
        });
    }

    private void goToLoginScreen() {
        Toast.makeText(context, "Вы не авторизованы", Toast.LENGTH_SHORT).show();
        if (context instanceof MainActivity) {
            Variables.token = null;
            ((MainActivity) context).preferences.edit().putString("token", "").apply();
            BottomNavigationView navView = ((MainActivity) context).findViewById(R.id.nav_view);
            if (navView != null) {
                if (navView.getVisibility() == View.VISIBLE || navView.getVisibility() == View.INVISIBLE)
                    navView.setVisibility(View.GONE);
            }
            View divider = ((MainActivity) context).findViewById(R.id.divider2);
            if (divider != null) {
                if (divider.getVisibility() == View.VISIBLE || divider.getVisibility() == View.INVISIBLE)
                    divider.setVisibility(View.GONE);
            }
            Navigation.findNavController((MainActivity) context, R.id.nav_host_fragment).navigate(R.id.loginFragment);
        }
    }
    // endregion Login

    //region FeedBack
    public void sendFeedBack(String title, String text, RequestBody filePart) {
        Profile api= Server.GetServerWithToken(Profile.class, Variables.token.access_token);
        Call<ResponseBody> call=api.sendFeedBack(Variables.token.eventId, Variables.token.participantId, title, text, filePart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, "Сообщение успешно отправлено!", Toast.LENGTH_SHORT).show();
                    if (onSentFeedbackListener!=null) {
                        onSentFeedbackListener.onSuccess();
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            Gson gson = new Gson();
                            ErrorBody errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                            Toast.makeText(context, errorBody.Ru, Toast.LENGTH_SHORT).show();
                            Log.d("FEEDB_RESP_ERROR", "" + errorBody.Ru);
                        } else {
                            Log.d("FEEDB_RESP_ERROR", "ErrorBody is null.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (response.code() == 401) {
                        goToLoginScreen();
                    }
                    Variables.token = null;
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context, "Ошибка сервера.", Toast.LENGTH_SHORT).show();
                Log.d("FEEDB_SERVER_ERROR", "" + t.getMessage());
            }
        });
    }
    //endregion FeedBack
}
