package nl.andrewlalis.git_api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.andrewlalis.model.Student;
import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.model.TATeam;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This class is responsible for initializing the Github repositories and setting permissions, adding teams, etc.
 */
public class Initializer {

    /**
     * The name of the organization to operate on.
     */
    private String organizationName;

    /**
     * The oauth access token needed for the request.
     */
    private String accessToken;

    /**
     * The object which simplifies creating REST URL's for most of the requests.
     */
    private URLBuilder urlBuilder;

    /**
     * The name of the assignments repository where students will get assignments from.
     */
    private String assignmentsRepo;

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
     * The HttpClient object to use with all requests.
     */
    private HttpClient client;

    /**
     * ObjectMapper to use when constructing JSON objects.
     */
    private ObjectMapper mapper;

    /**
     * The logger for outputting debug info.
     */
    private static final Logger logger = Logger.getLogger(Initializer.class.getName());
    static {
        logger.setParent(Logger.getGlobal());
    }

    public Initializer(String organizationName, String accessToken, String assignmentsRepo, String teachingAssistantsTeamName, String studentRepoPrefix) {
        this.organizationName = organizationName;
        this.accessToken = accessToken;
        this.assignmentsRepo = assignmentsRepo;
        this.teachingAssistantsTeamName = teachingAssistantsTeamName;
        this.studentRepoPrefix = studentRepoPrefix;
        this.urlBuilder = new URLBuilder(organizationName, accessToken);
        this.client = HttpClientBuilder.create().build();
        this.mapper = new ObjectMapper();
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
        List<TATeam> taTeams = listMemberTeams();
        TATeam teamAll = this.getTeamAll(taTeams, this.teachingAssistantsTeamName);

        this.setupAssignmentsRepo(teamAll);

    }

    /**
     * Sets up the organization's assignments repository, and grants permissions to all teaching assistants.
     * @param allTeachingAssistants A team consisting of all teaching assistants.
     * @return True if everything went successfully, or false if an error occurred.
     * @throws IOException If an HTTP request failed.
     */
    private boolean setupAssignmentsRepo(TATeam allTeachingAssistants) throws IOException {
        // First, create the assignments repository.
        boolean successCreate = this.createRepository(this.assignmentsRepo, allTeachingAssistants);
        if (successCreate) {
            logger.info("Created repository: " + this.assignmentsRepo);
        } else {
            logger.severe("Could not create repository: " + this.assignmentsRepo);
        }

        // Then, protect the master branch.
        boolean successProtect = this.protectMasterBranch(this.assignmentsRepo, allTeachingAssistants);
        if (successProtect) {
            logger.info("Protected master branch of: " + this.assignmentsRepo);
        } else {
            logger.severe("Could not protect master branch of: " + this.assignmentsRepo);
        }

        // And finally give all teaching assistants admin permissions to the repository.
        boolean successGrant = this.grantTeamPermissions(this.assignmentsRepo, allTeachingAssistants);
        if (successGrant) {
            logger.info("Granted permissions to teaching assistants for: " + this.assignmentsRepo);
        } else {
            logger.severe("Could not grant permissions to teaching assistants for: " + this.assignmentsRepo);
        }

        return successCreate && successProtect && successGrant;
    }

    /**
     * Creates and sets up a student team's repository, and invites those students to the organization's assignments
     * repository as well.
     * @param team The student team to set up.
     * @param taTeam The team of teaching assistants that is responsible for these students.
     * @return True if everything went according to plan, false if at least one error occurred.
     * @throws IOException If an HTTP request fails.
     */
    private boolean setupStudentTeam(StudentTeam team, TATeam taTeam) throws IOException {
        String teamRepoName = team.generateUniqueName(this.studentRepoPrefix);

        // First create the repository.
        boolean successCreate = this.createRepository(teamRepoName, taTeam);
        if (successCreate) {
            logger.info("Created repository: " + teamRepoName);
        } else {
            logger.severe("Could not create repository: " + teamRepoName);
        }

        // Then protect the master branch.
        boolean successProtect = this.protectMasterBranch(teamRepoName, taTeam);
        if (successProtect) {
            logger.info("Protected master branch of: " + teamRepoName);
        } else {
            logger.severe("Could not protect master branch of: " + teamRepoName);
        }

        // And finally create the development branch.
        boolean successDev = this.createDevelopmentBranch(teamRepoName);
        if (successDev) {
            logger.info("Created development branch for: " + teamRepoName);
        } else {
            logger.severe("Could not create development branch for: " + teamRepoName);
        }

        boolean successGrant = this.grantTeamPermissions(teamRepoName, taTeam);
        if (successGrant) {
            logger.info("Granted permissions for: " + teamRepoName + " to " + taTeam.getName());
        } else {
            logger.severe("Could not grant permissions for: " + teamRepoName + " to " + taTeam.getName());
        }

        Map<Student, Boolean[]> studentSuccesses = new HashMap<>();
        // For every student in the team, add them to the organization's assignments repo, and their own.
        for (Student student : team.getStudents()) {
            // First invite to their own repository.
            boolean successInviteOwn = this.addStudentToRepository(teamRepoName, student, true);
            if (successInviteOwn) {
                logger.info("Invited " + student + " to " + teamRepoName);
            } else {
                logger.severe("Could not invite " + student + " to " + teamRepoName);
            }

            // Then invite the student to the assignments repo.
            boolean successInviteAssign = this.addStudentToRepository(this.assignmentsRepo, student, false);
            if (successInviteAssign) {
                logger.info("Invited " + student + " to " + this.assignmentsRepo);
            } else {
                logger.severe("Could not invite " + student + " to " + this.assignmentsRepo);
            }

            studentSuccesses.put(student, new Boolean[]{successInviteOwn, successInviteAssign});
        }

        // Calculate total success.
        boolean allStudentSuccess = true;
        for (Map.Entry<Student, Boolean[]> entry : studentSuccesses.entrySet()) {
            for (boolean b : entry.getValue()) {
                allStudentSuccess &= b;
            }
        }

        return successCreate && successProtect && successDev && successGrant && allStudentSuccess;
    }

