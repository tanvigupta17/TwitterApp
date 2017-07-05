package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by tanvigupta on 7/5/17.
 */

public class SearchTweetsFragment extends TweetsListFragment {

    TwitterClient client;

    public static SearchTweetsFragment newInstance(String query) {
        SearchTweetsFragment searchTweetsFragment = new SearchTweetsFragment();
        Bundle args = new Bundle();
        args.putString("query", query);
        searchTweetsFragment.setArguments(args);

        return searchTweetsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApp.getRestClient();

        populateTimeline();
    }

    @Override
    public void populateTimeline() {
        // unpackage bundle that comes from activity
        String query = getArguments().getString("query");
        // Make network request to get data from Twitter API
        client.getSearchResults(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

            // use this method since array response is expected
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());

//                try {
//                    oldest = Tweet.fromJSON(response.getJSONObject(0)).uid;
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                // iterate through array
//                for (int i = 0; i < response.length(); i++) {
//                    // for each entry, deserialize JSON object, convert to Tweet
//                    // add Tweet to list, notify adapter that dataset has changed
//                    try {
//                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
//
//                        if (tweet.uid < oldest) {
//                            oldest = tweet.uid;
//                        }
//
//                        tweets.add(tweet);
//                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                scrollListener.resetState();
                addItems(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }

    @Override
    public void fetchTimelineAsync(int page) {
        // unpackage bundle that comes from activity
        String query = getArguments().getString("query");
        // Send the network request to fetch the updated data
        client.getSearchResults(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                try {
//                    // clear old items before appending new ones
//                    tweetAdapter.clear();
//
//                    List<Tweet> new_tweets = new ArrayList<Tweet>();
//
//                    for (int i = 0; i < response.length(); i++) {
//                        new_tweets.add(Tweet.fromJSON(response.getJSONObject(i)));
//                    }
//
//                    // add new items to adapter
//                    tweetAdapter.addAll(new_tweets);
//
//                    // Now we call setRefreshing(false) to signal refresh has finished
//                    swipeContainer.setRefreshing(false);
//
//                    scrollListener.resetState();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                refreshItems(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                swipeContainer.setRefreshing(false);
            }
        });
    }
}
