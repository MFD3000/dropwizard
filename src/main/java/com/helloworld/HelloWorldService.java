package com.helloworld;



import org.skife.jdbi.v2.DBI;

import com.helloworld.health.TemplateHealthCheck;
import com.helloworld.jdbi.CampaignDAO;
import com.helloworld.resources.CampaignResource;
import com.helloworld.resources.HelloWorldResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.jdbi.DBIFactory;


public class HelloWorldService extends Service<HelloWorldConfiguration> {
    public static void main(String[] args) throws Exception {
        new HelloWorldService().run(args);
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
        bootstrap.setName("hello-world");
        
    }

    @Override
    public void run(HelloWorldConfiguration configuration,
                    Environment environment) {
    	final String template = configuration.getTemplate();
        final String defaultName = configuration.getDefaultName();
        
        
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDatabaseConfiguration(),"postgresql");
        final CampaignDAO dao = jdbi.onDemand(CampaignDAO.class);
        
        environment.addResource(new CampaignResource(dao));
        
        environment.addResource(new HelloWorldResource(template, defaultName));
        environment.addHealthCheck(new TemplateHealthCheck(template));
        
    }

}