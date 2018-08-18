package nl.andrewlalis.git_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.andrewlalis.model.Student;
import nl.andrewlalis.model.StudentTeam;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.kohsuke.github.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class is responsible for initializing the Github repositories and setting permissions, adding teams, etc.
 */
public class GithubManager {

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
    private String accessToken;

    /**
     * The logger for outputting debug info.
     */
    private static final Logger logger = Logger.getLogger(GithubManager.class.getName());
    static {
        logger.setParent(Logger.getGlobal());
    }

    public GithubManager(String organizationName, String accessToken, String assignmentsRepo, String teachingAssistantsTeamName, String studentRepoPrefix) {
        this.assignmentsRepoName = assignmentsRepo;
        this.teachingAssistantsTeamName = teachingAssistantsTeamName;
        this.studentRepoPrefix = studentRepoPrefix;
        this.accessToken = accessToken;

        try {
            this.github = GitHub.connectUsingOAuth(accessToken);
            this.organization = this.github.getOrganization(organizationName);
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
        // Check if the repository already exists.
        GHRepository existingRepo = this.organization.getRepository(this.assignmentsRepoName);
        if (existingRepo != null) {
            existingRepo.delete();
            logger.fine("Deleted pre-existing assignments repository.");
        }

        // Create the repository.
        GHCreateRepositoryBuilder builder = this.organization.createRepository(this.assignmentsRepoName);
        builder.description("Assignments repository for Advanced Object Oriented Programming");
        builder.wiki(false);
        builder.issues(true);
        builder.private_(false); // TODO: Make this true for production.
        builder.team(allTeachingAssistants);
        builder.gitignoreTemplate("Java");
        this.assignmentsRepo = builder.create();
        logger.info("Created assignments repository.");

        // Protect the master branch.
        GHBranchProtectionBuilder protectionBuilder = this.assignmentsRepo.getBranch("master").enableProtection();
        protectionBuilder.includeAdmins(false);
        protectionBuilder.restrictPushAccess();
        protectionBuilder.teamPushAccess(allTeachingAssistants);
        protectionBuilder.addRequiredChecks("ci/circleci");
        protectionBuilder.enable();
        logger.fine("Protected master branch of assignments repository.");

        // Grant all teaching assistants write access.
        allTeachingAssistants.add(this.assignmentsRepo, GHOrganization.Permission.ADMIN);
        logger.fine("Gave admin rights to all teaching assistants in team: " + allTeachingAssistants.getName());
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
        List<GHRepository> repositories = this.organization.listRepositories().asList();
        for (GHRepository repo : repositories) {
            repo.delete();
        }
    }

    /**
     * Archives all repositories whose name contains the given substring.
     * @param sub Any repository containing this substring will be archived.
     */
    public void archiveAllRepositories(String sub) throws IOException {
        List<GHRepository> repositories = this.organization.listRepositories().asList();
        for (GHRepository repo : repositories) {
            if (repo.getName().contains(sub)) {
                archiveRepository(repo);
            }
        }
    }

    /**
     * Archives a repository so that it can no longer be manipulated.
     * TODO: Change to using Github API instead of Apache HttpUtils.
     * @param repo The repository to archive.
     * @throws IOException If an error occurs with the HTTP request.
     */
    public void archiveRepository(GHRepository repo) throws IOException {
        HttpPatch patch = new HttpPatch("https://api.github.com/repos/" + repo.getFullName() + "?access_token=" + this.accessToken);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("archived", true);
        String json = mapper.writeValueAsString(root);
        patch.setEntity(new StringEntity(json));
        HttpResponse response = client.execute(patch);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new IOException("Could not archive repository: " + repo.getName() + ". Code: " + response.getStatusLine().getStatusCode());
        }
        logger.info("Archived repository: " + repo.getFullName());
    }

}
