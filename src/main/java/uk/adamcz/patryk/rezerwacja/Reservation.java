package uk.adamcz.patryk.rezerwacja;

import java.time.LocalTime;
import java.util.UUID;

public class Reservation {

    public UUID Id = UUID.randomUUID();

    public String OwnerUsername;

    public LocalTime StartTime;

    public LocalTime EndTime;

    Reservation(String ownerUsername, LocalTime startTime, LocalTime endTime) {
        OwnerUsername = ownerUsername;
        StartTime = startTime;
        EndTime = endTime;
    }
}
