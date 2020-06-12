package com.neda.newsfeed.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.neda.newsfeed.R;
import com.neda.newsfeed.databinding.FragmentHomeBinding;
import com.neda.newsfeed.model.Post;
import com.neda.newsfeed.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment implements PostClickListener {
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    PostsRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        prepareRecyclerView();

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        observeViewModel();
        //viewModel.loadPosts();

        return binding.getRoot();
    }

    private void prepareRecyclerView() {
        binding.postsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostsRecyclerAdapter(this);
        binding.postsRV.setAdapter(adapter);
    }

    /**
     * Start observing viewModel's live data
     */
    private void observeViewModel() {
        viewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), posts -> {
            // update UI
            adapter.setPosts(posts);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onPostClicked(Post post) {
        String userId = post.getUserId();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_homeFragment_to_post_dialog_fragment, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
