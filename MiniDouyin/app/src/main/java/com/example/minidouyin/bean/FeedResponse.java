package com.example.minidouyin.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Xavier.S
 * @date 2019.01.20 14:17
 */
public class FeedResponse {

    // TODO-C2 (2) Implement your FeedResponse Bean here according to the response json
    @SerializedName("success") private boolean success;
    @SerializedName("feeds") private List<Feed> feeds;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
    }

    public List<Feed> getFeeds() {
        return feeds;
    }


}
