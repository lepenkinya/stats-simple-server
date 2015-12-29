package com.intellij.stats.completion;

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
import java.util.Set;

@Path("/stats")
@Produces(MediaType.APPLICATION_JSON)
public class UploaderResource {
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

        Set<String> ids = mySaver.getAllUserIds();
        for (String id : ids) {
            ContentInfo contentInfo = mySaver.getInfoFor(id);
            Date firstSentDate = new Date(contentInfo.firstSentTimestamp);
            Date lastSentDate = new Date(contentInfo.lastSentTimestamp);
            builder.append("User: ").append(id)
                    .append(" Total size (Kb): ").append(contentInfo.receivedDataKb)
                    .append(" First sent: ").append(firstSentDate)
                    .append(" Last sent: ").append(lastSentDate);
            builder.append('\n');
        }

        return builder.toString();
    }
    
    
    
}