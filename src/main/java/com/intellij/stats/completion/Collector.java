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
        StatisticSaver statSaver = new StatisticSaver();
        
        UploaderResource resource = new UploaderResource(statSaver);
        ExperimentResource experimentResource = new ExperimentResource();
        
        environment.jersey().register(resource);
        environment.jersey().register(experimentResource);
    }

}