    /**
     * Invites a student as a collaborator to a repository.
     * @param repositoryName The name of the repository to invite the student to.
     * @param student The student to send the invitation to.
     * @param writeAllowed Whether or not the student is allowed to push to the repository.
     * @return True if successful and the invitation is created, false otherwise.
     * @throws IOException If an error occurs while performing a request or with json compiling.
     */
    private boolean addStudentToRepository(String repositoryName, Student student, boolean writeAllowed) throws IOException {
        // First create a simple JSON object that either gives push or pull permission.
        ObjectNode root = this.mapper.createObjectNode();
        root.put("permissions", (writeAllowed ? "push" : "pull"));
        String json = this.mapper.writeValueAsString(root);

        // Then make the request.
        HttpPut put = new HttpPut(this.urlBuilder.buildCollaboratorURL(repositoryName, student.getGithubUsername()));
        put.setEntity(new StringEntity(json));
        logger.fine("Sending PUT request to add student: " + student + " to repository: " + repositoryName);
        HttpResponse response = this.client.execute(put);
        logger.finest("Response from PUT to add student to repository: " + response.getStatusLine().getStatusCode());

        // Only if 201 is returned do we know that a collaborator is invited.
        return response.getStatusLine().getStatusCode() == 201;
    }

    /**
     * Creates the development branch needed by all student repositories.
     * @param repositoryName The name of the repository.
     * @return True if the branch could successfully be created, false otherwise.
     * @throws IOException If an error occurs while performing a request or with json compiling.
     */
    private boolean createDevelopmentBranch(String repositoryName) throws IOException {
        // First obtain a reference for the repository.
        HttpGet get = new HttpGet(this.urlBuilder.buildReferenceGetURL(repositoryName));
        HttpResponse getResponse = this.client.execute(get);
        if (getResponse.getStatusLine().getStatusCode() != 200) {
            return false;
        }
        ObjectNode responseRoot = this.mapper.valueToTree(getResponseBody(getResponse));
        String sha = responseRoot.get("object").get("sha").textValue();

        // Construct the request data.
        ObjectNode root = this.mapper.createObjectNode();
        root.put("ref", "refs/heads/development");
        root.put("sha", sha);
        String json = this.mapper.writeValueAsString(root);

        // Send the request.
        HttpPost post = new HttpPost(this.urlBuilder.buildReferencePostURL(repositoryName));
        post.setEntity(new StringEntity(json));
        logger.fine("Sending POST request to create development branch for repository: " + repositoryName);
        HttpResponse postResponse = this.client.execute(post);
        logger.finest("Response from POST to create development branch: " + postResponse.getStatusLine().getStatusCode());

        // Only if 201 is returned, is this successful.
        return postResponse.getStatusLine().getStatusCode() == 201;
    }

    /**
     * Grants the given team admin permissions to the repository with the given name.
     * @param repositoryName The name of the repository.
     * @param team The team to give admin permissions to.
     * @return True if rights were successfully granted, false otherwise.
     * @throws IOException If an error occurs while attempting to give a team admin rights over the repository.
     */
    private boolean grantTeamPermissions(String repositoryName, TATeam team) throws IOException {
        // First, create the JSON object, which is simply one value pair.
        ObjectNode root = this.mapper.createObjectNode();
        root.put("permission", "admin");
        String json = this.mapper.writeValueAsString(root);

        // Create and send request.
        HttpPut put = new HttpPut(this.urlBuilder.buildTeamPermissionsURL(repositoryName, String.valueOf(team.getId())));
        put.setEntity(new StringEntity(json));
        logger.fine("Sending PUT request to add team " + team.getName() + " as admins for repo: " + repositoryName);
        HttpResponse response = this.client.execute(put);
        logger.finest("Response from adding team as admins: " + response.getStatusLine().getStatusCode());

        // Return true only if request returns 204.
        return response.getStatusLine().getStatusCode() == 204;
    }

