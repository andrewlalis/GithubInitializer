package nl.andrewlalis.git_api;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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
        listMemberTeams();
        //this.createRepository(this.assignmentsRepo, )
    }

    private boolean createRepository(String repositoryName, TATeam taTeam) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("name", repositoryName));
        nvps.add(new BasicNameValuePair("private", "True"));
        nvps.add(new BasicNameValuePair("has_issues", "True"));
        nvps.add(new BasicNameValuePair("has_wiki", "True"));
        nvps.add(new BasicNameValuePair("team_id", taTeam.getId()));
        nvps.add(new BasicNameValuePair("auto_init", "False"));
        nvps.add(new BasicNameValuePair("gitignore_template", "Java"));
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(this.urlBuilder.buildRepoURL());
        try {
            post.setEntity(new StringEntity(nvps.toString()));
            post.setHeader("Content-type", "application/json");
            HttpResponse response = client.execute(post);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Obtains a list of all teams in the organization, meaning all teaching assistant teams.
     * @return A list of all teams in the organization.
     */
    private Map<String, String> listMemberTeams() {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(this.urlBuilder.buildTeamURL());
        try (CloseableHttpResponse response = client.execute(get)) {
            StatusLine line = response.getStatusLine();
            logger.finest("Response code from listing member teams: " + line.getStatusCode());
            logger.finest("Response entity: " + response.getEntity().toString());
            HashMap<String,Object> result =
                    new ObjectMapper().readValue(response.getEntity().getContent(), HashMap.class);
            for (Map.Entry<String, Object> e : result.entrySet()) {
                logger.finest(e.getKey() + ": " + e.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
