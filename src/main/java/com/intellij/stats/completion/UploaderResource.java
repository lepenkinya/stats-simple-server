package com.intellij.stats.completion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

@Path("/stats")
@Produces(MediaType.APPLICATION_JSON)
public class UploaderResource {
    
    Logger LOG = LoggerFactory.getLogger(UploaderResource.class);
    
    @Path("/upload")
    @POST
    public Response receiveContent(@FormParam("content") String value, @FormParam("uid") String uid) {
        LOG.info("Received data of size {} kb", ((double)value.length()) * 2 / 1024);
        
        File file = new File(uid);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return Response.serverError().build();
            }
        }
        
        try {
            BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardOpenOption.APPEND);
            writer.write(value);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        
        return Response.accepted().build();    
    }
    
    @Path("/test")
    @POST
    public void receiveContent() {
        System.out.println();
    }

    
    @Path("/get")
    @GET
    public String test() {
        return "Super check!";
    }
    
    
}