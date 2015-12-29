package com.intellij.stats.completion;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
}


class ContentInfo {
    public final double receivedDataKb;
    public final long timestamp;

    public ContentInfo(double receivedDataKb) {
        this.receivedDataKb = receivedDataKb;
        this.timestamp = System.currentTimeMillis();
    }
}
