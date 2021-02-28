package com.example.homework3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.loopj.android.http.AsyncHttpClient;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    // private ViewPager2 viewPager;

    private static final String api_url="https://rickandmortyapi.com/api";
    private static AsyncHttpClient client = new AsyncHttpClient();

    // TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                if (position == 0) {
                    CharacterFragment characterFragment = new CharacterFragment();
                    loadFragment(characterFragment);
                }
                else if (position == 1) {
                    EpisodeFragment episodeFragment = new EpisodeFragment();
                    loadFragment(episodeFragment);
                }
                else if (position == 2) {
                    LocationFragment locationFragment = new LocationFragment();
                    loadFragment(locationFragment);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }

}