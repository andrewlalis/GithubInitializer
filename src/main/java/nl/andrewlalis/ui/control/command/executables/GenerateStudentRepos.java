package nl.andrewlalis.ui.control.command.executables;

import nl.andrewlalis.git_api.GithubManager;

public class GenerateStudentRepos extends GithubExecutable {

    @Override
    protected boolean executeWithManager(GithubManager manager, String[] args) {
        return false;
    }

}
