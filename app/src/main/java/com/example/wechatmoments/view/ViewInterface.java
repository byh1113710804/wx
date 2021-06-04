package com.example.wechatmoments.view;

import android.widget.ListAdapter;

import com.example.wechatmoments.data.entity.Tweet;
import com.example.wechatmoments.data.entity.User;

import java.util.List;

public interface ViewInterface {

    void onTweetsLoaded(List<Tweet> tweets);

    void onUserLoaded(User user);

    void onTweetsLoadFailed();

    void onUserLoadFailed();
}
