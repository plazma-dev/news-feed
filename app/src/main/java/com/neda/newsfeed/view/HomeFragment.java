package com.neda.newsfeed.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.neda.newsfeed.alarm.Alarm;
import com.neda.newsfeed.Injection;
import com.neda.newsfeed.R;
import com.neda.newsfeed.databinding.FragmentHomeBinding;
import com.neda.newsfeed.model.Post;
import com.neda.newsfeed.viewmodel.HomeViewModel;
import com.neda.newsfeed.viewmodel.ViewModelFactory;

public class HomeFragment extends Fragment implements PostClickListener {
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private PostsRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        prepareRecyclerView();

        //Use Activity as owner so MainActivity can get the same instance of view model when requested
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(requireContext());
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(HomeViewModel.class);
        viewModel.setAlarmInstance(Alarm.getInstance(requireContext()));
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
            binding.progressBar.setVisibility(View.GONE);
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if(loading)
                binding.progressBar.setVisibility(View.VISIBLE);
        });

        viewModel.getPostsFetchErrorMessage().observe(getViewLifecycleOwner(), message -> {
            binding.progressBar.setVisibility(View.GONE);
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
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
