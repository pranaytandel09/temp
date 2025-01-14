package com.purplebits.emrd2.entity;
public class OtpData {
    private String otp;
    private long createdAt;
    private long expiryTime;
    private int attempts;

    public OtpData(String otp, long createdAt, long expiryTime) {
        this.otp = otp;
        this.createdAt = createdAt;
        this.expiryTime = expiryTime;
        this.attempts = 0;
    }

    public String getOtp() {
        return otp;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public int getAttempts() {
        return attempts;
    }

    public void incrementAttempts() {
        this.attempts++;
    }
}
//
