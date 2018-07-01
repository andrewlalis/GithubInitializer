package nl.andrewlalis.util;

import nl.andrewlalis.model.Student;
import nl.andrewlalis.model.Team;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TeamGenerator {

    public static List<Team> generateFromCSV(String filename, int teamSize) throws IOException {
        System.out.println("Generating teams of size " + teamSize);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new FileReader(filename));

        Map<Integer, Student> studentMap = readAllStudents(records, teamSize);
        return generateAllValidTeams(studentMap, teamSize);
    }

    private static List<Team> generateAllValidTeams(Map<Integer, Student> studentMap, int teamSize) {
        List<Student> singleStudents = new ArrayList<>(studentMap.values());
        List<Team> teams = new ArrayList<>();

        int teamCount = 1;
        // For each student, try to make a team from its preferred partners.
        for (Map.Entry<Integer, Student> e : studentMap.entrySet()) {
            Team t = e.getValue().getPreferredTeam(studentMap);
            // Check if the team is of a valid size, and is not a duplicate.
            // Note that at this stage, singles are treated as teams of 1, and thus not valid for any teamSize > 1.
            if (t.isValid(teamSize) && !teams.contains(t)) {
                t.setId(teamCount++);
                // Once we know this team is completely valid, we remove all the students in it from the list of singles.
                singleStudents.removeAll(t.getStudents());
                teams.add(t);
            }
        }

        teams.addAll(mergeSingleStudents(singleStudents, teamSize, teamCount));
        return teams;
    }

    private static List<Team> mergeSingleStudents(List<Student> singleStudents, int teamSize, int teamIndex) {
        List<Team> teams = new ArrayList<>();
        while (!singleStudents.isEmpty()) {
            Team t = new Team();
            t.setId(teamIndex++);
            while (t.getStudentCount() < teamSize && !singleStudents.isEmpty()) {
                t.addStudent(singleStudents.remove(0));
            }
            teams.add(t);
        }
        return teams;
    }

    /**
     * Reads all the rows from the CSV file, and returns a map of students, using student number as the index.
     * @param records The records in the CSV file.
     * @param teamSize The preferred size of teams, or rather, the expected number of partners.
     * @return A map of all students in the file.
     */
    private static Map<Integer, Student> readAllStudents(Iterable<CSVRecord> records, int teamSize) {
        Map<Integer, Student> studentMap = new HashMap<>();
        for (CSVRecord record : records) {
            List<Integer> preferredIds = new ArrayList<>();
            if (record.get(5).equals("Yes")) {
                int columnOffset = 6;
                for (int i = 0; i < teamSize-1; i++) {
                    preferredIds.add(Integer.parseInt(record.get(columnOffset + i)));
                }
            }
            Student s = new Student(Integer.parseInt(record.get(3)), record.get(2), record.get(1), record.get(4), preferredIds);
            studentMap.put(s.getNumber(), s);
        }
        return studentMap;
    }

}
