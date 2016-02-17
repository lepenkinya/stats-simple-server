package com.intellij.stats.completion;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.UUID;

@Path("experiment")
public class ExperimentResource {
    private final String salt = getSalt(); 
    private final int experimentVersion = 1;
    
    private static String getSalt() {
        String uuid = UUID.randomUUID().toString();
        return uuid.split("-")[0];
    }
    
    @Path("info/{uid}")
    public ExperimentInfo getExperimentInfo(@PathParam("uid") String uid) {
        int hash = (uid + salt).hashCode();
        boolean performExperiment = hash % 2 == 0;
        return new ExperimentInfo(experimentVersion, performExperiment);
    }
    
}

class ExperimentInfo {
    private int experimentVersion = 0;
    private boolean performExperiment = false;

    ExperimentInfo(int experimentVersion, boolean performExperiment) {
        this.experimentVersion = experimentVersion;
        this.performExperiment = performExperiment;
    }
}
