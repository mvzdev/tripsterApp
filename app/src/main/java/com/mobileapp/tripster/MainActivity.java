package com.mobileapp.tripster;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.mobileapp.tripster.location.LocationManager;
import com.mobileapp.tripster.model.Connection;
import com.mobileapp.tripster.viewmodels.ConnectionViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final int NUMBER_OF_CONNECTIONS = 3;

    private LocationManager locationManager;
    private ConnectionViewModel connectionsViewModel;

    @BindView(R.id.text_departure)
    EditText departureTextField;

    @BindView(R.id.text_destination)
    EditText destinationTextField;

    @BindView(R.id.time_input)
    EditText timeInput;

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

        locationManager = new LocationManager(this);
        connectionsViewModel = ViewModelProviders.of(this).get(ConnectionViewModel.class);

    }

    @OnClick(R.id.current_location)
    public void onCurrentLocationClick() {
        this.checkForLocationPermission();
        this.locationManager.getLastKnownLocation().observe(this, lastLocation -> {
            departureTextField.setText(lastLocation);
        });
    }

    @OnClick(R.id.switch_selection)
    public void onSwitchSelectionClick() {
        String departure = departureTextField.getText().toString();
        String destination = destinationTextField.getText().toString();
        departureTextField.setText(destination);
        destinationTextField.setText(departure);

    }

    @OnClick(R.id.submit_search)
    public void onSearchClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        view.setEnabled(false);

        String from = departureTextField.getText().toString();
        String to = destinationTextField.getText().toString();
        String time = timeInput.getText().toString();
        connectionsViewModel.searchLimitedConnections(from, to, time, NUMBER_OF_CONNECTIONS);

        connectionsViewModel.connections.observe(this, connections -> {
            setupConnectionAdapter(connections);
            progressBar.setVisibility(View.GONE);
            view.setEnabled(true);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.locationManager.flushLocations();
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

    private void setupConnectionAdapter(List<Connection> connections) {
            ConnectionAdapter connectionAdapter = new ConnectionAdapter(MainActivity.this, connections);
            connectionListView.setAdapter(connectionAdapter);
    }


    public void checkForLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            new AlertDialog.Builder(this)
                    .setTitle(R.string.grant_permission_location_title)
                    .setMessage(R.string.grant_permission_location)
                    .setPositiveButton(R.string.button_okay, (dialog, which) -> ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1))
                    .setNegativeButton(R.string.button_cancel, (dialog, which) -> dialog.dismiss())
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast toast = Toast.makeText(this, R.string.permission_granted_location, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toast = Toast.makeText(this, R.string.permission_denied_location, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
