package com.codepath.apps.restclienttemplate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.MyDividerItemDecoration;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * Created by tanvigupta on 7/3/17.
 */

public class TweetsListFragment extends Fragment {

    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;

    @BindView(R.id.rvTweet) RecyclerView rvTweets;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    private final int COMPOSE_REQUEST = 10;
    private final int DETAIL_REQUEST = 20;

    long oldest;
    private EndlessRecyclerViewScrollListener scrollListener;

    private Unbinder unbinder;

    // inflation happens inside onCreateView
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the view
        View v = inflater.inflate(R.layout.fragments_tweets_list, container, false);
        unbinder = ButterKnife.bind(this, v);

        oldest = 0;

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // initialize arraylist
        tweets = new ArrayList<>();

        //construct the adapter from the array list
        tweetAdapter = new TweetAdapter(tweets, getContext());

        // RecyclerView setup (layout manager, use adapter)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.setAdapter(tweetAdapter);
        MyDividerItemDecoration dividerItemDecoration = new MyDividerItemDecoration(rvTweets.getContext());
        rvTweets.addItemDecoration(dividerItemDecoration);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                populateTimeline();
            }
        };
        // Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void addItems(JSONArray response) {
        try {
            oldest = Tweet.fromJSON(response.getJSONObject(0)).uid;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // iterate through array
        for (int i = 0; i < response.length(); i++) {
            // for each entry, deserialize JSON object, convert to Tweet
            // add Tweet to list, notify adapter that dataset has changed
            try {
                Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));

                if (tweet.uid < oldest) {
                    oldest = tweet.uid;
                }

                tweets.add(tweet);
                tweetAdapter.notifyItemInserted(tweets.size() - 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        scrollListener.resetState();
    }

    public void populateTimeline() {}

    public void refreshItems(JSONArray response) {
        try {
            // clear old items before appending new ones
            tweetAdapter.clear();

            List<Tweet> new_tweets = new ArrayList<Tweet>();

            for (int i = 0; i < response.length(); i++) {
                new_tweets.add(Tweet.fromJSON(response.getJSONObject(i)));
            }

            // add new items to adapter
            tweetAdapter.addAll(new_tweets);

            // Now we call setRefreshing(false) to signal refresh has finished
            swipeContainer.setRefreshing(false);

            scrollListener.resetState();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void fetchTimelineAsync(int page) {}

    public void activityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == COMPOSE_REQUEST && resultCode == RESULT_OK) {
            Tweet tweet = (Tweet) Parcels.unwrap(data.getParcelableExtra("tweet"));
            tweets.add(0, tweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
        } else if (requestCode == DETAIL_REQUEST && resultCode == RESULT_OK) {
            Log.i("TweetsListFragment", "Attempting to update spontaneously" + DETAIL_REQUEST);
            Tweet tweet = (Tweet) Parcels.unwrap(data.getParcelableExtra("tweet"));
            int position = -1;
            for (int i = 0; i < tweets.size(); i++) {
                if (tweets.get(i).uid == tweet.uid) {
                    position = i;
                    break;
                }
            }
            tweetAdapter.notifyItemChanged(position);
        }
    }
}
