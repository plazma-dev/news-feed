package com.neda.newsfeed.view;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.neda.newsfeed.databinding.FragmentDialogBinding;
import com.neda.newsfeed.viewmodel.DialogViewModel;

public class PostDialogFragment extends DialogFragment {
    private final String TAG = "PostDialogFragment";
    DialogViewModel model;
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
        View view = binding.getRoot();
        model = new ViewModelProvider(this).get(DialogViewModel.class);
        if(getArguments() != null && getArguments().containsKey("userId")) {
            model.setUserId(getArguments().getString("userId"));
        }
        observeViewModel();
        return view;
    }

    private void observeViewModel() {
        model.getUserMutableLiveData().observe(getViewLifecycleOwner(), user -> {
            //Update UI
            Log.d(TAG, user.toString());
            binding.name.setText(user.getName());
            binding.email.setText(user.getEmail());
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.name.setVisibility(View.VISIBLE);
            binding.email.setVisibility(View.VISIBLE);
        });

        model.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
