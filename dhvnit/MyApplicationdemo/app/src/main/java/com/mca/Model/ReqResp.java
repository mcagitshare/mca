package com.mca.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ReqResp extends RealmObject {

    @PrimaryKey
    private String id;

    private String batchId;
    private boolean processed;
    private long requestTime, responseTime;
    private String response;
    private String groupId;

    public ReqResp() {
    }

    public ReqResp(String id, String batchId, boolean processed, String response, long requestTime,
                   long responseTime, String groupid) {
        this.id = id;
        this.groupId = groupid;
        this.batchId = batchId;
        this.processed = processed;
        this.requestTime = requestTime;
        this.responseTime = responseTime;
        this.response = response;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }
}