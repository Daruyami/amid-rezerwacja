package uk.adamcz.patryk.rezerwacja;

import java.util.List;
import java.util.Map;

public class LocationManager {
    public Map< String, Map<String, List<Long>>> Locations;
    
    public LocationManager(){
        List<String> locations = StorageManager.ListLocations();

        if (locations != null && !locations.isEmpty())
            for (String key : locations) {
                Locations.put(key, null);
            }
    }
}
