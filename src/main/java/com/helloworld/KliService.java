package com.helloworld;

import org.skife.jdbi.v2.DBI;
import org.postgresql.*;
import com.helloworld.health.TemplateHealthCheck;
import com.helloworld.jdbi.KliDAO;
import com.helloworld.resources.CampaignResource;
import com.helloworld.resources.KliResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.jdbi.DBIFactory;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import redis.clients.jedis.Jedis;

public class KliService extends Service<KliConfiguration> {
	public static void main(String[] args) throws Exception {
		new KliService().run(args);
	}

	@Override
	public void initialize(Bootstrap<KliConfiguration> bootstrap) {
		bootstrap.setName("hello-world");

	}

	@Override
	public void run(KliConfiguration configuration, Environment environment) {
		final String template = configuration.getTemplate();
		final String defaultName = configuration.getDefaultName();

		

		final DatabaseConfiguration data = new DatabaseConfiguration();
		data.setDriverClass("org.postgresql.Driver");
		data.setUrl("jdbc:postgresql://localhost:5432/segmintone");
		data.setUser("postgres");
		data.setPassword("SQL");

		// final DBIFactory factory = new DBIFactory();
		DBI jdbi = new DBI("jdbc:postgresql://localhost:5432/segmintone",
				"postgres", "SQL");
		// final DBI jdbi = factory.build(environment,data,"postgresql");

		final KliDAO dao = jdbi.onDemand(KliDAO.class);

		environment.addResource(new KliResource(dao));

		
		environment.addHealthCheck(new TemplateHealthCheck(template));

	}

}