package com.epam.roman.presencesdktestintegration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.ticketmaster.presencesdk.ExperienceConfiguration;
import com.ticketmaster.presencesdk.PresenceSDK;
import com.ticketmaster.presencesdk.login.PresenceLoginListener;
import com.ticketmaster.presencesdk.login.PresenceSdkConfigListener;
import com.ticketmaster.presencesdk.login.TMLoginApi;
import com.ticketmaster.presencesdk.login.UserInfoManager;

public class MainActivity extends AppCompatActivity implements
        PresenceSdkConfigListener,
        PresenceLoginListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    // AppCompatActivity methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        configurePresenceSDK();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // PresenceSdkConfigListener methods

    @Override
    public void onPresenceSdkConfigSuccessful() {
        Log.e(TAG, "onPresenceSdkConfigSuccessful");

        launchPresenceSDK();

//        getPresenceSdk().startLoginFlow(TMLoginApi.BackendName.ARCHTICS, this);
//        getPresenceSdk().startLoginFlow(TMLoginApi.BackendName.HOST, this);

        configureExperienceSDK();
    }

    @Override
    public void onPresenceSdkConfigFailed(String s) {
        Log.e(TAG, "onPresenceSdkConfigFailed");
    }

    // TMLoginListener methods

    @Override
    public void onLoginSuccessful(TMLoginApi.BackendName backendName,
                                  String accessToken) {
        Log.e(TAG, "Inside onLoginSuccessful");
    }

    @Override
    public void onLoginFailed(TMLoginApi.BackendName backendName,
                              String errorMessage) {
        Log.e(TAG, "Inside onLoginFailed");
    }

    @Override
    public void onLoginCancelled(TMLoginApi.BackendName backendName) {
        Log.e(TAG, "Inside onLoginCancelled");
    }

    @Override
    public void onLoginMethodUsed(TMLoginApi.BackendName backendName,
                                  TMLoginApi.LoginMethod method) {
        Log.e(TAG, "Inside onLoginMethodUsed");
    }

    @Override
    public void onLoginForgotPasswordClicked(TMLoginApi.BackendName backendName) {
        Log.e(TAG, "Inside onLoginForgotPasswordClicked");
    }

    @Override
    public void onCacheCleared() {
        Log.e(TAG, "Inside onCacheCleared");
    }

    @Override
    public void onMemberUpdated(TMLoginApi.BackendName backendName,
                                @Nullable UserInfoManager.MemberInfo memberInfo) {
        Log.e(TAG, "Inside onMemberUpdated");
    }

    @Override
    public void onLogoutSuccessful(TMLoginApi.BackendName backendName) {
        Log.e(TAG, "Inside onLogoutSuccessful");
    }

    @Override
    public void onLogoutAllSuccessful() {
        Log.e(TAG, "Inside onLogoutAllSuccessful");
    }

    @Override
    public void onTokenRefreshed(TMLoginApi.BackendName backendName, String s) {

    }

    @Override
    public void onRefreshTokenFailed(TMLoginApi.BackendName backendName) {

    }

    @Override
    public void onLoginWindowDidDisplay(TMLoginApi.BackendName backendName) {

    }

    // Utility methods

    private void configurePresenceSDK() {
        getPresenceSdk().registerConfigListener(this);
        getPresenceSdk().setConfig(
                "YOUR_CONSUMER_KEY",
                "YOUR_TEAM_NAME",
                true);
    }

    private void launchPresenceSDK() {
        getPresenceSdk().start(this, R.id.presenceSDKView, this);
    }

    private void configureExperienceSDK() {
        // specify these parameters to integrate ExperienceSDK
        // and pass them into PresenceSDK
        // this will NOT crash if no ExperienceSDK lib provided
        // presenceSDK.start() will check the availability of ExperienceSDK lib
        // setSsoSigningKey() is optional, allows to use Pinless Feature
        // to disable pin prompt within add, return, and upgrade buttons.
        ExperienceConfiguration wrapper = new ExperienceConfiguration.Builder(
                "YOUR_SUB_DOMAIN",
                "YOUR_APPID",
                "YOUR_APPNAME",
                "YOUR_APPSOURCE")
                .setApiKey("YOUR_APIKEY")
                .setApiSubdomain("YOUR_API_SUBDOMAIN")
                .setSsoSigningKey(null)
                .build();

        getPresenceSdk().setExperienceConfiguration(wrapper);
    }

    public PresenceSDK getPresenceSdk() {
        return PresenceSDK.getPresenceSDK(getApplicationContext());
    }
}
