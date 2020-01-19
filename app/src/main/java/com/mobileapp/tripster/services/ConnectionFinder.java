package com.mobileapp.tripster.services;

import android.util.Log;

import com.mobileapp.tripster.dtos.ConnectionContainerDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionFinder {

    private String apiUrl;

    public ConnectionFinder(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public void findConnections(String from, String to) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ConnectionServiceInterface service = retrofit.create(ConnectionServiceInterface.class);

        service.searchConnections(from, to).enqueue(new Callback<ConnectionContainerDto>() {

            @Override
            public void onResponse(Call<ConnectionContainerDto> call, Response<ConnectionContainerDto> response) {
                if (response.isSuccessful()) {
                    ConnectionContainerDto connectionsContainer = response.body();


                    // TODO: new intent to results activity + pass 3 connections (as required)

                    System.out.println(connectionsContainer.connections.get(0).from.departure.toString());

                    // originTime.setText(connectionsContainer.connections.get(0).from.departure.toString());
                    // destinationTime.setText(connectionsContainer.connections.get(0).to.arrival.toString());
                }
            }

            @Override
            public void onFailure(Call<ConnectionContainerDto> call, Throwable t) {

                if (t.getLocalizedMessage() != null) {
                    Log.d("Test", t.getLocalizedMessage());
                }
            }
        });


    }



}
