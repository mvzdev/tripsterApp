package com.mobileapp.tripster.repositories;

import androidx.lifecycle.LiveData;

import com.mobileapp.tripster.model.Connection;
import com.mobileapp.tripster.services.ConnectionWebService;

import java.util.List;

public class ConnectionRepository {
    private ConnectionWebService webService = new ConnectionWebService();

    public LiveData<List<Connection>> searchLimitedConnection(String from, String to, String time, int nOfConnections) {
        return webService.searchLimitedConnections(from, to, time, nOfConnections);
    }
}
