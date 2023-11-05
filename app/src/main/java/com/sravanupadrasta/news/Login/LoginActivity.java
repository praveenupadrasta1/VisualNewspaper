package com.praveenupadrasta.news.Login;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.praveenupadrasta.news.FCM.FirebaseIDService;
import com.praveenupadrasta.news.ForgotPassword.ForgotPasswordActivity;
import com.praveenupadrasta.news.DeviceLocations.MainActivity;
import com.praveenupadrasta.news.R;
import com.praveenupadrasta.news.RegisterUser.RegisterUserActivity;

import org.json.JSONObject;

import java.util.ArrayList;

import Miscellaneous.Utils;


public class LoginActivity extends AppCompatActivity {

    private EditText txtEmail = null;
    private EditText txtPassword = null;
    private TextView btnLogin = null;
    private TextView btnRegisterUser = null;
    private TextView btnForgotPassword = null;
    private TextView lblErrorEmail = null;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeViews();
    }

    private void initializeViews()
    {
        txtEmail = (EditText) findViewById(R.id.txt_email);
        txtPassword = (EditText) findViewById(R.id.txt_password);
        btnLogin = (TextView) findViewById(R.id.btnLogin);
        btnRegisterUser = (TextView) findViewById(R.id.register_user);
        btnForgotPassword = (TextView) findViewById(R.id.forgot_password);
        lblErrorEmail = (TextView) findViewById(R.id.lbl_error_email);
        btnLogin.setOnClickListener(login_OnClickListener);
        btnRegisterUser.setOnClickListener(registerUser_OnClickListener);
        btnForgotPassword.setOnClickListener(forgotPassword_OnClickListener);
        isMyServiceRunning(FirebaseIDService.class);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    View.OnClickListener login_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                ArrayList<Integer> idList = null;
                idList = Utils.getEmptyFields(getWindow(), R.id.txt_email, R.id.txt_password);
                if (idList.isEmpty()) {
                    byte[] urlParams = getLoginDataFromView();
                    new LoginTask(LoginActivity.this, urlParams).execute();
                }
                else
                {
                    Utils.showErrorMsg(getWindow(), "One or more fields left empty!",
                                    R.id.lbl_error_email, idList);
                }
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener registerUser_OnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v)
        {
            Intent regUserIntent = new Intent(getApplicationContext(), RegisterUserActivity.class);
            startActivity(regUserIntent);
        }
    };

    View.OnClickListener forgotPassword_OnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v)
        {
            Intent regUserIntent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
            startActivity(regUserIntent);
        }
    };

    public void updateView(String response)
    {
        lblErrorEmail.setText(response);
        lblErrorEmail.setVisibility(View.VISIBLE);
        txtEmail.setBackgroundResource(R.drawable.edittext_error_background);
        txtPassword.setBackgroundResource(R.drawable.edittext_error_background);
    }

    public byte[] getLoginDataFromView()
    {
        byte[] urlParams = null;
        try {
            JSONObject loginData = new JSONObject();
            loginData.put("username", txtEmail.getText().toString());
            loginData.put("password", txtPassword.getText().toString());
            loginData.put("deviceId", Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID));
            loginData.put("deviceType", "Android");
            urlParams = loginData.toString().getBytes();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return urlParams;
    }

    public void loginUserSuccessAction(JSONObject response)
    {
        new LoginModel(getApplicationContext()).storeAccessToken(response);
        Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
