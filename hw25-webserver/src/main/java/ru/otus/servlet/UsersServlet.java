package ru.otus.servlet;

import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.services.TemplateProcessor;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class UsersServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_ATTR_ITEMS = "items";
    private static final String CLIENT_NAME = "clientName";


    private final TemplateProcessor templateProcessor;

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private Configuration configuration;
    private DBServiceClient dbServiceClient;

    public UsersServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
        initHiber();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter(CLIENT_NAME);
        dbServiceClient.saveClient(new Client(name));

        writeResponse(response);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        writeResponse(response);
    }

    private void writeResponse(HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();

        var items = dbServiceClient.findAll();

        paramsMap.put(TEMPLATE_ATTR_ITEMS, items);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));

    }


    private void initHiber() {
        configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        dbServiceClient.saveClient(new Client("dbServiceFirst"));
        dbServiceClient.saveClient(new Client("dbServiceSecond"));
    }

}
