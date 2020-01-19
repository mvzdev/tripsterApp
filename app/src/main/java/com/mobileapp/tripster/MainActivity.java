package com.mobileapp.tripster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.mobileapp.tripster.services.ConnectionFinder;

import butterknife.BindView;
import butterknife.ButterKnife;

import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.text_departure)
    EditText departureTextField;

    @BindView(R.id.text_destination)
    EditText destinationTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.search)
    public void onSearchClick() {

        String from = departureTextField.getText().toString();
        String to = destinationTextField.getText().toString();

        ConnectionFinder finder = new ConnectionFinder();

        finder.findConnections(from, to);

        // TODO: change findConnections to return data instead of logging
        // TODO: navigate to new activity with data from findConnections


    }

}
