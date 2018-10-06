package nl.andrewlalis.model.database;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * A utility class for easier interaction with the Hibernate database.
 */
public class DbUtil {

    private static SessionFactory sessionFactory;

    /**
     * Set up the session factory based on hibernate.cfg.xml.
     */
    private static void setUp() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    /**
     * Close the session factory when it's no longer needed.
     */
    public static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    /**
     * Gets the session factory so that sessions can be made.
     * @return The session factory.
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            setUp();
        }
        return sessionFactory;
    }
}
