package nl.andrewlalis.git_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.andrewlalis.model.Student;
import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.model.TATeam;
import nl.andrewlalis.model.TeachingAssistant;
import nl.andrewlalis.model.error.Error;
import nl.andrewlalis.model.error.Severity;
import nl.andrewlalis.ui.view.InitializerApp;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
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
            logger.severe("Unable to make a GithubManager with organization name: " + organizationName + " and access token: " + accessToken);
            e.printStackTrace();
        }
    }

    /**
     * Gets a list of teams in the organization.
     * @return A List of all TA teams in the organization.
     */
    public List<TATeam> getTeams() {
        List<TATeam> teams = new ArrayList<>();
        try {
            for (Map.Entry<String, GHTeam> entry : this.organization.getTeams().entrySet()) {
                TATeam team = new TATeam(entry.getKey(), -1);
                team.setGithubTeam(entry.getValue());
                for (GHUser user : entry.getValue().getMembers()) {
                    team.addMember(new TeachingAssistant(-1, user.getName(), user.getEmail(), user.getLogin()));
                }
                teams.add(team);
            }
        } catch (IOException e) {
            logger.severe("Could not get a list of teams in the organization.\n" + e.getMessage());
            e.printStackTrace();
        }
        return teams;
    }

    /**
     * Gets a list of all teaching assistants, or members, in the organization.
     * @return A List of teaching assistants, and empty if an error occurred.
     */
    public List<TeachingAssistant> getMembers() {
        List<TeachingAssistant> teachingAssistants = new ArrayList<>();
        try {
            for (GHUser member : this.organization.listMembers().asList()) {
                teachingAssistants.add(new TeachingAssistant(-1, member.getName(), member.getEmail(), member.getLogin()));
            }
        } catch (IOException e) {
            logger.severe("Could not get list of members in the organization.\n" + e.getMessage());
            e.printStackTrace();
        }
        return teachingAssistants;
    }

    /**
     * Sets up the organization's assignments repository, and grants permissions to all teaching assistants.
     * @param assignmentsRepoName The name of the assignments repository.
     * @param description The description of the repository.
     * @param allTeachingAssistants The name of the team consisting of all teaching assistants.
     * @throws IOException If an HTTP request failed.
     */
    public void setupAssignmentsRepo(String assignmentsRepoName, String description, String allTeachingAssistants) throws IOException {
        GHTeam team = this.organization.getTeamByName(allTeachingAssistants);
        // Check if the repository already exists.
        GHRepository existingRepo = this.organization.getRepository(assignmentsRepoName);
        if (existingRepo != null) {
            InitializerApp.organization.addError(new Error(Severity.MINOR, "Assignments repository already existed, deleting it."));
            existingRepo.delete();
            logger.fine("Deleted pre-existing assignments repository.");
        }

        this.assignmentsRepo = this.createRepository(assignmentsRepoName, team, description, false, true, false);

        if (this.assignmentsRepo == null) {
            logger.severe("Could not create assignments repository.");
            return;
        }

        this.protectMasterBranch(this.assignmentsRepo, team);

        // Grant all teaching assistants write access.
        team.add(this.assignmentsRepo, GHOrganization.Permission.ADMIN);
        logger.fine("Gave admin rights to all teaching assistants in team: " + team.getName());
    }

    /**
     * Creates and sets up a student team's repository, and invites those students to the organization's assignments
     * repository as well.
     * @param team The student team to set up.
     * @param taTeam The team of teaching assistants that is responsible for these students.
     * @param prefix The prefix to append to the front of the repo name.
     */
    public void setupStudentTeam(StudentTeam team, TATeam taTeam, String prefix) {
        // First check that the assignments repo exists, otherwise no invitations can be sent.
        if (this.assignmentsRepo == null) {
            logger.warning("Assignments repository must be created before student repositories.");
            return;
        }

        GHRepository repo = this.createRepository(team.generateUniqueName(prefix), taTeam.getGithubTeam(), team.generateRepoDescription(), false, true, false);

        if (repo == null) {
            logger.severe("Repository for student team " + team.getId() + " could not be created.");
            return;
        }

        this.protectMasterBranch(repo, taTeam.getGithubTeam());
        this.createDevelopmentBranch(repo);
        this.addTATeamAsAdmin(repo, taTeam.getGithubTeam());
        this.inviteStudentsToRepos(team, repo);

        team.setRepository(repo);
        team.setTaTeam(taTeam);
    }

    /**
     * Deletes all repositories in the organization.
     * @param substring The substring which repository names should contain to be deleted.
     */
    public void deleteAllRepositories(String substring) {
        List<GHRepository> repositories = this.organization.listRepositories().asList();
        for (GHRepository repo : repositories) {
            if (repo.getName().contains(substring)) {
                try {
                    repo.delete();
                } catch (IOException e) {
                    InitializerApp.organization.addError(new Error(Severity.HIGH, "Could not delete repository: " + repo.getName()));
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Archives all repositories whose name contains the given substring.
     * @param sub Any repository containing this substring will be archived.
     */
    public void archiveAllRepositories(String sub) {
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
     */
    private void archiveRepository(GHRepository repo) {
        try {
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
        } catch (IOException e) {
            InitializerApp.organization.addError(new Error(Severity.HIGH, "Could not archive repository: " + repo.getName()));
        }
    }

    /**
     * Invites students in a team to their repository, and the assignments repository.
     * @param team The team of students to invite as collaborators.
     * @param repo The repository created for the students.
     */
    private void inviteStudentsToRepos(StudentTeam team, GHRepository repo) {
        try {
            logger.finest("Adding students from team: " + team.getId() + " as collaborators.");
            List<GHUser> users = new ArrayList<>();
            for (Student student : team.getStudents()) {
                GHUser user = this.github.getUser(student.getGithubUsername());
                users.add(user);
            }

            repo.addCollaborators(users);
            this.assignmentsRepo.addCollaborators(users);
        } catch (IOException e) {
            InitializerApp.organization.addError(new Error(Severity.HIGH, "Students in team: " + team + " could not be added as collaborators to assignments repo or their repository."));
            logger.warning("Could not add students as collaborators to assignments or their repo.");
        }
    }

    /**
     * Adds a teaching assistant team as admins to a particular student repository.
     * @param studentRepo The student repository.
     * @param taTeam The team to give admin rights.
     */
    private void addTATeamAsAdmin(GHRepository studentRepo, GHTeam taTeam) {
        try {
            taTeam.add(studentRepo, GHOrganization.Permission.ADMIN);
            logger.fine("Added team " + taTeam.getName() + " as admin to repository: " + studentRepo.getName());
        } catch (IOException e) {
            InitializerApp.organization.addError(new Error(Severity.HIGH, "Could not add TA Team: " + taTeam.getName() + " as ADMIN to repository: " + studentRepo.getName()));
            logger.severe("Could not add TA Team: " + taTeam.getName() + " as admins to repository: " + studentRepo.getName());
        }
    }

    /**
     * Protects the master branch of a given repository, and gives admin rights to the given team.
     * @param repo The repository to protect the master branch of.
     * @param team The team which gets admin rights to the master branch.
     */
    @SuppressWarnings("deprecation")
    private void protectMasterBranch(GHRepository repo, GHTeam team) {
        try {
            GHBranchProtectionBuilder protectionBuilder = repo.getBranch("master").enableProtection();
            protectionBuilder.includeAdmins(false);
            protectionBuilder.restrictPushAccess();
            protectionBuilder.teamPushAccess(team);
            protectionBuilder.addRequiredChecks("ci/circleci");
            protectionBuilder.enable();
            logger.fine("Protected master branch of repository: " + repo.getName());
        } catch (IOException e) {
            InitializerApp.organization.addError(new Error(Severity.HIGH, "Could not protect master branch of repository: " + repo.getName()));
            logger.severe("Could not protect master branch of repository: " + repo.getName());
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
            InitializerApp.organization.addError(new Error(Severity.HIGH, "Could not create development branch of repository: " + repo.getName()));
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
     * @return The repository that was created, or null if it could not be created.
     */
    private GHRepository createRepository(String name, GHTeam taTeam, String description, boolean hasWiki, boolean hasIssues, boolean isPrivate){
        try {
            GHCreateRepositoryBuilder builder = this.organization.createRepository(name);
            builder.team(taTeam);
            builder.wiki(hasWiki);
            builder.issues(hasIssues);
            builder.description(description);
            builder.gitignoreTemplate("Java");
            builder.private_(isPrivate);
            GHRepository repo = builder.create();
            logger.fine("Created repository: " + repo.getName());
            return repo;
        } catch (IOException e) {
            logger.severe("Could not create repository: " + name + '\n' + e.getMessage());
            InitializerApp.organization.addError(new Error(Severity.CRITICAL, "Could not create repository: " + name));
            e.printStackTrace();
            return null;
        }
    }

}
