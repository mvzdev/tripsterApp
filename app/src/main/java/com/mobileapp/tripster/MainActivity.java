package com.mobileapp.tripster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mobileapp.tripster.dtos.ConnectionContainerDto;
import com.mobileapp.tripster.services.ConnectionService;

import butterknife.BindView;
import butterknife.ButterKnife;

import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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


        // TODO: store url in a config file
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://transport.opendata.ch/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ConnectionService service = retrofit.create(ConnectionService.class);

        service.searchConnections(from, to).enqueue(new Callback<ConnectionContainerDto>() {

            @Override
            public void onResponse(Call<ConnectionContainerDto> call, Response<ConnectionContainerDto> response) {
                if (response.isSuccessful()) {
                    ConnectionContainerDto connectionsContainer = response.body();
                    // Handle result...

                    System.out.println(connectionsContainer);

                    // originTime.setText(connectionsContainer.connections.get(0).from.departure.toString());
                    // destinationTime.setText(connectionsContainer.connections.get(0).to.arrival.toString());
                }
            }

            @Override
            public void onFailure(Call<ConnectionContainerDto> call, Throwable t) {
                Log.d("Test", "fail");
            }
        });

    }

}
