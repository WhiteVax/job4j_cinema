package ru.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionDto {
    private int id;
    private String filmName;
    private String filmDescription;
    private String hallName;
    private int rowCount;
    private int placeCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int price;
}
