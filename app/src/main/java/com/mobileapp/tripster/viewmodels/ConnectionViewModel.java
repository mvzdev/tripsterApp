package com.mobileapp.tripster.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mobileapp.tripster.model.Connection;
import com.mobileapp.tripster.repositories.ConnectionRepository;

import java.util.Collections;
import java.util.List;

public class ConnectionViewModel extends AndroidViewModel {

    private ConnectionRepository connectionRepository;
    public LiveData<List<Connection>> connections;

    public ConnectionViewModel(@NonNull Application application) {
        super(application);
        connectionRepository = new ConnectionRepository();
        connections = getConnections();

    }

    public void searchLimitedConnections(String from, String to, String time, String via, int nOfConnections) {
        this.connections = this.connectionRepository
                .searchLimitedConnection(from, to, time, via, nOfConnections);
    }

    public LiveData<List<Connection>> getConnections() {
        if (this.connections == null) {

            MutableLiveData<List<Connection>> data = new MutableLiveData<>();
            data.setValue(Collections.emptyList());
            this.connections = data;
        }
        return this.connections;
    }

}
