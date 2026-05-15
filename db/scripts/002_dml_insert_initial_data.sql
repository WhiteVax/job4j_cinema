INSERT INTO files (name, path)
VALUES ('interstellar.jpg', '/files/interstellar.jpg'),
       ('inception.jpg', '/files/inception.jpg'),
       ('matrix.jpg', '/files/matrix.jpg')
ON CONFLICT (path) DO NOTHING;

INSERT INTO genres (name)
VALUES ('Фантастика'),
       ('Триллер'),
       ('Боевик')
ON CONFLICT (name) DO NOTHING;

INSERT INTO films (name, description, "year", genre_id, minimal_age,
                   duration_in_minutes, file_id)
VALUES ('Интерстеллар',
        'История исследователей, которые отправляются за пределы галактики.',
        2014,
        (SELECT id FROM genres WHERE name = 'Фантастика'),
        12,
        169,
        (SELECT id FROM files WHERE path = '/files/interstellar.jpg')),
       ('Начало',
        'Профессиональный вор внедряет идеи через сны.',
        2010,
        (SELECT id FROM genres WHERE name = 'Триллер'),
        12,
        148,
        (SELECT id FROM files WHERE path = '/files/inception.jpg')),
       ('Матрица',
        'Хакер узнает правду о мире, в котором живет.',
        1999,
        (SELECT id FROM genres WHERE name = 'Боевик'),
        16,
        136,
        (SELECT id FROM files WHERE path = '/files/matrix.jpg'))
ON CONFLICT DO NOTHING;

INSERT INTO halls (name, row_count, place_count, description)
VALUES ('Зал 1', 5, 8, 'Большой зал'),
       ('Зал 2', 4, 6, 'Малый зал')
ON CONFLICT DO NOTHING;

INSERT INTO film_sessions (film_id, halls_id, start_time, end_time, price)
VALUES ((SELECT id FROM films WHERE name = 'Интерстеллар'),
        (SELECT id FROM halls WHERE name = 'Зал 1'),
        '2026-05-20 18:00:00',
        '2026-05-20 20:49:00',
        250),
       ((SELECT id FROM films WHERE name = 'Начало'),
        (SELECT id FROM halls WHERE name = 'Зал 1'),
        '2026-05-21 19:30:00',
        '2026-05-21 21:58:00',
        230),
       ((SELECT id FROM films WHERE name = 'Матрица'),
        (SELECT id FROM halls WHERE name = 'Зал 2'),
        '2026-05-22 20:00:00',
        '2026-05-22 22:16:00',
        220);
