package uk.adamcz.patryk.rezerwacja;

import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationManager {
    public Map< String, Map<String, List<Long>>> Locations;
    
    public LocationManager(){
        loadLocations();
    }

    private void loadLocations(){
        Locations = new HashMap<String, Map<String, List<Long>>>();
        List<String> locations = StorageManager.ListLocations();

        if (locations != null && !locations.isEmpty()) {
            Collections.sort(locations);
            for (String key : locations) {
                Locations.put(key, null);
            }
        }
    }

    public void AddLocation(String location){
        if (!location.isBlank())
            StorageManager.TryAddLocation(location);

        loadLocations();
    }

    public void RemoveLocation(String location){
        if (!location.isBlank())
            StorageManager.TryRemoveLocation(location);

        loadLocations();
    }

    public void ReserveLocation(String location, String startTimeString, String endTimeString){
        if (!location.isBlank()) {
            LocalTime startTime = LocalTime.parse(startTimeString);
            LocalTime endTime = LocalTime.parse(endTimeString);

            if (Locations.containsKey(location)) {
                Map<String, List<Long>> locationData = Locations.get(location);

            }
        }

    }
}
