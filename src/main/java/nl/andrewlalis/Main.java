package nl.andrewlalis;

import nl.andrewlalis.command.CommandExecutor;
import nl.andrewlalis.command.executables.*;
import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.ui.view.InitializerApp;
import nl.andrewlalis.ui.view.StartView;
import nl.andrewlalis.util.CommandLine;
import nl.andrewlalis.util.Logging;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Main program entry point.
 */
public class Main {

    private static final Logger logger = Logger.getGlobal();

    public static void main(String[] args) {

        // Parsed command line arguments.
        Map<String, String> userOptions = CommandLine.parseArgs(args);

        // Initialize logger.
        Logging.setup();

        // Command executor which will be used by all actions the user can do.
        CommandExecutor executor = new CommandExecutor();

        // Initialize User Interface.
        InitializerApp app = new InitializerApp(executor);
        app.begin();
        app.setAccessToken(userOptions.get("token"));

        // Initialize executable commands.
        executor.registerCommand("read_students", new ReadStudentsFile(app));
        executor.registerCommand("archive_all", new ArchiveRepos());
        executor.registerCommand("generate_assignments", new GenerateAssignmentsRepo());
        executor.registerCommand("define_ta_teams", new DefineTaTeams(app));
        executor.registerCommand("list_errors", new ListErrors(app));
        executor.registerCommand("delete_repos", new DeleteRepos());
        executor.registerCommand("delegate_student_teams", new DelegateStudentTeams(app));
        executor.registerCommand("setup_student_repos", new SetupStudentRepos(app));
        executor.registerCommand("list_repos", new ListRepos());

        logger.info("GithubManager for Github Repositories in Educational Organizations.\n" +
                "© Andrew Lalis (2018), All rights reserved.\n" +
                "Program initialized.");

        GithubManager manager = new GithubManager();
        StartView startView = new StartView(manager, "InitializerTesting", userOptions.get("token"));

//        SessionFactory factory = DbUtil.getSessionFactory();
//        Session session = factory.openSession();
//        session.beginTransaction();
//        System.out.println(session.save(new Student(1, "a", "a@e.com", "git", null)));
//        session.getTransaction().commit();
//        session.close();
    }

}
