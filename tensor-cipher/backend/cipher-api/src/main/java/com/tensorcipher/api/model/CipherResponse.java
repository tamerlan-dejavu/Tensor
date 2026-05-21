package com.tensorcipher.api.model;

public class CipherResponse {

    private String result;
    private String operation;
    private long processingTimeMs;

    public CipherResponse() {}

    public CipherResponse(String result, String operation, long processingTimeMs) {
        this.result = result;
        this.operation = operation;
        this.processingTimeMs = processingTimeMs;
    }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }

    public long getProcessingTimeMs() { return processingTimeMs; }
    public void setProcessingTimeMs(long processingTimeMs) { this.processingTimeMs = processingTimeMs; }
}
