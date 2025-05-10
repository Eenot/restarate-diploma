MERGE INTO pricing(id, name)
VALUES (1, '$'),
       (2, '$$'),
       (3, '$$$'),
       (4, '$$$$'),
       (5, '$$$$$');

MERGE INTO category(id, name)
VALUES (1, 'Десерт'),
       (2, 'Напиток'),
       (3, 'Паста'),
       (4, 'Роллы'),
       (5, 'Супы'),
       (6, 'Гарниры');

INSERT INTO events_type(events_type_id, events_name)
    VALUES (1, 'LIKE'),
           (2, 'REVIEW'),
           (3, 'FRIEND');

INSERT INTO events_operation(events_operation_id, operation_name)
    VALUES (1, 'ADD'),
           (2, 'UPDATE'),
           (3, 'REMOVE');