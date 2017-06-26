package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tanvigupta on 6/26/17.
 */

public class Tweet {

    // attributes of the tweet class
    public String body;
    public long uid; // database ID for the tweet
    public String createdAt;
    public User user;

    // deserialize JSON - take in JSON object, return Tweet object
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));

        return tweet;
    }
}
