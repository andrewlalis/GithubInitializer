package nl.andrewlalis.git_api;

public class URLBuilder {

    /**
     * The main URL from which all requests must be formed.
     */
    private static final String baseURL = "https://api.github.com";

    /**
     * The name of the github organization in which to create repositories.
     */
    private String organizationName;

    /**
     * The token needed to use the API.
     */
    private String accessToken;

    public URLBuilder(String organizationName, String accessToken) {
        this.organizationName = organizationName;
        this.accessToken = accessToken;
    }

    /**
     * @return The URL for adding a repository.
     */
    public String buildRepoURL() {
        return baseURL
                + "/orgs/"
                + this.organizationName
                + "/repos?access_token="
                + this.accessToken;
    }

    /**
     * @param repoName The name of the repository.
     * @param branch The name of the branch in the repository above.
     * @return The URL for setting branch protection.
     */
    public String buildBranchProtectionURL(String repoName, String branch) {
        return baseURL
                + "/repos/"
                + this.organizationName
                + '/'
                + repoName
                + "/branches/"
                + branch
                + "/protection?access_token="
                + this.accessToken;
    }

    /**
     * @param repoName The name of the repository the branch is in.
     * @return The URL for getting a branch reference.
     */
    public String buildReferenceGetURL(String repoName) {
        return baseURL
                + "/repos/"
                + this.organizationName
                + '/'
                + repoName
                + "/git/refs/heads/master?acces_token="
                + this.accessToken;
    }

    /**
     * @param repoName The repository name.
     * @return The URL for creating a new branch, once a reference has been obtained.
     */
    public String buildReferencePostURL(String repoName) {
        return baseURL
                + "/repos/"
                + this.organizationName
                + '/'
                + repoName
                + "/git/refs?access_token="
                + this.accessToken;
    }

    /**
     * @param repoName The name of the repository.
     * @param collaborator The collaborator's name.
     * @return The URL for adding a collaborator to a repository.
     */
    public String buildCollaboratorURL(String repoName, String collaborator) {
        return baseURL
                + "/repos/"
                + this.organizationName
                + '/'
                + repoName
                + "/collaborators/"
                + collaborator
                + "?access_token="
                + this.accessToken;
    }

    /**
     * @return The URL for obtaining the teams.
     */
    public String buildTeamURL() {
        return baseURL
                + "/orgs/"
                + this.organizationName
                + "/teams?access_token="
                + this.accessToken;
    }

    /**
     * @param repoName The name of the repository.
     * @param teamName The name of the team to set permissions for.
     * @return The URL for setting team permissions of a repository.
     */
    public String buildTeamPermissionsURL(String repoName, String teamName) {
        return baseURL
                + "/teams/"
                + teamName
                + "/repos/"
                + this.organizationName
                + '/'
                + repoName
                + "?access_token="
                + this.accessToken;
    }

    /**
     * @param repoName The name of the repository.
     * @return The URL for archiving a repository.
     */
    public String buildArchiveRepoURL(String repoName) {
        return baseURL
                + "/repos/"
                + this.organizationName
                + '/'
                + repoName
                + "?access_token="
                + this.accessToken;
    }

}
