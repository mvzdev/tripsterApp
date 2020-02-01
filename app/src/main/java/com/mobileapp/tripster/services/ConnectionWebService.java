package com.mobileapp.tripster.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mobileapp.tripster.model.Connection;
import com.mobileapp.tripster.api.ApiClient;
import com.mobileapp.tripster.dtos.ConnectionContainerDto;
import com.mobileapp.tripster.dtos.ConnectionDto;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ConnectionWebService {

    private ConnectionService connectionService;
    private static final String CONNECTION_SERVICE_LOG_TAG = "ConnectionService";

    public ConnectionWebService() {
        Retrofit retrofit = ApiClient.getClient();
        this.connectionService = retrofit.create(ConnectionService.class);
    }

    public LiveData<List<Connection>> searchLimitedConnections(String from, String to, String time, int nOfConnections) {
        final MutableLiveData<List<Connection>> data = new MutableLiveData<>();

        Call<ConnectionContainerDto> call = connectionService.searchLimitedConnections(from, to, time, nOfConnections);
        call.enqueue(new Callback<ConnectionContainerDto>() {

            @Override
            public void onResponse(Call<ConnectionContainerDto> call, Response<ConnectionContainerDto> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<ConnectionDto> connectionDtos = response.body().connections;

                    List<Connection> connections = connectionDtos.stream()
                            .map(c -> new Connection(c.from.departure, c.from.station.name, c.to.arrival, c.to.station.name))
                            .collect(Collectors.toList());

                    data.setValue(connections);
                }
            }

            @Override
            public void onFailure(Call<ConnectionContainerDto> call, Throwable t) {
                if (t.getLocalizedMessage() != null) {

                    // TODO: Figure out best way to display an error.

                    /*
                    Context context = getApplicationContext();
                    CharSequence text = "Es scheint keine Verbindung zum Internet zu bestehen...";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    */

                    Log.e(CONNECTION_SERVICE_LOG_TAG, t.getLocalizedMessage());
                }
            }

        });

        return data;
    }
}
