package nl.andrewlalis.git_api;

import nl.andrewlalis.model.Team;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for initializing the Github repositories and setting permissions, adding teams, etc.
 */
public class Initializer {

    private String organizationName;

    private String accessToken;

    private URLBuilder urlBuilder;

    private String assignmentsRepo;

    public Initializer(String organizationName, String accessToken, String assignmentsRepo) {
        this.organizationName = organizationName;
        this.accessToken = accessToken;
        this.assignmentsRepo = assignmentsRepo;
        this.urlBuilder = new URLBuilder(organizationName, accessToken);
    }

    /**
     * Initializes the github repository for all teams given.
     *
     * Creates for each team:
     * - a repository
     * - protected master branch
     * - development branch
     * - adds students to repository
     * - creates assignments repository
     * - adds all students to assignments repository.
     * @param teams The list of student teams.
     */
    public void initializeGithubRepos(List<Team> teams) {
        listMemberTeams();
    }

    private Map<String, String> listMemberTeams() {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(this.urlBuilder.buildTeamURL());
        try (CloseableHttpResponse response = client.execute(get)) {
            System.out.println(response.getStatusLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
