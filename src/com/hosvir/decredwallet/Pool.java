package com.hosvir.decredwallet;

/**
 * @author captain-redbeard
 * @version 1.00
 * @since 3/04/17
 */
public class Pool {
    private String name;
    private String network;
    private String url;
    private boolean apiEnabled;
    private long immature;
    private long live;
    private long voted;
    private long missed;
    private double poolFees;
    private double proportionLive;
    private long userCount;

    public Pool(String name, String network, String url, boolean apiEnabled, long immature, long live, long voted,
                long missed, double poolFees, double proportionLive, long userCount) {
        this.name = name;
        this.network = network;
        this.url = url;
        this.apiEnabled = apiEnabled;

        this.immature = immature;
        this.live = live;
        this.voted = voted;
        this.missed = missed;
        this.poolFees = poolFees;
        this.proportionLive = proportionLive;
        this.userCount = userCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isApiEnabled() {
        return apiEnabled;
    }

    public void setApiEnabled(boolean apiEnabled) {
        this.apiEnabled = apiEnabled;
    }

    public long getImmature() {
        return immature;
    }

    public void setImmature(long immature) {
        this.immature = immature;
    }

    public long getLive() {
        return live;
    }

    public void setLive(long live) {
        this.live = live;
    }

    public long getVoted() {
        return voted;
    }

    public void setVoted(long voted) {
        this.voted = voted;
    }

    public long getMissed() {
        return missed;
    }

    public void setMissed(long missed) {
        this.missed = missed;
    }

    public double getPoolFees() {
        return poolFees;
    }

    public void setPoolFees(double poolFees) {
        this.poolFees = poolFees;
    }

    public double getProportionLive() {
        return proportionLive;
    }

    public void setProportionLive(double proportionLive) {
        this.proportionLive = proportionLive;
    }

    public long getUserCount() {
        return userCount;
    }

    public void setUserCount(long userCount) {
        this.userCount = userCount;
    }
}
