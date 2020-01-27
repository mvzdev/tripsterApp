package com.mobileapp.tripster;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.mobileapp.tripster.viewmodels.ConnectionViewModel;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int NUMBER_OF_CONNECTIONS = 3;

    private FusedLocationProviderClient client;
    private Geocoder geocoder;
    private LocationCallback locationCallback;
    private ConnectionViewModel connectionsViewModel;

    @BindView(R.id.text_departure)
    EditText departureTextField;

    @BindView(R.id.text_destination)
    EditText destinationTextField;

    @BindView(R.id.connection_list_view)
    ListView connectionListView;

    @BindView(R.id.indeterminateBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        progressBar.setVisibility(View.GONE);

        // Location related
        // TODO: move to it's own class
        this.setLocation();


        this.connectionsViewModel = ViewModelProviders.of(this)
                .get(ConnectionViewModel.class);

        this.connectionsViewModel.getConnections().observe(this, connections -> {
            ConnectionAdapter connectionAdapter = new ConnectionAdapter(MainActivity.this, connections);
            connectionListView.setAdapter(connectionAdapter);
        });
    }

    @OnClick(R.id.search)
    public void onSearchClick() {
        progressBar.setVisibility(View.VISIBLE);

        String from = departureTextField.getText().toString();
        String to = destinationTextField.getText().toString();
        connectionsViewModel.searchLimitedConnections(from, to, NUMBER_OF_CONNECTIONS);

        this.connectionsViewModel.getConnections().observe(this, connections -> {
            ConnectionAdapter connectionAdapter = new ConnectionAdapter(MainActivity.this, connections);
            connectionListView.setAdapter(connectionAdapter);
            progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.getLastLocation().addOnSuccessListener((Location location) -> updateLocationOnUi(location));

        this.connectionsViewModel.getConnections().observe(this, connections -> {
            ConnectionAdapter connectionAdapter = new ConnectionAdapter(MainActivity.this, connections);
            connectionListView.setAdapter(connectionAdapter);
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        client.flushLocations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void updateLocationOnUi(Location location) {
        if (location != null) {
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                departureTextField.setText(addresses.get(0).getAddressLine(0));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setLocation() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    updateLocationOnUi(location);
                }
            }
        };

        client = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this);
    }

}
