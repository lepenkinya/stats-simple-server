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
import java.util.Collection;
import java.util.Date;

@Path("/stats")
@Produces(MediaType.APPLICATION_JSON)
public class UploaderResource {
    private Logger LOG = LoggerFactory.getLogger(UploaderResource.class);
    private StatisticSaver mySaver;

    public UploaderResource(StatisticSaver saver) {
        mySaver = saver;
    }

    @Path("/upload")
    @POST
    public Response receiveContent(@FormParam("content") String value, @FormParam("uid") String uid) {
        mySaver.dataReceived(uid, value.length());

        File dir = getDataDirectory();

        File file = new File(dir, uid);
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

    private File getDataDirectory() {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir;
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
    
    
    @Path("/pluginUsers")
    @GET
    public String info() {
        Collection<ContentInfo> allData = mySaver.getAllData();
        StringBuilder builder = new StringBuilder();

        int allUsers = allData.size();

        builder.append("Total received data from ")
                .append(allUsers)
                .append(" users");

        builder.append('\n');
        builder.append('\n');

        for (ContentInfo contentInfo : allData) {
            Date date = new Date(contentInfo.timestamp);
            builder.append("User: ").append(contentInfo.receivedDataKb).append("Kb ").append("Last sent ").append(date);
            builder.append('\n');
        }

        return builder.toString();
    }
    
    
    
}