package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailActivity extends AppCompatActivity {

    public ImageView ivProfileImage;
    public TextView tvUsername;
    public TextView tvHandle;
    public TextView tvBody;
    public ImageView ivMediaImage;

    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvHandle = (TextView) findViewById(R.id.tvHandle);
        tvBody = (TextView) findViewById(R.id.tvBody);
        ivMediaImage = (ImageView) findViewById(R.id.ivMediaImage);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        tvUsername.setText(tweet.user.name);
        tvBody.setText(tweet.body);
        tvHandle.setText(String.format("@" + tweet.user.screenName));

        String s = tweet.user.profileImageUrl;
        s = s.replace("normal", "bigger");
        Glide
                .with(this)
                .load(s)
                .bitmapTransform(new RoundedCornersTransformation(this, 20, 0))
                .into(ivProfileImage);

        if (tweet.mediaUrl != "") {
            ivMediaImage.setVisibility(View.VISIBLE);
            Glide
                    .with(this)
                    .load(tweet.mediaUrl)
                    .into(ivMediaImage);
        } else {
            ivMediaImage.setVisibility(View.GONE);
        }
    }

    public String getDateTime(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.formatDateTime(this, dateMillis, DateUtils.FORMAT_NUMERIC_DATE);

            relativeDate = relativeDate.replace(" ago", "");
            relativeDate = relativeDate.replace(" sec.", "s");
            relativeDate = relativeDate.replace(" min.", "m");
            relativeDate = relativeDate.replace(" hr.", "h");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
