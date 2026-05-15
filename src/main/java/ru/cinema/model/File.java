package ru.cinema.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File {
    private int id;
    private String name;
    private String path;
}
