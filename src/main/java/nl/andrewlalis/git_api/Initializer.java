package nl.andrewlalis.git_api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.model.TATeam;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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
     * The logger for outputting debug info.
     */
    private static final Logger logger = Logger.getLogger(Initializer.class.getName());
    static {
        logger.setParent(Logger.getGlobal());
    }

    public Initializer(String organizationName, String accessToken, String assignmentsRepo) {
        this.organizationName = organizationName;
        this.accessToken = accessToken;
        this.assignmentsRepo = assignmentsRepo;
        this.urlBuilder = new URLBuilder(organizationName, accessToken);
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
     */
    public void initializeGithubRepos(List<StudentTeam> studentTeams) {
        List<TATeam> taTeams = listMemberTeams();
        TATeam teamAll = this.getTeamAll(taTeams, "teaching-assistants");
        this.createRepository(this.assignmentsRepo, teamAll);
    }

    private boolean createRepository(String repositoryName, TATeam taTeam) {
        logger.info("Creating repository: " + repositoryName);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("name", repositoryName);
        root.put("private", false);
        root.put("has_issues", true);
        root.put("has_wiki", true);
        root.put("team_id", taTeam.getId());
        root.put("auto_init", false);
        root.put("gitignore_template", "Java");
        String json = null;
        try {
            json = mapper.writer().writeValueAsString(root);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(this.urlBuilder.buildRepoURL());
        try {
            post.setEntity(new StringEntity(json));
            post.setHeader("Content-type", "application/json");
            HttpResponse response = client.execute(post);
            logger.info("Response from creating repository POST: " + response.getStatusLine().getStatusCode());
            logger.info(getResponseBody(response));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Extracts the team containing all TA's from a list of all teams.
     * @param taTeams The list of all teams in the organization.
     * @param teamAllName The name of the team representing all TA's.
     * @return The team which holds all TA's.
     */
    private TATeam getTeamAll(List<TATeam> taTeams, String teamAllName) {
        for (TATeam team : taTeams) {
            if (team.getName().equals(teamAllName)) {
                return team;
            }
        }
        return null;
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
