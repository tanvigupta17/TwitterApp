package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.User;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by tanvigupta on 7/6/17.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> mUsers;

    Context context;
    static Context mContext;

    TwitterClient client;

    // pass Users array into constructor
    public UserAdapter(List<User> users, Context context) {
        mUsers = users;
        mContext = context;
    }

    // inflate layout and cache references into ViewHolder for each row
    // only called when entirely new row is to be created
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View userView = inflater.inflate(R.layout.item_user, parent, false);
        ViewHolder viewHolder = new ViewHolder(userView);

        return viewHolder;
    }

    // bind values based on position of the element
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the data according to position
        final User user = mUsers.get(position);

        client = TwitterApp.getRestClient();

        // populate the views according to this data
        holder.tvUserName.setText(user.name);
        holder.tvHandle.setText(String.format("@" + user.screenName));
        holder.tvTagline.setText(user.tagline);

        Glide
                .with(context)
                .load(user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 20, 0))
                .into(holder.ivProfileImage);

        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("screen_name", user.screenName);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        mUsers.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<User> list) {
        mUsers.addAll(list);
        notifyDataSetChanged();
    }

    public static Context getContext() {
        return mContext;
    }

    // create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvHandle;
        public TextView tvTagline;

        // constructor
        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvHandle = (TextView) itemView.findViewById(R.id.tvHandle);
            tvTagline = (TextView) itemView.findViewById(R.id.tvTagline);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION) {
                User user = mUsers.get(position);
                Intent in = new Intent(context, ProfileActivity.class);
                in.putExtra("screen_name", user.screenName);
                ((AppCompatActivity) mContext).startActivity(in);
            }
        }
    }
}
