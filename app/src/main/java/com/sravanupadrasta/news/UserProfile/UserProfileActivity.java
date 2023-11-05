package com.praveenupadrasta.news.UserProfile;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.praveenupadrasta.news.DeviceLocations.MainActivity;
import com.praveenupadrasta.news.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;

import Helper.HTTPConnection;
import Miscellaneous.Config;
import Miscellaneous.DialogBox;
import Miscellaneous.Utils;

public class UserProfileActivity extends AppCompatActivity {

    private TextView lblName = null;
    private TextView lblAddress = null;
    private TextView lblLocation = null;
    private TextView lblContact = null;
    private ImageView imgBtnEditProfile = null;
    private ImageView imgBtnChangePassword = null;
    private ConstraintLayout layoutChangePasswordInfo = null;
    private TextView btnChangePassword = null;
    private TextView btnCancelChangePassword = null;
    private EditText txtCurrentPassword = null;
    private EditText txtNewPassword = null;
    private EditText txtConfirmPassword = null;
    private Context context = this;
    private TextView lblErrorCurrentPassword = null;
    private TextView lblErrorConfirmPassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, MainActivity.class);
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

        lblName = (TextView) findViewById(R.id.lbl_name_value);
        lblAddress = (TextView) findViewById(R.id.lbl_address_value);
        lblContact = (TextView) findViewById(R.id.lbl_contact_value);
        lblLocation = (TextView) findViewById(R.id.lbl_location_value);
        imgBtnChangePassword = (ImageView) findViewById(R.id.imgbtn_change_password);
        layoutChangePasswordInfo = (ConstraintLayout) findViewById(R.id.layout_constraint_change_password_info);
        btnChangePassword = (TextView) findViewById(R.id.btn_change);
        btnCancelChangePassword = (TextView) findViewById(R.id.btn_cancel);
        txtCurrentPassword = (EditText) findViewById(R.id.txt_current_password);
        txtNewPassword = (EditText) findViewById(R.id.txt_new_password);
        txtConfirmPassword = (EditText) findViewById(R.id.txt_confirm_password);
        imgBtnEditProfile = (ImageView) findViewById(R.id.imgbtn_edit_profile);
        lblErrorCurrentPassword = (TextView) findViewById(R.id.lbl_error_current_password);
        lblErrorConfirmPassword = (TextView) findViewById(R.id.lbl_error_confirm_password);

        imgBtnEditProfile.setOnClickListener(imgBtnEditProfile_OnClickListener);
        imgBtnChangePassword.setOnClickListener(imgChangePassword_OnClickListener);
        btnChangePassword.setOnClickListener(changePassword_OnClickListener);
        btnCancelChangePassword.setOnClickListener(cancelChangePassword_OnClickListener);
        new GetUserProfileTask(UserProfileActivity.this, context).execute();
    }

    View.OnClickListener imgBtnEditProfile_OnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent editProfileActivity = new Intent(context, EditUserProfileActivity.class);
            editProfileActivity.putExtra("name", lblName.getText().toString());
            editProfileActivity.putExtra("location", lblLocation.getText().toString());
            editProfileActivity.putExtra("address", lblAddress.getText().toString());
            editProfileActivity.putExtra("contact", lblContact.getText().toString());
            startActivity(editProfileActivity);
        }
    };

    View.OnClickListener imgChangePassword_OnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
            layoutChangePasswordInfo.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener changePassword_OnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v)
        {
            if(isNewAndConfirmPasswordEqual()){
                new ChangePasswordTask(UserProfileActivity.this, context, getChangePasswordData()).execute();
            }
            else
            {
                txtNewPassword.setBackgroundResource(R.drawable.edittext_error_background);
                txtConfirmPassword.setBackgroundResource(R.drawable.edittext_error_background);
                txtNewPassword.setText("");
                txtConfirmPassword.setText("");
                lblErrorConfirmPassword.setText("New and Confirm passwords mismatch!");
                lblErrorConfirmPassword.setVisibility(View.VISIBLE);
            }
        }
    };

    View.OnClickListener cancelChangePassword_OnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v)
        {
            txtCurrentPassword.setText("");
            txtNewPassword.setText("");
            txtConfirmPassword.setText("");
            layoutChangePasswordInfo.setVisibility(View.GONE);
        }
    };

    public void updateUserProfileView(JSONObject profile)
    {
        try {
            lblName.setText(profile.get("name").toString());
            lblLocation.setText(profile.get("location").toString());
            lblAddress.setText(profile.get("address").toString());
            lblContact.setText(profile.get("contact").toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private byte[] getChangePasswordData()
    {
        byte[] data = null;
        try {
            JSONObject changePasswordJson = new JSONObject();
            changePasswordJson.put("old_password",txtCurrentPassword.getText().toString());
            changePasswordJson.put("new_password", txtNewPassword.getText().toString());
            data = changePasswordJson.toString().getBytes();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return data;
    }

    private boolean isNewAndConfirmPasswordEqual()
    {
        if(txtNewPassword.getText().toString().equals(txtConfirmPassword.getText().toString()))
            return true;
        return false;
    }

    public void updateView(int resCode, JSONObject response)
    {
        try {
            if (resCode == 200) {
                Dialog success = new DialogBox().createDialogBox("Password upated!", context);
                success.show();
            } else if (resCode == 403) {
                txtCurrentPassword.setBackgroundResource(R.drawable.edittext_error_background);
                txtCurrentPassword.setText("");
                lblErrorCurrentPassword.setText(response.get("details").toString());
                lblErrorCurrentPassword.setVisibility(View.VISIBLE);
            } else if (resCode == 400) {
                txtNewPassword.setBackgroundResource(R.drawable.edittext_error_background);
                txtConfirmPassword.setBackgroundResource(R.drawable.edittext_error_background);
                txtNewPassword.setText("");
                txtConfirmPassword.setText("");
                lblErrorConfirmPassword.setText(response.get("details").toString());
                lblErrorConfirmPassword.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
