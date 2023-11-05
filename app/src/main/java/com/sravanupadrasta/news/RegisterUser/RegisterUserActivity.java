package com.praveenupadrasta.news.RegisterUser;

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

import org.json.JSONObject;

import java.util.ArrayList;

import Miscellaneous.DialogBox;
import Miscellaneous.Utils;

public class RegisterUserActivity extends AppCompatActivity {

    private EditText txtEmail = null;
    private EditText txtPassword = null;
    private TextView btnRegister = null;
    private TextView lblErrorEmail = null;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
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

        txtEmail    = (EditText) findViewById(R.id.txt_email);
        txtPassword = (EditText) findViewById(R.id.txt_password);
        btnRegister = (TextView) findViewById(R.id.btnRegister);
        lblErrorEmail = (TextView) findViewById(R.id.lbl_error_email);
        btnRegister.setOnClickListener(register_OnClickListener);
    }

    View.OnClickListener register_OnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v)
        {
            ArrayList<Integer> idList = null;
            idList = Utils.getEmptyFields(getWindow(), R.id.txt_email, R.id.txt_password);
            if (idList.isEmpty()) {
                txtEmail.setBackgroundResource(R.drawable.edittext_modified_states);
                txtPassword.setBackgroundResource(R.drawable.edittext_modified_states);
                byte[] registerData = getRegisterData();
                new RegisterUserTask(RegisterUserActivity.this, registerData).execute();
            }
            else
            {
                Utils.showErrorMsg(getWindow(), "One or more fields left empty!",
                        R.id.lbl_error_email, idList);
            }
        }
    };

    public byte[] getRegisterData()
    {
        JSONObject registerData = null;
        try {
            registerData = new JSONObject();
            registerData.put("username", txtEmail.getText().toString());
            registerData.put("password", txtPassword.getText().toString());

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return registerData.toString().getBytes();
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
            if (response.has("password")) {
                String temp = response.get("password").toString();
                msg += "Password: " + temp.substring(2, temp.length() - 2) + "\n";
                txtPassword.setBackgroundResource(R.drawable.edittext_error_background);
            }
            lblErrorEmail.setText(msg);
            lblErrorEmail.setVisibility(View.VISIBLE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void registerUserSuccessAction()
    {
        Dialog success = new DialogBox().createDialogBox("User registration successfull", context);
        success.show();
        TextView btnOk = (TextView) success.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(context, LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
