package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by tanvigupta on 6/26/17.
 */

@Parcel
public class User {

    // attributes of this class
    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;
    public String tagline;
    public int followingCount;
    public int followersCount;

    public User() {
    }

    // deserialize the JSON
    public static User fromJSON(JSONObject json) throws JSONException {
        User user = new User();

        // extract values
        user.name = json.getString("name");
        user.uid = json.getLong("id");
        user.screenName = json.getString("screen_name");
        user.profileImageUrl = json.getString("profile_image_url");
        user.tagline = json.getString("description");
        user.followersCount = json.getInt("followers_count");
        user.followingCount = json.getInt("friends_count");
        return user;
    }
}
