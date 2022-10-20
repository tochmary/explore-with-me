package ru.practicum.mainservice.event.mapper;

import ru.practicum.mainservice.event.model.Location;

public class LocationMapper {
    public static Location toLocation(Float lat, Float lon) {
        return new Location(lat,lon);
    }
}
