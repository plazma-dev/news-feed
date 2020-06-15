package com.neda.newsfeed.view;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.neda.newsfeed.Injection;
import com.neda.newsfeed.R;
import com.neda.newsfeed.databinding.FragmentDialogBinding;
import com.neda.newsfeed.viewmodel.DialogViewModel;
import com.neda.newsfeed.viewmodel.ViewModelFactory;

import java.util.Objects;

public class PostDialogFragment extends DialogFragment {
    private final String TAG = "PostDialogFragment";
    private DialogViewModel viewModel;
    private FragmentDialogBinding binding;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDialogBinding.inflate(inflater, container, false);
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(requireContext());
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(DialogViewModel.class);

        if(getArguments() != null) {
            String userId = PostDialogFragmentArgs.fromBundle(getArguments()).getUserId();
            String postId = PostDialogFragmentArgs.fromBundle(getArguments()).getPostId();
            viewModel.setUserId(userId);
            observeViewModel();
            binding.btnDelete.setOnClickListener(b -> {
                viewModel.deletePost(postId);
                dismiss();
            });
        }
        return binding.getRoot();
    }

    private void observeViewModel() {
        viewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), user -> {
            //Update UI
            Log.d(TAG, user.toString());
            binding.name.setText(user.getName());
            binding.email.setText(user.getEmail());
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.name.setVisibility(View.VISIBLE);
            binding.email.setVisibility(View.VISIBLE);
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = Objects.requireNonNull(getDialog()).getWindow();
        if(window == null) return;
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        params.height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        window.setAttributes(params);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
