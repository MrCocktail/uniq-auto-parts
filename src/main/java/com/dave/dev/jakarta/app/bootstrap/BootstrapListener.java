package com.dave.dev.jakarta.app.bootstrap;

import com.dave.dev.jakarta.app.dao.EmployeeDAO;
import com.dave.dev.jakarta.app.models.Employee;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.Locale;
import org.mindrot.jbcrypt.BCrypt;

@WebListener
public class BootstrapListener implements ServletContextListener {

    private static final String DEFAULT_EMAIL = "admin@uniq-auto-parts.local";
    private static final String DEFAULT_PASSWORD = "Admin123!";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            EmployeeDAO employeeDAO = new EmployeeDAO();
            if (employeeDAO.findByEmail(DEFAULT_EMAIL) != null) {
                return;
            }

            Employee admin = new Employee();
            admin.setEmail(DEFAULT_EMAIL.toLowerCase(Locale.ROOT));
            admin.setPasswordHash(BCrypt.hashpw(DEFAULT_PASSWORD, BCrypt.gensalt()));
            employeeDAO.save(admin);
        } catch (Throwable ex) {
            sce.getServletContext().log("Initialisation du compte admin ignoree (connexion DB indisponible).", ex);
        }
    }
}