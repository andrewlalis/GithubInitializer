package nl.andrewlalis.util;

import nl.andrewlalis.model.Student;
import nl.andrewlalis.model.StudentTeam;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class TeamGenerator {

    private static final Logger logger = Logger.getLogger(TeamGenerator.class.getName());
    static {
        logger.setParent(Logger.getGlobal());
    }

    /**
     * Creates a list of teams by reading a CSV file of a certain format. The format for each column is as follows:
     * 1. Timestamp - The date and time the record was entered.
     * 2. Username - The email address.
     * 3. Name - The student's name.
     * 4. Student Number
     * 5. Github Username
     * 6. I have chosen a partner. (Yes / No) If yes:
     *      7. Your Partner's Student Number
     * @param filename The CSV file to load from.
     * @param teamSize The preferred teamsize used in creating teams.
     * @return A list of teams.
     * @throws IOException If the file is unable to be read.
     * @throws IllegalArgumentException If an invalid teamsize is given.
     */
    public static List<StudentTeam> generateFromCSV(String filename, int teamSize) throws IOException, IllegalArgumentException {
        logger.fine("Generating teams of size " + teamSize);
        if (teamSize < 1) {
            logger.severe("Invalid team size.");
            throw new IllegalArgumentException("StudentTeam size must be greater than or equal to 1. Got " + teamSize);
        }
        logger.fine("Parsing CSV file.");
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new FileReader(filename));

        logger.fine("Reading all records into map.");
        Map<Integer, Student> studentMap;
        try {
            studentMap = readAllStudents(records, teamSize);
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.severe("StudentTeam size does not match column count in records.");
            throw new IllegalArgumentException("StudentTeam size does not match column count in records.");
        }


        logger.fine("Generating all valid teams from student map.");
        return generateAllValidTeams(studentMap, teamSize);
    }

    /**
     * Generates all teams, given a mapping of all students to their student numbers. It will first try to generate
     * teams from students' preferences, and then take all students who are not in a team, and merge them into as many
     * teams as possible, and grouping all remainder single students into one final team.
     *
     * The algorithm works as follows:
     * For each student, try to create a team from their preferred partner numbers.
     *      Check if the team is valid by confirming that all their partners have the same preferred partners.
     *      If that's true, add this team, and remove all students in it from the list of singles.
     *      If it's not true, then the students will not be removed from the list of singles, and a warning is given.
     * After all students with preferred partners are placed in teams, the single students are merged, and their teams
     * are added afterwards.
     *
     * @param studentMap A mapping for each student to their student number.
     * @param teamSize The preferred maximum size for a team.
     * @return A list of teams, most of which are of teamSize size.
     */
    private static List<StudentTeam> generateAllValidTeams(Map<Integer, Student> studentMap, int teamSize) {
        List<Student> singleStudents = new ArrayList<>(studentMap.values());
        List<StudentTeam> studentTeams = new ArrayList<>();

        int teamCount = 1;
        // For each student, try to make a team from its preferred partners.
        for (Map.Entry<Integer, Student> e : studentMap.entrySet()) {
            StudentTeam newTeam = e.getValue().getPreferredTeam(studentMap);
            logger.finest("Checking if student's preferred team is valid:\n" + newTeam);
            // Check if the team is of a valid size, and is not a duplicate.
            // Note that at this stage, singles are treated as studentTeams of 1, and thus not valid for any teamSize > 1.
            if (newTeam.isValid(teamSize)) {
                // We know that the team is valid on its own, so now we check if it has members identical to any team already created.
                boolean matchFound = false;
                for (StudentTeam team : studentTeams) {
                    if (newTeam.hasSameMembers(team)) {
                        matchFound = true;
                        break;
                    }
                }
                if (!matchFound) {
                    // Once we know this team is completely valid, we remove all the students in it from the list of singles.
                    newTeam.setId(teamCount++);
                    singleStudents.removeAll(Arrays.asList(newTeam.getStudents()));
                    studentTeams.add(newTeam);
                    logger.fine("Created team:\n" + newTeam);
                }
            }
        }

        studentTeams.addAll(mergeSingleStudents(singleStudents, teamSize, teamCount));
        return studentTeams;
    }

    /**
     * Given a list of single students, this method generates as many teams as possible. that are as close to the team
     * size as possible.
     * @param singleStudents A list of students who have no preferred partners.
     * @param teamSize The preferred team size.
     * @param teamIndex The current number used in assigning an id to the team.
     * @return A list of teams comprising of single students.
     */
    private static List<StudentTeam> mergeSingleStudents(List<Student> singleStudents, int teamSize, int teamIndex) {
        List<StudentTeam> studentTeams = new ArrayList<>();
        while (!singleStudents.isEmpty()) {
            StudentTeam t = new StudentTeam();
            t.setId(teamIndex++);
            logger.fine("Creating new team of single students:\n" + t);
            while (t.memberCount() < teamSize && !singleStudents.isEmpty()) {
                Student s = singleStudents.remove(0);
                logger.finest("Single student: " + s);
                t.addMember(s);
            }
            studentTeams.add(t);
            logger.fine("Created team:\n" + t);
        }
        return studentTeams;
    }

    /**
     * Reads all the rows from the CSV file, and returns a map of students, using student number as the index.
     * @param records The records in the CSV file.
     * @param teamSize The preferred size of teams, or rather, the expected number of partners.
     * @return A map of all students in the file.
     * @throws ArrayIndexOutOfBoundsException if the teamSize does not work with the columns in the record.
     */
    private static Map<Integer, Student> readAllStudents(Iterable<CSVRecord> records, int teamSize) throws ArrayIndexOutOfBoundsException {
        Map<Integer, Student> studentMap = new HashMap<>();
        for (CSVRecord record : records) {
            logger.finest("Read record: " + record);
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
        logger.fine("Read " + studentMap.size() + " students from records.");
        return studentMap;
    }

}
