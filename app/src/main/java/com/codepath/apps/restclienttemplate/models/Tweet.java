package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by tanvigupta on 6/26/17.
 */

@Parcel
public class Tweet {

    // attributes of the tweet class
    public String body;
    public long uid; // database ID for the tweet
    public String createdAt;
    public User user;
    public String mediaUrl;
    public boolean retweeted;
    public boolean favorited;

    public Tweet() {
    }

    // deserialize JSON - take in JSON object, return Tweet object
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));

        if (jsonObject.has("entities") && jsonObject.getJSONObject("entities").has("media")) {
            tweet.mediaUrl = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url");
        } else {
            tweet.mediaUrl = "";
        }

        tweet.retweeted = jsonObject.getBoolean("retweeted");
        tweet.favorited = jsonObject.getBoolean("favorited");

        return tweet;
    }
}
