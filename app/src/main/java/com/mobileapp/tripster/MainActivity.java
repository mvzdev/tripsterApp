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
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.mobileapp.tripster.api.ApiClient;
import com.mobileapp.tripster.dtos.ConnectionContainerDto;
import com.mobileapp.tripster.dtos.ConnectionDto;
import com.mobileapp.tripster.services.ConnectionServiceInterface;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient client;
    private Geocoder geocoder;
    private LocationCallback locationCallback;

    @BindView(R.id.text_departure)
    EditText departureTextField;

    @BindView(R.id.text_destination)
    EditText destinationTextField;

    @BindView(R.id.connection_list_view)
    ListView connectionListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Location related
        // TODO: move to it's own class
        this.setLocation();

    }

    @OnClick(R.id.search)
    public void onSearchClick() {
        // TODO: navigate to new activity with data from findConnections
        // TODO: Show progress bar

        String from = departureTextField.getText().toString();
        String to = destinationTextField.getText().toString();

//        ConnectionFinder finder = new ConnectionFinder();
//        finder.findConnections(from, to);

        Retrofit retrofit = ApiClient.getClient();
        ConnectionServiceInterface connectionServiceInterface = retrofit.create(ConnectionServiceInterface.class);

        Call<ConnectionContainerDto> call = connectionServiceInterface.searchConnections(from, to);
        call.enqueue(new Callback<ConnectionContainerDto>() {

            @Override
            public void onResponse(Call<ConnectionContainerDto> call, Response<ConnectionContainerDto> response) {

                if (response.body() != null) {
                    List<ConnectionDto> connectionDtos = response.body().connections;

                    List<Connection> connections = connectionDtos.stream()
                            .map(c -> new Connection(c.from.departure, c.to.arrival))
                            .collect(Collectors.toList());

                    ConnectionAdapter connectionAdapter = new ConnectionAdapter(MainActivity.this, connections);
                    connectionListView.setAdapter(connectionAdapter);
                }
            }

            @Override
            public void onFailure(Call<ConnectionContainerDto> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.getLastLocation().addOnSuccessListener((Location location) -> updateLocationOnUi(location));
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
