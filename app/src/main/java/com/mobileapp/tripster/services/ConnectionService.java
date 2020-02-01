package com.mobileapp.tripster.services;

import com.mobileapp.tripster.dtos.ConnectionContainerDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ConnectionService {

    @GET("connections")
    Call<ConnectionContainerDto> searchConnections(@Query("from") String origin, @Query("to") String destination);

    @GET("connections")
    Call<ConnectionContainerDto> searchLimitedConnections(@Query("from") String origin, @Query("to") String destination, @Query("time") String time, @Query("limit") int nOfConnections);


}
