package io.progix.dropwizard.patch.example;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.progix.dropwizard.patch.PatchBundle;

public class App extends Application<AppConfiguration>{

	public static void main(String[] args) throws Exception {
		new App().run(args);	
	}
	
	@Override
	public void initialize(Bootstrap<AppConfiguration> bootstrap) {
		bootstrap.addBundle(new PatchBundle());
	}

	@Override
	public void run(AppConfiguration configuration, Environment environment)
			throws Exception {
		environment.jersey().register(new AppResource());
		
	}

}
