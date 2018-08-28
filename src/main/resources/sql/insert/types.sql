INSERT INTO person_types (id, name)
VALUES (0, 'student'),
       (1, 'teaching-assistant'),
       (2, 'professor');

INSERT INTO team_types (id, name)
VALUES (0, 'student_team'),
       (1, 'teaching_assistant_team'),
       (2, 'all_teaching_assistants'),
       (3, 'none');

INSERT INTO teams (id, team_type_id)
VALUES (1000000, 3), -- None team for all students or TA's without a team.
       (1000001, 2); -- Team for all teaching assistants.

INSERT INTO error_types (id, name)
VALUES (0, 'team_error'),
       (1, 'person_error'),
       (2, 'system_error');