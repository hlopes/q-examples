package org.hlopes;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/")
public class TheBestPlaceToBeResource {
    static final double VALENCE_LATITUDE = 44.9;
    static final double VALENCE_LONGITUDE = 4.9;

    static final double ATHENS_LATITUDE = 37.9;
    static final double ATHENS_LONGITUDE = 23.7;

    @RestClient
    WeatherService weatherService;

    @GET
    @RunOnVirtualThread
    public String getTheBestPlaceToBe() {
        var valence =
            weatherService.getWeather(VALENCE_LATITUDE, VALENCE_LONGITUDE).weather().temperature();
        var athens =
            weatherService.getWeather(ATHENS_LATITUDE, ATHENS_LONGITUDE).weather().temperature();

        return "Valence: " + valence + " - Athens: " + athens + " - thread: " + Thread.currentThread();
    }

}
