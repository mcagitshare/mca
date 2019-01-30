package com.mca.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ReqResp extends RealmObject {

    @PrimaryKey
    private String id;

    private int batchId;
    private boolean processed;
    private long requestTime, responseTime;
    private String response;

    public ReqResp() {
    }

    public ReqResp(String id, int batchId, boolean processed, String response, long requestTime, long responseTime) {
        this.id = id;
        this.batchId = batchId;
        this.processed = processed;
        this.requestTime = requestTime;
        this.responseTime = responseTime;
        this.response = response;
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

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }
}