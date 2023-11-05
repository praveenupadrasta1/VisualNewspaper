package com.praveenupadrasta.news.UserProfile;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.praveenupadrasta.news.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import Helper.HTTPConnection;
import Miscellaneous.Config;
import Miscellaneous.DialogBox;
import Miscellaneous.Utils;

public class EditUserProfileActivity extends AppCompatActivity {

    private Spinner spinnerLocation = null;
    private EditText txtFirstName = null;
    private EditText txtLastName = null;
    private EditText txtAddress = null;
    private EditText txtContact = null;
    private Context context = this;
    private List<String> locationName = null;
    private List<String> locationUuid = null;
    private TextView btnEditProfile = null;
    private  ArrayAdapter<String> locationAdapter = null;
    private String locationSelected = null;
    private TextView lblErrorMsg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        initViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, UserProfileActivity.class);
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

        spinnerLocation = (Spinner) findViewById(R.id.spinner_location);
        txtFirstName = (EditText) findViewById(R.id.txt_first_name);
        txtLastName = (EditText) findViewById(R.id.txt_last_name);
        txtAddress = (EditText) findViewById(R.id.txt_address);
        txtContact = (EditText) findViewById(R.id.txt_phone_num);
        btnEditProfile = (TextView) findViewById(R.id.btn_edit_profile);
        lblErrorMsg = (TextView) findViewById(R.id.lbl_error_msg);
        btnEditProfile.setOnClickListener(btnEditProfile_OnClickListener);

        locationName = new ArrayList<>();
        locationUuid = new ArrayList<>();

        new GetLocationsTask(EditUserProfileActivity.this).execute();

        locationAdapter = new ArrayAdapter<String>(EditUserProfileActivity.this,android.R.layout.simple_spinner_item, locationName);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(locationAdapter);
        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locationSelected = locationUuid.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        populateData();
    }

    private void populateData()
    {
        String name = getIntent().getStringExtra("name");
        String[] temp = name.split(" ");
        String lastName = temp[temp.length-1];
        String firstName = name.substring(0, name.length() - lastName.length());
        String location = getIntent().getStringExtra("location");
        String address = getIntent().getStringExtra("address");
        String contact = getIntent().getStringExtra("contact");

        txtFirstName.setText(firstName);
        txtLastName.setText(lastName);
        txtContact.setText(contact);
        txtAddress.setText(address);

        int index = getLocationIndex(location);
        if(index != -1) {
            spinnerLocation.setSelection(index);
            locationSelected = locationUuid.get(index);
        }
    }

    private int getLocationIndex(String location)
    {
        return locationName.indexOf(location);
    }

    private void resetFields()
    {
        txtFirstName.setBackgroundResource(R.drawable.edittext_modified_states);
        txtLastName.setBackgroundResource(R.drawable.edittext_modified_states);
        txtAddress.setBackgroundResource(R.drawable.edittext_modified_states);
        txtContact.setBackgroundResource(R.drawable.edittext_modified_states);
        lblErrorMsg.setText("");
        lblErrorMsg.setVisibility(View.GONE);
    }

    View.OnClickListener btnEditProfile_OnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
            resetFields();
            List<Integer> listNullFields = getNullFields();
            if(listNullFields.isEmpty()) {
                new EditProfileTask(EditUserProfileActivity.this).execute();
            }
            else
            {
                for(Integer id : listNullFields)
                {
                    String viewName = findViewById(id).toString();
                    String resourcetype = viewName.substring(0,viewName.indexOf("{"));
                    if(!resourcetype.equals("android.support.v7.widget.AppCompatSpinner"))
                        findViewById(id).setBackgroundResource(R.drawable.edittext_error_background);
                }
                lblErrorMsg.setText("One or More fields left blank");
                lblErrorMsg.setVisibility(View.VISIBLE);
            }
        }
    };

    private List<Integer> getNullFields()
    {
        List<Integer> nullFields = new ArrayList<>();
        if(txtFirstName.getText().toString().equals(""))
            nullFields.add(txtFirstName.getId());
        if(txtLastName.getText().toString().equals(""))
            nullFields.add(txtLastName.getId());
        if(txtContact.getText().toString().equals(""))
            nullFields.add(txtContact.getId());
        if(txtAddress.getText().toString().equals(""))
            nullFields.add(txtAddress.getId());
        if(locationSelected == null || locationSelected.equals(""))
            nullFields.add(spinnerLocation.getId());
        return nullFields;
    }

    private byte[] frameJsonData() throws Exception
    {
        JSONObject editUserProfileJson = new JSONObject();
        editUserProfileJson.put("first_name", txtFirstName.getText().toString());
        editUserProfileJson.put("last_name", txtLastName.getText().toString());
        editUserProfileJson.put("location", locationSelected);
        editUserProfileJson.put("address", txtAddress.getText().toString());
        editUserProfileJson.put("contact", txtContact.getText().toString());
        return editUserProfileJson.toString().getBytes();
    }

    private class EditProfileTask extends AsyncTask<Void, Void, JSONObject>
    {
        private ProgressDialog pd = null;
        public EditProfileTask(EditUserProfileActivity editUserProfileActivity)
        {
            pd = new ProgressDialog(editUserProfileActivity);
        }

        @Override
        protected void onPreExecute()
        {
            pd.setMessage("Loading");
            pd.show();
        }

        @Override
        protected JSONObject doInBackground(Void... params)
        {
            JSONObject response = null;
            HttpURLConnection urlConnection = null;
            try {
                byte[] editProfileData = frameJsonData();
                urlConnection = new HTTPConnection().doPut(Config.editProfile, editProfileData);
                String accessToken = Utils.getAccessToken(context);
                urlConnection.setRequestProperty("Auth", accessToken);

                OutputStream os = urlConnection.getOutputStream();
                os.write(editProfileData);
                os.close();
                urlConnection.connect();
                response = Utils.readResponse(urlConnection);
                response.put("status_code", urlConnection.getResponseCode());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(JSONObject response)
        {
            pd.dismiss();
            try {
                if ((int)response.get("status_code") == 200 || (int)response.get("status_code") == 201) {
                    Dialog success = new DialogBox().createDialogBox("Profile edited successfully!", context);
                    success.show();
                    success.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view)
                        {
                            Intent intent = new Intent(context, UserProfileActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                else
                {
                    String errorMsg = "";
                    if(response.has("first_name")) {
                        txtFirstName.setBackgroundResource(R.drawable.edittext_error_background);
                        String msg = response.get("first_name").toString();
                        errorMsg += msg.substring(2, msg.length()-2) + "\n";
                    }
                    if(response.has("last_name")) {
                        txtLastName.setBackgroundResource(R.drawable.edittext_error_background);
                        String msg = response.get("last_name").toString();
                        errorMsg += msg.substring(2, msg.length()-2) + "\n";
                    }
                    if(response.has("address")) {
                        txtAddress.setBackgroundResource(R.drawable.edittext_error_background);
                        String msg = response.get("address").toString();
                        errorMsg += msg.substring(2, msg.length()-2) + "\n";
                    }
                    if(response.has("location")) {
                        String msg = response.get("location").toString();
                        errorMsg += msg.substring(2, msg.length()-2) + "\n";
                    }
                    if(response.has("contact")) {
                        txtContact.setBackgroundResource(R.drawable.edittext_error_background);
                        String msg = response.get("contact").toString();
                        errorMsg += msg.substring(2, msg.length()-2) + "\n";
                    }
                    lblErrorMsg.setText(errorMsg);
                    lblErrorMsg.setVisibility(View.VISIBLE);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private class GetLocationsTask extends AsyncTask<Void, Void, Void>
    {
        private ProgressDialog pd = null;
        public GetLocationsTask(EditUserProfileActivity editUserProfileActivity)
        {
            pd = new ProgressDialog(editUserProfileActivity);
            pd.setMessage("Loading");
        }

        @Override
        protected void onPreExecute()
        {
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try {
                HttpURLConnection urlConnection = new HTTPConnection().doGet(Config.getLocations);
                urlConnection.setRequestProperty("Auth", Utils.getAccessToken(context));
                urlConnection.connect();
                JSONObject locationJson = Utils.readResponse(urlConnection);
                populateLocationData(locationJson);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param)
        {
            pd.dismiss();
        }
    }

    private void populateLocationData(JSONObject locationJson) throws Exception
    {
        JSONArray locationArray = locationJson.getJSONArray("details");
        for(int i=0; i<locationArray.length(); i++)
        {
            JSONObject tempJson = locationArray.getJSONObject(i);
            locationName.add(tempJson.getString("name"));
            locationUuid.add(tempJson.getString("uuid"));
        }
        locationAdapter.notifyDataSetChanged();
    }
}
