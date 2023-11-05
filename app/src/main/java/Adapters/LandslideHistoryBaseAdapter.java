package Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.praveenupadrasta.news.DetectionAndHistory.LandslideHistory.LandslideHistoryRowItem;
import com.praveenupadrasta.news.R;

import java.util.List;

import pl.pawelkleczkowski.customgauge.CustomGauge;

/**
 * Created by praveenupadrasta on 23-06-2017.
 */

public class LandslideHistoryBaseAdapter extends BaseAdapter {

    Context context;
    List<LandslideHistoryRowItem> rowItems;
    LayoutInflater mInflater;
    public LandslideHistoryBaseAdapter(Context context, List<LandslideHistoryRowItem> items)
    {
        this.context = context;
        this.rowItems = items;
        mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.landslide_history_row_item, null);
            holder = new ViewHolder();
            holder.txtDateTime = (TextView) convertView.findViewById(R.id.txtDateTimeValue);
            holder.txtLatitudeLongitude = (TextView) convertView.findViewById(R.id.txtLatitudeLongitude);
            holder.txtRainfall = (TextView) convertView.findViewById(R.id.txtRainfall);
            holder.txtMoistureValue = (TextView) convertView.findViewById(R.id.lblMoistureValue);
            holder.txtMagnitudeValue = (TextView) convertView.findViewById(R.id.lblMagnitudeValue);
            holder.moistureLevel = (CustomGauge) convertView.findViewById(R.id.gaugeMoistureLevel);
            holder.magnitude = (CustomGauge) convertView.findViewById(R.id.gaugeMagnitudeLevel);

            holder.moistureLevel.setPointStartColor(Color.GREEN);
            holder.moistureLevel.setPointEndColor(Color.RED);
            holder.moistureLevel.setStartValue(0);
            holder.moistureLevel.setEndValue(1000);
            holder.moistureLevel.setStartAngle(180);

            holder.magnitude.setPointStartColor(Color.GREEN);
            holder.magnitude.setPointEndColor(Color.RED);
            holder.magnitude.setStartValue(0);
            holder.magnitude.setEndValue(2000);
            holder.magnitude.setStartAngle(180);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        LandslideHistoryRowItem rowItem = (LandslideHistoryRowItem) getItem(position);
        String dateTime = rowItem.getDateTime();
        dateTime = dateTime.substring(0, dateTime.length()-1);
        String temp[] = dateTime.split("T");
        holder.txtDateTime.setText(temp[0]+" "+temp[1]+" UTC");
        holder.txtLatitudeLongitude.setText(rowItem.getLatitude()+","+rowItem.getLongitude());
        holder.txtRainfall.setText(rowItem.getRainfall());
        holder.txtMagnitudeValue.setText(String.format ("%.2f", rowItem.getMagnitude()));//((Integer)(rowItem.getMagnitude().intValue() * 1000)).toString());
        holder.txtMoistureValue.setText(String.format ("%.2f", rowItem.getMoistureLevel()));
        holder.moistureLevel.setValue((int)(rowItem.getMoistureLevel() * 1000));

        holder.magnitude.setValue((int)(rowItem.getMagnitude() * 1000));
        return convertView;
    }

    private class ViewHolder {
        TextView txtDateTime;
        TextView txtLatitudeLongitude;
        TextView txtRainfall;
        TextView txtMoistureValue;
        TextView txtMagnitudeValue;
        CustomGauge moistureLevel;
        CustomGauge magnitude;
    }

}
