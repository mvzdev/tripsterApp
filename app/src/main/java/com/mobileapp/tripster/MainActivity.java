package com.mobileapp.tripster;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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
    private ConnectionAdapter connectionAdapter;
    private LiveData<List<Connection>> connectionObservable;

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

        connectionsViewModel = ViewModelProviders.of(this).get(ConnectionViewModel.class);

        // this.locationManager = new LocationManager(this);
        // this.locationManager.setLocation();


    }

    @OnClick(R.id.current_location)
    public void onCurrentLocationClick() {

    }

    @OnClick(R.id.switch_selection)
    public void onSwitchSelectionClick() {
        String departure = departureTextField.getText().toString();
        String destination = destinationTextField.getText().toString();
        departureTextField.setText(destination);
        destinationTextField.setText(departure);

    }

    @OnClick(R.id.submit_search)
    public void onSearchClick() {
        progressBar.setVisibility(View.VISIBLE);
        String from = departureTextField.getText().toString();
        String to = destinationTextField.getText().toString();
        connectionsViewModel.searchLimitedConnections(from, to, NUMBER_OF_CONNECTIONS);

        connectionsViewModel.connections.observe(this, connections -> {
            setupConnectionAdapter(connections);
            progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // this.locationManager.flushLocations();
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
            connectionAdapter = new ConnectionAdapter(MainActivity.this, connections);
            connectionListView.setAdapter(connectionAdapter);
    }

}
