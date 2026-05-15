package ru.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {
    private int id;
    private int sessionId;
    private String filmName;
    private String hallName;
    private int rowNumber;
    private int placeNumber;
    private int price;
}
