package com.mobileapp.tripster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConnectionAdapter extends BaseAdapter {

    private Context context;
    private List<Connection> connections;
    public static final String DATE_FORMAT_1 = "HH:mm ";


    public ConnectionAdapter(Context context, List<Connection> connections) {
        this.context = context;
        this.connections = connections;
    }

    @Override
    public int getCount() {
        return connections.size();
    }

    @Override
    public Object getItem(int position) {
        return connections.get(position);
    }

    @Override
    public long getItemId(int position) {
        Connection connection = (Connection) getItem(position);
        return connection.getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.connection_item, parent, false);
        }

        Connection connection = (Connection) getItem(position);
        TextView departureTime = convertView.findViewById(R.id.connection_departure_time);
        departureTime.setText(getFormattedDate(connection.getDeparture()));

        TextView arrivalTime = convertView.findViewById(R.id.connection_arrival_time);
        arrivalTime.setText( getFormattedDate(connection.getArrival()));

        return convertView;
    }

    private String getFormattedDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_1);
        //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }
}
