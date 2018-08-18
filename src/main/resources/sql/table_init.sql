PRAGMA foreign_keys = TRUE;
PRAGMA writable_schema = 1;
DELETE FROM sqlite_master WHERE type IN ('table', 'index', 'trigger');
PRAGMA writable_schema = 0;
VACUUM;

-- Basic schema design.
CREATE TABLE IF NOT EXISTS person_types (
  id INTEGER PRIMARY KEY,
  name TEXT NOT NULL UNIQUE
);

INSERT INTO person_types (id, name)
VALUES (0, 'student'),
       (1, 'teaching-assistant'),
       (2, 'professor');

CREATE TABLE IF NOT EXISTS persons (
  id INTEGER PRIMARY KEY,
  name TEXT NOT NULL,
  email_address TEXT NOT NULL,
  github_username TEXT NOT NULL UNIQUE,
  person_type_id INTEGER NOT NULL,
  FOREIGN KEY (person_type_id)
    REFERENCES person_types(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS team_types (
  id INTEGER PRIMARY KEY,
  name TEXT NOT NULL UNIQUE
);

INSERT INTO team_types (id, name)
VALUES (0, 'student_team'),
       (1, 'teaching_assistant_team'),
       (2, 'all_teaching_assistants'),
       (3, 'none');

CREATE TABLE IF NOT EXISTS teams (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  team_type_id INTEGER NOT NULL,
  FOREIGN KEY (team_type_id)
    REFERENCES team_types(id)
    ON DELETE CASCADE
);

INSERT INTO teams (id, team_type_id)
VALUES (0, 3), -- None team for all students or TA's without a team.
       (1, 2); -- Team for all teaching assistants.

CREATE TABLE IF NOT EXISTS teaching_assistant_teams (
  team_id INTEGER PRIMARY KEY,
  name TEXT NOT NULL UNIQUE,
  FOREIGN KEY (team_id)
    REFERENCES teams(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS student_teams (
  team_id INTEGER PRIMARY KEY,
  repository_name TEXT,
  group_id INTEGER NOT NULL UNIQUE,
  teaching_assistant_team_id INTEGER,
  FOREIGN KEY (team_id)
    REFERENCES teams(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (teaching_assistant_team_id)
    REFERENCES  teaching_assistant_teams(team_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS students (
  person_id INTEGER PRIMARY KEY,
  team_id INTEGER NOT NULL,
  chose_partner INTEGER NOT NULL,
  FOREIGN KEY (person_id)
    REFERENCES persons(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (team_id)
    REFERENCES teams(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS teaching_assistants (
  person_id INTEGER PRIMARY KEY,
  team_id INTEGER NOT NULL,
  FOREIGN KEY (person_id)
    REFERENCES persons(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (team_id)
    REFERENCES teams(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

-- Error queue storage.
CREATE TABLE IF NOT EXISTS error_types (
  id INTEGER PRIMARY KEY,
  name TEXT NOT NULL UNIQUE
);

INSERT INTO error_types (id, name)
VALUES (0, 'team_error'),
       (1, 'person_error'),
       (2, 'system_error');

CREATE TABLE IF NOT EXISTS errors (
  id INTEGER PRIMARY KEY,
  timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  error_type_id INTEGER NOT NULL,
  message TEXT NOT NULL,
  FOREIGN KEY (error_type_id)
    REFERENCES error_types(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS team_errors (
  error_id INTEGER PRIMARY KEY,
  team_id INTEGER NOT NULL,
  FOREIGN KEY (error_id)
    REFERENCES errors(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (team_id)
    REFERENCES teams(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS person_errors (
  error_id INTEGER PRIMARY KEY,
  person_id INTEGER NOT NULL,
  FOREIGN KEY (error_id)
    REFERENCES errors(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (person_id)
    REFERENCES persons(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);