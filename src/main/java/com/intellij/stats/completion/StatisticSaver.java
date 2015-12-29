package com.intellij.stats.completion;

import java.util.*;

public class StatisticSaver {

    private Map<String, ContentInfo> myIdToKb = new HashMap<>();
    
    public void dataReceived(String fromId, int contentLength) {
        double newContentKb = ((double) contentLength * 2) / 1024;
        ContentInfo info = myIdToKb.get(fromId);
        double newSizeKb = info == null ? newContentKb : (info.receivedDataKb + newContentKb);
        myIdToKb.put(fromId, new ContentInfo(newSizeKb));
    }
    
    public Collection<ContentInfo> getAllData() {
        return myIdToKb.values();
    }

    public Set<String> getAllUserIds() {
        return myIdToKb.keySet();
    }

    public ContentInfo getInfoFor(String uid) {
        return myIdToKb.get(uid);
    }

}


class ContentInfo {
    public final double receivedDataKb;
    public final long timestamp;

    public ContentInfo(double receivedDataKb) {
        this.receivedDataKb = receivedDataKb;
        this.timestamp = System.currentTimeMillis();
    }
}