    /**
     * Protects the master branch of the given repository.
     * @see <a href="https://developer.github.com/v3/repos/branches/#update-branch-protection">Github Branch Protection</a>
     * @param repositoryName The repository to protect the master branch of.
     * @param taTeam The team to give rights to.
     * @return True if the master branch has been protected, false otherwise.
     * @throws IOException If an error occurs with requests or compiling json.
     */
    private boolean protectMasterBranch(String repositoryName, TATeam taTeam) throws IOException {
        // First, create quite a complex JSON object.
        // settings
        ObjectNode root = this.mapper.createObjectNode();
        // required_status_checks
        {
            ObjectNode requiredStatusChecksNode = this.mapper.createObjectNode();
            requiredStatusChecksNode.put("strict", false);
            // contexts
            {
                ArrayNode contextsNode = this.mapper.createArrayNode();
                contextsNode.add("ci/circleci");
                requiredStatusChecksNode.set("contexts", contextsNode);
            }
            root.set("required_status_checks", requiredStatusChecksNode);
        }
        root.put("enforce_admins", false);
        root.set("required_pull_request_reviews", this.mapper.createObjectNode());
        // restrictions
        {
            ObjectNode restrictionsNode = this.mapper.createObjectNode();
            restrictionsNode.set("users", this.mapper.createArrayNode());
            // teams
            {
                ArrayNode teamsNode = this.mapper.createArrayNode();
                teamsNode.add(taTeam.getName());
                restrictionsNode.set("teams", teamsNode);
            }
            root.set("restrictions", restrictionsNode);
        }
        String json = this.mapper.writer().writeValueAsString(root);

        // Create request and put the json to the server.
        HttpPut put = new HttpPut(this.urlBuilder.buildBranchProtectionURL(repositoryName, "master"));
        put.setEntity(new StringEntity(json));
        put.setHeader("Accept", "application/vnd.github.luke-cage-preview+json");
        logger.fine("Sending PUT request to protect master branch of repository: " + repositoryName);
        HttpResponse response = this.client.execute(put);
        logger.finest("Response from protecting master branch: " + response.getStatusLine().getStatusCode());
        logger.finest(getResponseBody(response));

        // Return true only if the status code is 200.
        return response.getStatusLine().getStatusCode() == 200;
    }

    /**
     * Creates a repository of the given name and assigns the given team to it.
     * @param repositoryName The name of the repository to make.
     * @param taTeam The team to assign to this repository.
     * @return True if the repository was created successfully, or false if an error occurred.
     * @throws IOException If an error occurs during http requesting or JSON processing.
     */
    private boolean createRepository(String repositoryName, TATeam taTeam) throws IOException {
        logger.info("Creating repository: " + repositoryName);

        ObjectNode root = this.mapper.createObjectNode();
        root.put("name", repositoryName);
        root.put("private", false);
        root.put("has_issues", true);
        root.put("has_wiki", true);
        root.put("team_id", taTeam.getId());
        root.put("auto_init", false);
        root.put("gitignore_template", "Java");
        String json = this.mapper.writer().writeValueAsString(root);

        // Create the request and post it.
        HttpPost post = new HttpPost(this.urlBuilder.buildRepoURL());
        post.setEntity(new StringEntity(json));
        post.setHeader("Content-type", "application/json");
        logger.fine("Sending POST request to create repository: " + repositoryName);
        HttpResponse response = this.client.execute(post);
        logger.finest("Response from creating repository POST: " + response.getStatusLine().getStatusCode());
        logger.finest(getResponseBody(response));

        // Return true if 201 is the response code, and false otherwise.
        return response.getStatusLine().getStatusCode() == 201;
    }

    /**
     * Extracts the team containing all TA's from a list of all teams.
     * @param taTeams The list of all teams in the organization.
     * @param teamAllName The name of the team representing all TA's.
     * @return The team which holds all TA's.
     * @throws Exception If no team with the given name exists.
     */
    private TATeam getTeamAll(List<TATeam> taTeams, String teamAllName) throws Exception {
        for (TATeam team : taTeams) {
            if (team.getName().equals(teamAllName)) {
                return team;
            }
        }
        throw new Exception("No team with name " + teamAllName + " could be found.");
    }

    /**
     * Obtains a list of all teams in the organization, meaning all teaching assistant teams.
     * @return A list of all teams in the organization.
     */
    private List<TATeam> listMemberTeams() {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(this.urlBuilder.buildTeamURL());
        try (CloseableHttpResponse response = client.execute(get)) {
            StatusLine line = response.getStatusLine();
            logger.finer("Response code from listing member teams: " + line.getStatusCode());
            String result = getResponseBody(response);
            logger.finer("Response entity: " + result);

            ObjectMapper mapper = new ObjectMapper();
            List<TATeam> memberTeams = mapper.readValue(result, new TypeReference<List<TATeam>>(){});
            for (TATeam r : memberTeams) {
                logger.finest(r.toString());
            }
            return memberTeams;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Extracts a string which represents the response body of an HttpResponse.
     * @param response The response to a previous request.
     * @return A string containing the whole response body.
     */
    private static String getResponseBody(HttpResponse response) {
        try {
            return new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
