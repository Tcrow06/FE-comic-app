package com.tq.comic.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tq.comic.dto.response.authentication.AuthenticationResponse;
import com.tq.comic.dto.response.user.UserResponse;

public class PrefManager {
    private static final String PREF_NAME = "my_app_prefs";
    private static final String KEY_AUTH_RESPONSE = "auth_response";
    private static final String KEY_USER_RESPONSE = "user_response";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public PrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public void saveAuthResponse(AuthenticationResponse authResponse) {
        String json = gson.toJson(authResponse);
        editor.putString(KEY_AUTH_RESPONSE, json);
        editor.apply();
    }
    public void saveUserResponse(UserResponse userResponse) {
        String json = gson.toJson(userResponse);
        editor.putString(KEY_USER_RESPONSE, json);
        editor.apply();
    }

    public AuthenticationResponse getAuthResponse() {
        String json = sharedPreferences.getString(KEY_AUTH_RESPONSE, null);
        if (json != null) {
            return gson.fromJson(json, AuthenticationResponse.class);
        }
        return null;
    }
    public UserResponse getUserResponse() {
        String json = sharedPreferences.getString(KEY_AUTH_RESPONSE, null);
        if (json != null) {
            return gson.fromJson(json, UserResponse.class);
        }
        return null;
    }

    public void clearAuthResponse() {
        editor.remove(KEY_AUTH_RESPONSE);
        editor.apply();
    }
    public void clearUserResponse() {
        editor.remove(KEY_USER_RESPONSE);
        editor.apply();
    }


    public boolean isLoggedIn() {
        return getAuthResponse() != null;
    }

    public void updateAuthField(AuthField field, String newValue) {
        AuthenticationResponse auth = getAuthResponse();
        if (auth == null) return;

        // gọi enum để apply
        field.apply(auth, newValue);

        // lưu lại
        saveAuthResponse(auth);
    }
    public void updateUserField(UseField field, String newValue) {
        UserResponse auth = getUserResponse();
        if (auth == null) return;

        // gọi enum để apply
        field.apply(auth, newValue);

        // lưu lại
        saveUserResponse(auth);
    }
    public enum UseField{
        FIRSTNAME {
            @Override
            public void apply(UserResponse resp, String value) {
                resp.setFirstName(value);
            }
        },
        LASTNAME {
            @Override
            public void apply(UserResponse resp, String value) {
                resp.setLastName(value);
            }
        },
        IMAGE {
            @Override
            public void apply(UserResponse resp, String value) {
                resp.setPicture(value);
            }
        }


        ;
        public abstract void apply(UserResponse resp, String value);

    }

    public enum AuthField {
        TOKEN {
            @Override
            public void apply(AuthenticationResponse resp, String value) {
                resp.setAccessToken(value);
            }
        },



        ;
        /** Áp dụng value mới vào resp */
        public abstract void apply(AuthenticationResponse resp, String value);



    }

}
