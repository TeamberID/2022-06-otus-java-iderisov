package ru.otus;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientWithCache;

public class DbServiceWithCacheDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceWithCacheDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    private static DBServiceClient dbServiceClient;

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
///
        dbServiceClient = new DbServiceClientWithCache(transactionManager, clientTemplate);

        Client firstClient = new Client("dbServiceFirst");
        firstClient = dbServiceClient.saveClient(firstClient);

        long requestTime = calculateRequestTime(firstClient);

        long requestTimeWithCache = calculateRequestTime(firstClient);

        System.out.println("#######################################");
        System.out.println("DB: " + requestTime + ", CACHE: " + requestTimeWithCache);
    }


    private static long calculateRequestTime(Client client){
        long start = System.currentTimeMillis();

        dbServiceClient.getClient(client.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + client.getId()));

        return System.currentTimeMillis() - start;

    }


}
