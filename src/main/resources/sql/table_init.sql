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

CREATE TABLE IF NOT EXISTS persons (
  id INTEGER PRIMARY KEY,
  name TEXT NOT NULL,
  email_address TEXT NOT NULL,
  github_username TEXT NOT NULL UNIQUE,
  person_type_id INTEGER NOT NULL,
  team_id INTEGER NULL,
  FOREIGN KEY (person_type_id)
    REFERENCES person_types(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (team_id)
    REFERENCES teams(id)
      ON DELETE CASCADE
      ON UPDATE CASCADE
);

-- Team tables for all types of teams.
CREATE TABLE IF NOT EXISTS team_types (
  id INTEGER PRIMARY KEY,
  name TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS teams (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  team_type_id INTEGER NOT NULL,
  FOREIGN KEY (team_type_id)
    REFERENCES team_types(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS team_members (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  team_id INTEGER NOT NULL,
  person_id INTEGER NOT NULL,
  FOREIGN KEY (team_id)
    REFERENCES teams(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (person_id)
    REFERENCES persons(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  UNIQUE (team_id, person_id)
);

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

-- Student-specific tables.
CREATE TABLE IF NOT EXISTS students (
  person_id INTEGER PRIMARY KEY,
  chose_partner INTEGER NOT NULL,
  FOREIGN KEY (person_id)
    REFERENCES persons(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS student_preferred_partners (
  student_id INTEGER PRIMARY KEY,
  partner_id INTEGER NOT NULL,
  FOREIGN KEY (student_id)
    REFERENCES students(person_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  UNIQUE (student_id, partner_id)
);

-- TeachingAssistant-specific tables.
CREATE TABLE IF NOT EXISTS teaching_assistants (
  person_id INTEGER PRIMARY KEY,
  FOREIGN KEY (person_id)
    REFERENCES persons(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

-- Error queue storage.
CREATE TABLE IF NOT EXISTS error_types (
  id INTEGER PRIMARY KEY,
  name TEXT NOT NULL UNIQUE
);

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