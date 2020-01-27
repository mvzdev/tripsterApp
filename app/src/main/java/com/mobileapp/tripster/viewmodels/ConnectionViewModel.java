package com.mobileapp.tripster.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mobileapp.tripster.model.Connection;
import com.mobileapp.tripster.ConnectionRepository;

import java.util.Collections;
import java.util.List;

public class ConnectionViewModel extends ViewModel {

    private ConnectionRepository connectionRepository = new ConnectionRepository();

    private LiveData<List<Connection>> connections;

    public void searchLimitedConnections(String from, String to, int nOfConnections) {
        this.connections = this.connectionRepository
                .searchLimitedConnection(from, to, nOfConnections);
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
