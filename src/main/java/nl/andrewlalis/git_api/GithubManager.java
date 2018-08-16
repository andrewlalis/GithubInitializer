package nl.andrewlalis.git_api;

import nl.andrewlalis.model.Student;
import nl.andrewlalis.model.StudentTeam;
import org.kohsuke.github.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class is responsible for initializing the Github repositories and setting permissions, adding teams, etc.
 */
public class GithubManager {

    /**
     * The name of the organization to operate on.
     */
    private String organizationName;

    /**
     * The object which simplifies creating REST URL's for most of the requests.
     */
    private URLBuilder urlBuilder;

    /**
     * The assignments repository where students will get assignments from.
     */
    private GHRepository assignmentsRepo;
    private String assignmentsRepoName;

    /**
     * The name of the team which contains all teaching assistants.
     */
    private String teachingAssistantsTeamName;

    /**
     * The prefix used to prepend the names of student repositories.
     * Should ideally contain the current school year.
     */
    private String studentRepoPrefix;

    /**
     * Github object for API interactions.
     */
    private GitHub github;
    private GHOrganization organization;

    /**
     * The logger for outputting debug info.
     */
    private static final Logger logger = Logger.getLogger(GithubManager.class.getName());
    static {
        logger.setParent(Logger.getGlobal());
    }

    public GithubManager(String organizationName, String accessToken, String assignmentsRepo, String teachingAssistantsTeamName, String studentRepoPrefix) {
        this.organizationName = organizationName;
        this.assignmentsRepoName = assignmentsRepo;
        this.teachingAssistantsTeamName = teachingAssistantsTeamName;
        this.studentRepoPrefix = studentRepoPrefix;
        this.urlBuilder = new URLBuilder(organizationName, accessToken);

        try {
            this.github = GitHub.connectUsingOAuth(accessToken);
            this.organization = this.github.getOrganization(this.organizationName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the github repository for all studentTeams given.
     *
     * Creates for the entire organization:
     * - an assignments repository with protected master branch and TA permissions.
     * Creates for each team:
     * - a repository
     * - protected master branch
     * - development branch
     * - adds students to repository
     * - adds all students to assignments repository.
     * @param studentTeams The list of student studentTeams.
     * @throws Exception If an error occurs while initializing the github repositories.
     */
    public void initializeGithubRepos(List<StudentTeam> studentTeams) throws Exception {
        GHTeam teamAll = this.organization.getTeamByName(this.teachingAssistantsTeamName);

        this.setupAssignmentsRepo(teamAll);

        StudentTeam t = new StudentTeam();
        Student s = new Student(3050831, "Andrew Lalis", "andrewlalisofficial@gmail.com", "andrewlalis", null);
        t.addStudent(s);
        t.setId(42);

        this.setupStudentTeam(t, teamAll);
    }

    /**
     * Sets up the organization's assignments repository, and grants permissions to all teaching assistants.
     * @param allTeachingAssistants A team consisting of all teaching assistants.
     * @throws IOException If an HTTP request failed.
     */
    private void setupAssignmentsRepo(GHTeam allTeachingAssistants) throws IOException {
        // Create the repository.
        GHCreateRepositoryBuilder builder = this.organization.createRepository(this.assignmentsRepoName);
        builder.description("Assignments repository for Advanced Object Oriented Programming");
        builder.wiki(false);
        builder.issues(true);
        builder.private_(false); // TODO: Make this true for production.
        builder.team(allTeachingAssistants);
        builder.gitignoreTemplate("Java");
        this.assignmentsRepo = builder.create();

        // Protect the master branch.
        GHBranchProtectionBuilder protectionBuilder = this.assignmentsRepo.getBranch("master").enableProtection();
        protectionBuilder.includeAdmins(false);
        protectionBuilder.restrictPushAccess();
        protectionBuilder.teamPushAccess(allTeachingAssistants);
        protectionBuilder.addRequiredChecks("ci/circleci");
        protectionBuilder.enable();

        // Grant all teaching assistants write access.
        allTeachingAssistants.add(this.assignmentsRepo, GHOrganization.Permission.ADMIN);
    }

    /**
     * Creates and sets up a student team's repository, and invites those students to the organization's assignments
     * repository as well.
     * @param team The student team to set up.
     * @param taTeam The team of teaching assistants that is responsible for these students.
     * @throws IOException If an HTTP request fails.
     */
    private void setupStudentTeam(StudentTeam team, GHTeam taTeam) throws IOException {
        String teamRepoName = team.generateUniqueName(this.studentRepoPrefix);

        List<Student> students = team.getStudents();
        StringBuilder description = new StringBuilder("Group ");
        description.append(team.getId()).append(": ");

        for (Student s : students) {
            description.append(s.getName()).append(' ');
        }

        GHCreateRepositoryBuilder builder = this.organization.createRepository(teamRepoName);
        builder.team(taTeam);
        builder.wiki(false);
        builder.issues(true);
        builder.description(description.toString());
        builder.gitignoreTemplate("Java");
        builder.private_(false); // TODO: Change this to true for production
        GHRepository repo = builder.create();
        logger.info("Created repository: " + repo.getName());

        // Protect the master branch.
        GHBranchProtectionBuilder protectionBuilder = repo.getBranch("master").enableProtection();
        protectionBuilder.includeAdmins(false);
        protectionBuilder.teamPushAccess(taTeam);
        protectionBuilder.addRequiredChecks("ci/circleci");
        protectionBuilder.enable();
        logger.fine("Protected master branch of repository: " + repo.getName());

        // Create development branch.
        String sha1 = repo.getBranch(repo.getDefaultBranch()).getSHA1();
        repo.createRef("refs/heads/development", sha1);
        logger.fine("Created development branch of repository: " + repo.getName());

        taTeam.add(repo, GHOrganization.Permission.ADMIN);
        logger.fine("Added team " + taTeam.getName() + " as admin to repository: " + repo.getName());

        List<GHUser> users = new ArrayList<>();
        for (Student student : team.getStudents()) {
            GHUser user = this.github.getUser(student.getGithubUsername());
            users.add(user);
        }

        repo.addCollaborators(users);
        this.assignmentsRepo.addCollaborators(users);
    }

    /**
     * Deletes all repositories in the organization.
     * @throws IOException if an error occurs with sending requests.
     */
    public void deleteAllRepositories() throws IOException {
        Map<String, GHRepository> repoMap = this.organization.getRepositories();
        for (Map.Entry<String, GHRepository> repoEntry : repoMap.entrySet()) {
            repoEntry.getValue().delete();
        }
    }

    /**
     * Archives all repositories whose name contains the given substring.
     * @param sub Any repository containing this substring will be archived.
     */
    public void archiveAllRepositories(String sub) {

    }

}
