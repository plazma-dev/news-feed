package com.neda.newsfeed.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.neda.newsfeed.Injection;
import com.neda.newsfeed.R;
import com.neda.newsfeed.databinding.ActivityMainBinding;
import com.neda.newsfeed.viewmodel.HomeViewModel;
import com.neda.newsfeed.viewmodel.ViewModelFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            //Get the same HomeViewModel instance, scoped to this activity and already used by HomeFragment
            ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
            HomeViewModel homeViewModel = new ViewModelProvider(this, viewModelFactory).get(HomeViewModel.class);
            //Refresh list
            homeViewModel.getPostsFromApi();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}