package com.intellij.stats.completion;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;

public class Collector extends Application<Configuration> {

    public static void main(String[] args) throws Exception {
        new Collector().run(args);
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        UploaderResource resource = new UploaderResource();
        environment.jersey().register(resource);
    }

}

