package ru.cinema.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilmSession {
    private int id;
    private int filmId;
    private int hallsId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int price;
}
