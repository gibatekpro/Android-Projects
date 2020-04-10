package com.devapp.usbhostmanager.models;

public class InterfaceModel {
    private String className;
    private boolean successfulConnection;
    private boolean successfulDisconnection;
    private int numberOfEndpoints;
    private int totalNumInEndpoints = 0;
    private int totalOutEndpoints = 0;
    private int maxPacketSize = 0;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isSuccessfulConnection() {
        return successfulConnection;
    }

    public void setSuccessfulConnection(boolean successfulConnection) {
        this.successfulConnection = successfulConnection;
    }

    public int getNumberOfEndpoints() {
        return numberOfEndpoints;
    }

    public void setNumberOfEndpoints(int numberOfEndpoints) {
        this.numberOfEndpoints = numberOfEndpoints;
    }

    public int getTotalNumInEndpoints() {
        return totalNumInEndpoints;
    }

    public void setTotalNumInEndpoints(int totalNumInEndpoints) {
        this.totalNumInEndpoints = totalNumInEndpoints;
    }

    public int getTotalOutEndpoints() {
        return totalOutEndpoints;
    }

    public void setTotalOutEndpoints(int totalOutEndpoints) {
        this.totalOutEndpoints = totalOutEndpoints;
    }

    public int getMaxPacketSize() {
        return maxPacketSize;
    }

    public void setMaxPacketSize(int maxPacketSize) {
        if (maxPacketSize > this.maxPacketSize) {
            this.maxPacketSize = maxPacketSize;
        }
    }

    public boolean isSuccessfulDisconnection() {
        return successfulDisconnection;
    }

    public void setSuccessfulDisconnection(boolean successfulDisconnection) {
        this.successfulDisconnection = successfulDisconnection;
    }
}
