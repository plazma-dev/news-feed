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

import com.neda.newsfeed.Injection;
import com.neda.newsfeed.R;
import com.neda.newsfeed.databinding.FragmentHomeBinding;
import com.neda.newsfeed.model.Post;
import com.neda.newsfeed.viewmodel.HomeViewModel;
import com.neda.newsfeed.viewmodel.ViewModelFactory;

public class HomeFragment extends Fragment implements PostClickListener {
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    PostsRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        prepareRecyclerView();

        //We use Activity as owner so MainActivity can get the same instance of view model when requested
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(requireContext());
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(HomeViewModel.class);
        observeViewModel();

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
        viewModel.getPostListMutableLiveData().observe(getViewLifecycleOwner(), posts -> {
            // update UI
            adapter.setPosts(posts);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onPostClicked(Post post) {
        String userId = post.getUserId();
        HomeFragmentDirections.ActionHomeFragmentToPostDialogFragment action = HomeFragmentDirections.actionHomeFragmentToPostDialogFragment(userId, post.getId());
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
