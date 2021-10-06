package com.kaartiikvjn.empayarbatik.UI;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.kaartiikvjn.empayarbatik.R;
import com.kaartiikvjn.empayarbatik.databinding.ActivityMainBinding;
import com.kaartiikvjn.empayarbatik.utils.BaseActivity;
import com.kaartiikvjn.empayarbatik.utils.Constants;

import java.util.Objects;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.contentMain.mainToolbar.getRoot());
        toolbarSetter(Objects.requireNonNull(getSupportActionBar()));
        reAddFragment(new CategoryFragment(), Constants.newArrivals);
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
        toolbar.setTitle("New Arrivals");
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
            reAddFragment(new CategoryFragment(), Constants.newArrivals);
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            Objects.requireNonNull(getSupportActionBar()).setTitle(item.getTitle());
            return true;
        } else if (id == R.id.navDrawer_collections) {
            reAddFragment(new CollectionsFragment(), "");
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            Objects.requireNonNull(getSupportActionBar()).setTitle(item.getTitle());
            return true;
        } else if (id == R.id.navDrawer_top) {
            reAddFragment(new CategoryFragment(), Constants.top);
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            Objects.requireNonNull(getSupportActionBar()).setTitle(item.getTitle());
            return true;
        } else if (id == R.id.navDrawer_pants) {
            reAddFragment(new CategoryFragment(), Constants.pants);
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            Objects.requireNonNull(getSupportActionBar()).setTitle(item.getTitle());
            return true;
        } else if (id == R.id.navDrawer_dress) {
            reAddFragment(new CategoryFragment(), Constants.dress);
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            Objects.requireNonNull(getSupportActionBar()).setTitle(item.getTitle());
            return true;
        } else if (id == R.id.navDrawer_shoppingCart) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(MainActivity.this, ShoppingCartActivity.class));
            return true;
        }  else if (id == R.id.navDrawer_logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Logout!")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        getAuth().signOut();
                        dialog.dismiss();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).show();
            return true;
        } else
            return false;
    }

    private void reAddFragment(Fragment fragment, String category) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.itemCategory, category);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
}