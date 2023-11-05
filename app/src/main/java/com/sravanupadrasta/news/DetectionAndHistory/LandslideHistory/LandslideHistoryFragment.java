package com.praveenupadrasta.news.DetectionAndHistory.LandslideHistory;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.praveenupadrasta.news.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapters.LandslideHistoryBaseAdapter;
import Miscellaneous.Utils;

public class LandslideHistoryFragment extends Fragment {

    private ListView listLandslideHistory;
    private Context context;
    private String access_token;
    private String locationName;
    private List<LandslideHistoryRowItem> rowItems;
    private LandslideHistoryBaseAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_landslide_history, container, false);
        context = getContext();
        initViews(view);
        getLandslideHistory();
        return view;
    }

    private void initViews(View view)
    {
        listLandslideHistory = (ListView) view.findViewById(R.id.listLandslideHistory);
        rowItems = new ArrayList<>();
        Bundle args = getArguments();
        locationName = args.get("location").toString();
        access_token = Utils.getAccessToken(context);
    }

    private void getLandslideHistory()
    {
        new LandslideHistoryAsyncTask(LandslideHistoryFragment.this, context).execute();
    }

    public void updateView(JSONObject response)
    {
        try {
            if((int)response.get("status_code") == 200) {
                JSONArray history = response.getJSONArray("details");
                for(int i=0; i<history.length(); i++)
                {
                    JSONObject temp = (JSONObject) history.get(i);
                    String dateTime = temp.get("pk").toString();
                    JSONObject fields = (JSONObject)temp.get("fields");
                    String isRainfall = (boolean) fields.get("rainfall")? "Yes":"No";
                    double accelerometer_level = (double) fields.get("accelerometer_level");
                    String latitude = fields.get("latitude").toString();
                    String longitude = fields.get("longitude").toString();
                    double moistureLevel = (double) fields.get("moisture_level");
                    LandslideHistoryRowItem item = new LandslideHistoryRowItem(dateTime,
                            latitude, longitude, isRainfall, moistureLevel,
                            accelerometer_level);
                    rowItems.add(item);
                }
            }
            adapter = new LandslideHistoryBaseAdapter(context, rowItems);
            listLandslideHistory.setAdapter(adapter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
