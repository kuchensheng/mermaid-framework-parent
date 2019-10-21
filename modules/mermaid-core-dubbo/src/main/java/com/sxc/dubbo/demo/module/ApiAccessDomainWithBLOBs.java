package com.sxc.dubbo.demo.module;

import java.io.Serializable;

public class ApiAccessDomainWithBLOBs extends ApiAccessDomain implements Serializable {
    private String requestSnapshot;

    private String responseSnapshot;

    private String callSnapshot;

    private static final long serialVersionUID = 1L;

    public String getRequestSnapshot() {
        return requestSnapshot;
    }

    public void setRequestSnapshot(String requestSnapshot) {
        this.requestSnapshot = requestSnapshot == null ? null : requestSnapshot.trim();
    }

    public String getResponseSnapshot() {
        return responseSnapshot;
    }

    public void setResponseSnapshot(String responseSnapshot) {
        this.responseSnapshot = responseSnapshot == null ? null : responseSnapshot.trim();
    }

    public String getCallSnapshot() {
        return callSnapshot;
    }

    public void setCallSnapshot(String callSnapshot) {
        this.callSnapshot = callSnapshot == null ? null : callSnapshot.trim();
    }
}