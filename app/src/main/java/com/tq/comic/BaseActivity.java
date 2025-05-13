package com.tq.comic;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tq.comic.config.PrefManager;
import com.tq.comic.dto.request.authentication.IntrospectRequest;
import com.tq.comic.dto.request.authentication.RefreshTokenRequest;
import com.tq.comic.dto.response.ApiResponse;
import com.tq.comic.dto.response.authentication.AuthenticationResponse;
import com.tq.comic.dto.response.authentication.IntrospectResponse;
import com.tq.comic.service.auth.AuthService;

public abstract class BaseActivity extends AppCompatActivity {
    protected PrefManager prefManager;
    protected AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(this);
        authService = new AuthService();
    }

    protected void checkAndRefreshToken(Callback callback) {
        AuthenticationResponse auth = prefManager.getAuthResponse();

        if (auth == null || auth.getAccessToken() == null) {
            forceLogout();
            if (this instanceof MainHomeActivity) {
                ((MainHomeActivity) this).loadUserInfo(null);
            }
            return;
        }

        authService.introspect(new IntrospectRequest(auth.getAccessToken()), new AuthService.CallBack<IntrospectResponse>() {
            @Override
            public void onSuccess(ApiResponse<IntrospectResponse> result) {
                if (result.getResult()!=null) {
                    callback.onTokenValid(auth);
                } else {
                    refreshToken(callback);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                refreshToken(callback);
            }
        });
    }

    private void refreshToken(Callback callback) {
        String refreshToken = prefManager.getAuthResponse().getRefreshToken();
        if (refreshToken == null || refreshToken.isEmpty()) {
            forceLogout();
            if (this instanceof MainHomeActivity) {
                ((MainHomeActivity) this).loadUserInfo(null);
            }
            return;
        }

        authService.refreshToken(new RefreshTokenRequest(refreshToken), new AuthService.CallBack<AuthenticationResponse>() {
            @Override
            public void onSuccess(ApiResponse<AuthenticationResponse> result) {
                if (result.getResult() != null) {
                    prefManager.saveAuthResponse(result.getResult());
                    callback.onTokenValid(result.getResult());
                } else {
                    forceLogout();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                forceLogout();
            }
        });
    }

    protected void forceLogout() {
        prefManager.clearAuthResponse();
    }

    public interface Callback {
        void onTokenValid(AuthenticationResponse auth);
    }
}
