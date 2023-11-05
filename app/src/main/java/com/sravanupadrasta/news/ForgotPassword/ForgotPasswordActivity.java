package com.praveenupadrasta.news.ForgotPassword;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.praveenupadrasta.news.Login.LoginActivity;
import com.praveenupadrasta.news.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Miscellaneous.DialogBox;
import Miscellaneous.Utils;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText txtEmail = null;
    private TextView btnResetPassword = null;
    private TextView lblErrorEmail = null;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, LoginActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    private void initViews()
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        txtEmail = (EditText) findViewById(R.id.txt_email);
        btnResetPassword = (TextView) findViewById(R.id.btnResetPassword);
        lblErrorEmail = (TextView) findViewById(R.id.lbl_error_email);
        btnResetPassword.setOnClickListener(resetPassword_OnClickListener);
    }

    View.OnClickListener resetPassword_OnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            ArrayList<Integer> idList = null;
            idList = Utils.getEmptyFields(getWindow(), R.id.txt_email);
            if (idList.isEmpty()) {
                lblErrorEmail.setBackgroundResource(R.drawable.edittext_modified_states);
                new ResetPasswordTask(ForgotPasswordActivity.this).execute();
            }
            else
            {
                Utils.showErrorMsg(getWindow(), "Email Field left empty!",
                        R.id.lbl_error_email, idList);
            }
        }
    };

    public byte[] getForgotPasswordData() throws JSONException
    {
        JSONObject forgotPasswordJson = new JSONObject();
        forgotPasswordJson.put("username", txtEmail.getText().toString());
        return forgotPasswordJson.toString().getBytes();
    }

    public void updateView(JSONObject response)
    {
        try {
            String msg = "";
            if (response.has("username")) {
                String temp = response.get("username").toString();
                msg += "Email: " + temp.substring(2, temp.length() - 2) + "\n";
                txtEmail.setBackgroundResource(R.drawable.edittext_error_background);
            }
            lblErrorEmail.setText(msg);
            lblErrorEmail.setVisibility(View.VISIBLE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void resetPasswordSuccessAction(String response)
    {
        Dialog success = new DialogBox().createDialogBox(response, context);
        success.show();
    }
}
