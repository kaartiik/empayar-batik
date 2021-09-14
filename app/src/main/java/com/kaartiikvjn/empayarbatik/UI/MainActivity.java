package com.kaartiikvjn.empayarbatik.UI;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.navigation.NavigationView;
import com.kaartiikvjn.empayarbatik.R;
import com.kaartiikvjn.empayarbatik.databinding.ActivityMainBinding;
import com.kaartiikvjn.empayarbatik.utils.BaseActivity;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.contentMain.mainToolbar.getRoot());
        toolbarSetter(getSupportActionBar());
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                binding.contentMain.mainToolbar.getRoot(),
                R.string.nav_drawer_open,
                R.string.nav_drawer_close
        );
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navigationView.setNavigationItemSelectedListener(this);
    }

    private void toolbarSetter(ActionBar toolbar) {
        toolbar.setTitle("Main activity");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main_activity, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.navDrawer_newArrivals) {
            toast("New Arrivals clicked");
            return true;
        } else if (id == R.id.navDrawer_collections) {
            toast("Collections clicked");
            return true;
        } else if (id == R.id.navDrawer_top) {
            toast("Top clicked");
            return true;
        } else if (id == R.id.navDrawer_pants) {
            toast("Pants clicked");
            return true;
        } else if (id == R.id.navDrawer_dress) {
            toast("Dress clicked");
            return true;
        } else
            return false;
    }
}