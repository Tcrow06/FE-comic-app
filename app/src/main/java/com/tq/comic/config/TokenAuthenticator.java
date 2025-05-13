package com.tq.comic.config;

import android.content.Context;
import androidx.annotation.Nullable;

import com.tq.comic.dto.request.authentication.RefreshTokenRequest;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.authentication.AuthenticationResponse;
import com.tq.comic.service.auth.AuthService;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Route;
import com.tq.comic.service.auth.AuthAPIService;

public class TokenAuthenticator implements Authenticator {
    private final Context context;

    public TokenAuthenticator(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public Request authenticate(Route route, okhttp3.Response response) throws IOException {
        PrefManager prefManager = new PrefManager(context);
        String refreshToken = prefManager.getAuthResponse().getRefreshToken();

        if (refreshToken == null || refreshToken.isEmpty()) {
            return null;
        }

        AuthAPIService api = new AuthService().getApi(); // hoặc inject trước
        RefreshTokenRequest refreshRequest = new RefreshTokenRequest(refreshToken);

        try {
            retrofit2.Response<ApiResponse<AuthenticationResponse>> tokenResponse =
                    api.refreshToken(refreshRequest).execute();

            if (tokenResponse.isSuccessful() && tokenResponse.body() != null) {
                AuthenticationResponse newAuth = tokenResponse.body().getResult();
                if (newAuth != null) {
                    prefManager.saveAuthResponse(newAuth);

                    return response.request().newBuilder()
                            .header("Authorization", "Bearer " + newAuth.getAccessToken())
                            .build();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}


