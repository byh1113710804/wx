package com.example.wechatmoments.view;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_SETTLING;
import static com.example.wechatmoments.data.Constants.LABEL;
import static com.example.wechatmoments.data.Constants.ON_LOAD_TWEETS_FAILED;
import static com.example.wechatmoments.data.Constants.ON_LOAD_USER_FAILED;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wechatmoments.data.entity.Tweet;
import com.example.wechatmoments.data.entity.User;
import com.example.wechatmoments.databinding.FragmentMainBinding;
import com.example.wechatmoments.presenter.Presenter;
import com.example.wechatmoments.presenter.PresenterInterface;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;

import java.util.List;

public class MainFragment extends Fragment implements ViewInterface {

    private RecyclerViewAdapter adapter;
    private FragmentMainBinding binding;
    private PresenterInterface presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.refreshLayout.setRefreshHeader(new ClassicsHeader(requireActivity()));
        binding.refreshLayout.setRefreshFooter(new ClassicsFooter(requireActivity()));
        binding.refreshLayout.setEnableLoadMore(true);
        binding.refreshLayout.setEnableRefresh(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerViewAdapter();
        binding.recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayout.HORIZONTAL));
        binding.recyclerView.setAdapter(adapter);
        presenter = new Presenter(this);
        presenter.loadData();
        setListener();
        return view;
    }


    @Override
    public void onTweetsLoaded(List<Tweet> tweets) {
        Log.i(LABEL, "loadTweetsData");
        adapter.setData(tweets);
    }

    @Override
    public void onUserLoaded(User user) {
        binding.userLayout.nickName.setText(user.getNick());
        Glide.with(getActivity()).load(user.getAvator()).into(binding.userLayout.avator);
    }

    public void setListener() {
        binding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            presenter.refreshTweets();
            refreshLayout.finishRefresh();
        });

        binding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            presenter.loadMoreTweets();
            refreshLayout.finishLoadMore();
        });

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                switch (newState){
                    case SCROLL_STATE_IDLE:
                        if (getContext() != null)
                            Glide.with(getContext()).resumeRequests();
                        break;
                    case SCROLL_STATE_SETTLING:
                        if (getContext() != null)
                            Glide.with(getContext()).pauseRequests();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            }
        });
    }

    @Override
    public void onTweetsLoadFailed() {
        Toast.makeText(getActivity(), ON_LOAD_TWEETS_FAILED, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUserLoadFailed() {
        Toast.makeText(getActivity(), ON_LOAD_USER_FAILED, Toast.LENGTH_LONG).show();
    }
}
