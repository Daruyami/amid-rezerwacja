package uk.adamcz.patryk.rezerwacja;

import java.time.LocalTime;
import java.util.*;

public class LocationManager {
    public Map< String, List<Reservation>> Locations;
    
    public LocationManager(){
        loadLocations();
    }

    private void loadLocations(){
        Locations = new HashMap<String, List<Reservation>>();
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
        try {
            if (!location.isBlank()) {
                LocalTime startTime = parseTimeString(startTimeString);
                LocalTime endTime = parseTimeString(endTimeString);

                if (Locations.containsKey(location)) {
                    List<Reservation> locationData = Locations.get(location);

                    if (locationData == null)
                        locationData = new ArrayList<>();

                    for (Reservation reservation : locationData) {
                        if (startTime.equals(reservation.StartTime) || endTime.equals(reservation.EndTime)
                                || (reservation.EndTime.isAfter(startTime) && reservation.EndTime.isBefore(endTime))
                                || reservation.StartTime.isAfter(startTime) && reservation.StartTime.isBefore(endTime)) {
                            throw new Exception("Selected period is already reserved by someone else!");
                        }
                    }
                    locationData.add(new Reservation(IdentityHandler.Name, startTime, endTime));
                    locationData.sort(Comparator.comparingInt(x -> x.StartTime.toSecondOfDay()));
                    Locations.put(location, locationData);
                }
            }
        } catch (Exception ignored) { }
    }

    public void ClearReservationsLocation(String location, String startTimeString, String endTimeString){
        try {
            if (!location.isBlank()) {
                LocalTime startTime = parseTimeString(startTimeString);
                LocalTime endTime = parseTimeString(endTimeString);

                if (Locations.containsKey(location)) {
                    List<Reservation> locationData = Locations.get(location);

                    if (locationData == null)
                        locationData = new ArrayList<>();

                    for (Reservation reservation : locationData) {
                        if ((startTime.equals(reservation.StartTime) || endTime.equals(reservation.EndTime)
                                || endTime.isAfter(reservation.StartTime) && endTime.isBefore(reservation.EndTime))
                                || startTime.isAfter(reservation.StartTime) && startTime.isBefore(reservation.EndTime)) {
                            locationData.remove(reservation);
                        }
                    }

                    Locations.put(location, locationData);
                }
            }
        } catch (Exception ignored) { }
    }

    private static LocalTime parseTimeString(String time){
        if (time.length()<5)
            return LocalTime.parse("0"+time);
        return LocalTime.parse(time);
    }
}
