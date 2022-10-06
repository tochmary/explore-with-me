package ru.practicum.mainservice.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Широта и долгота места проведения события:
 * lat - Широта;
 * lon - Долгота;
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private Float lat;
    private Float lon;
}
