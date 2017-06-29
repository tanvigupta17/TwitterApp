package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by tanvigupta on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;

    Context context;
    Context mContext;

    private final int REQUEST_CODE = 10;

    // pass Tweets array into constructor
    public TweetAdapter(List<Tweet> tweets, Context context) {
        mTweets = tweets;
        mContext = context;
    }

    // inflate layout and cache references into ViewHolder for each row
    // only called when entirely new row is to be created
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);

        return viewHolder;
    }


    // bind values based on position of the element
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the data according to position
        final Tweet tweet = mTweets.get(position);

        // populate the views according to this data
        holder.tvUserName.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvHandle.setText(String.format("@" + tweet.user.screenName));
        holder.tvTimestamp.setText(getRelativeTimeAgo(tweet.createdAt));

        String s = tweet.user.profileImageUrl;
        s = s.replace("normal", "bigger");
        Glide
                .with(context)
                .load(s)
                .bitmapTransform(new RoundedCornersTransformation(context, 20, 0))
                .into(holder.ivProfileImage);

        if (tweet.mediaUrl != "") {
            holder.ivMediaImage.setVisibility(View.VISIBLE);
            Glide
                    .with(context)
                    .load(tweet.mediaUrl)
                    .bitmapTransform(new RoundedCornersTransformation(context, 50, 0))
                    .into(holder.ivMediaImage);
        } else {
            holder.ivMediaImage.setVisibility(View.GONE);
        }

        holder.btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ComposeActivity.class);
                intent.putExtra("reply", true);
                intent.putExtra("tweet", Parcels.wrap(tweet));
                ((TimelineActivity) mContext).startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString();

            relativeDate = relativeDate.replace(" ago", "");
            relativeDate = relativeDate.replace(" sec.", "s");
            relativeDate = relativeDate.replace(" min.", "m");
            relativeDate = relativeDate.replace(" hr.", "h");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }

    // create ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvHandle;
        public TextView tvTimestamp;
        public ImageView ivMediaImage;
        public ImageButton btnReply;

        // constructor
        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvHandle = (TextView) itemView.findViewById(R.id.tvHandle);
            tvTimestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);
            ivMediaImage = (ImageView) itemView.findViewById(R.id.ivMediaImage);
            btnReply = (ImageButton) itemView.findViewById(R.id.btnReply);
        }
    }
}
