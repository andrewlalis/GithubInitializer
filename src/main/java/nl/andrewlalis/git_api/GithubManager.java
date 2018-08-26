package nl.andrewlalis.git_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jdk.nashorn.internal.ir.annotations.Ignore;
import nl.andrewlalis.model.Student;
import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.model.TATeam;
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

    public GithubManager(String organizationName, String accessToken) {
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
     * @param teamAll The team of all teaching assistants.
     * @param assignmentsRepoName The name of the assignments repo.
     * @throws Exception If an error occurs while initializing the github repositories.
     */
    public void initializeGithubRepos(List<StudentTeam> studentTeams, TATeam teamAll, String assignmentsRepoName) throws Exception {
        this.setupAssignmentsRepo(assignmentsRepoName, "fuck the police", teamAll);

        StudentTeam t = new StudentTeam();
        Student s = new Student(3050831, "Andrew Lalis", "andrewlalisofficial@gmail.com", "andrewlalis", null);
        t.addMember(s);
        t.setId(42);

        this.setupStudentTeam(t, teamAll, "advoop_2018");
        // TODO: Finish this method.
    }

    /**
     * Sets up the organization's assignments repository, and grants permissions to all teaching assistants.
     * @param assignmentsRepoName The name of the assignments repository.
     * @param description The description of the repository.
     * @param allTeachingAssistants A team consisting of all teaching assistants.
     * @throws IOException If an HTTP request failed.
     */
    private void setupAssignmentsRepo(String assignmentsRepoName, String description, TATeam allTeachingAssistants) throws IOException {
        // Check if the repository already exists.
        GHRepository existingRepo = this.organization.getRepository(assignmentsRepoName);
        if (existingRepo != null) {
            existingRepo.delete();
            logger.fine("Deleted pre-existing assignments repository.");
        }

        // Create the repository.
        GHCreateRepositoryBuilder builder = this.organization.createRepository(assignmentsRepoName);
        builder.description("Assignments repository for Advanced Object Oriented Programming");
        builder.wiki(false);
        builder.issues(true);
        builder.private_(false); // TODO: Make this true for production.
        builder.team(allTeachingAssistants.getGithubTeam());
        builder.gitignoreTemplate("Java");
        this.assignmentsRepo = builder.create();
        logger.info("Created assignments repository.");

        this.assignmentsRepo = this.createRepository(assignmentsRepoName, allTeachingAssistants, description, false, true, false);

        if (this.assignmentsRepo == null) {
            logger.severe("Could not create assignments repository.");
            return;
        }

        this.protectMasterBranch(this.assignmentsRepo, allTeachingAssistants);

        // Grant all teaching assistants write access.
        allTeachingAssistants.getGithubTeam().add(this.assignmentsRepo, GHOrganization.Permission.ADMIN);
        logger.fine("Gave admin rights to all teaching assistants in team: " + allTeachingAssistants.getName());
    }

    /**
     * Creates and sets up a student team's repository, and invites those students to the organization's assignments
     * repository as well.
     * @param team The student team to set up.
     * @param taTeam The team of teaching assistants that is responsible for these students.
     * @param prefix The prefix to append to the front of the repo name.
     * @throws IOException If an HTTP request fails.
     */
    private void setupStudentTeam(StudentTeam team, TATeam taTeam, String prefix) throws IOException {
        // First check that the assignments repo exists, otherwise no invitations can be sent.
        if (this.assignmentsRepo == null) {
            logger.warning("Assignments repository must be created before student repositories.");
            return;
        }

        GHRepository repo = this.createRepository(team.generateUniqueName(prefix), taTeam, team.generateRepoDescription(), false, true, false);

        if (repo == null) {
            logger.severe("Repository for student team " + team.getId() + " could not be created.");
            return;
        }

        this.protectMasterBranch(repo, taTeam);
        this.createDevelopmentBranch(repo);

        taTeam.getGithubTeam().add(repo, GHOrganization.Permission.ADMIN);
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

    /**
     * Protects the master branch of a given repository, and gives admin rights to the given team.
     * @param repo The repository to protect the master branch of.
     * @param team The team which gets admin rights to the master branch.
     */
    @SuppressWarnings("deprecation")
    private void protectMasterBranch(GHRepository repo, TATeam team) {
        try {
            GHBranchProtectionBuilder protectionBuilder = repo.getBranch("master").enableProtection();
            protectionBuilder.includeAdmins(false);
            protectionBuilder.restrictPushAccess();
            protectionBuilder.teamPushAccess(team.getGithubTeam());
            protectionBuilder.addRequiredChecks("ci/circleci");
            protectionBuilder.enable();
            logger.fine("Protected master branch of repository: " + repo.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a development branch for the given repository.
     * @param repo The repository to create a development branch for.
     */
    private void createDevelopmentBranch(GHRepository repo) {
        try {
            String sha1 = repo.getBranch(repo.getDefaultBranch()).getSHA1();
            repo.createRef("refs/heads/development", sha1);
            logger.fine("Created development branch of repository: " + repo.getName());
        } catch (IOException e) {
            logger.severe("Could not create development branch for repository: " + repo.getName() + '\n' + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creates a new github repository.
     * @param name The name of the repository.
     * @param taTeam The team to give admin rights.
     * @param description The description of the repository.
     * @param hasWiki Whether the repo has a wiki enabled.
     * @param hasIssues Whether the repo has issues enabled.
     * @param isPrivate Whether or not the repository is private.
     * @return The repository that was created, or
     */
    private GHRepository createRepository(String name, TATeam taTeam, String description, boolean hasWiki, boolean hasIssues, boolean isPrivate){
        try {
            GHCreateRepositoryBuilder builder = this.organization.createRepository(name);
            builder.team(taTeam.getGithubTeam());
            builder.wiki(hasWiki);
            builder.issues(hasIssues);
            builder.description(description);
            builder.gitignoreTemplate("Java");
            builder.private_(isPrivate); // TODO: Change this to true for production
            GHRepository repo = builder.create();
            logger.fine("Created repository: " + repo.getName());
            return repo;
        } catch (IOException e) {
            logger.severe("Could not create repository: " + name + '\n' + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
