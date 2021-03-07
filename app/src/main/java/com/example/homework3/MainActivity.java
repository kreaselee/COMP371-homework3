package com.example.homework3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;

    private static final String api_url="https://rickandmortyapi.com/api";
    private static AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set tabLayout
        tabLayout = findViewById(R.id.tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                // add header to client
                client.addHeader("Accept", "application/json");
                // send a get request to the api url
                client.get(api_url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d("api response", new String(responseBody));

                        try {
                            JSONObject json = new JSONObject(new String(responseBody));
                            Bundle bundle = new Bundle();

                            // load fragment based on tab position
                            // first tab - characters
                            // second tab - episodes
                            // third tab - locations
                            if (position == 0) {
                                String url = json.getString("characters");
                                // Bundle bundle = new Bundle();
                                bundle.putString("url", url);
                                CharacterFragment characterFragment = new CharacterFragment();
                                characterFragment.setArguments(bundle);
                                loadFragment(characterFragment);
                            }
                            else if (position == 1) {
                                String url = json.getString("episodes");
                                // Bundle bundle = new Bundle();
                                bundle.putString("url", url);
                                EpisodeFragment episodeFragment = new EpisodeFragment();
                                episodeFragment.setArguments(bundle);
                                loadFragment(episodeFragment);
                            }
                            else if (position == 2) {
                                String url = json.getString("locations");
                                System.out.println(url);
                                bundle.putString("url", url);
                                LocationFragment locationFragment = new LocationFragment();
                                locationFragment.setArguments(bundle);
                                loadFragment(locationFragment);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.e("api error", new String(responseBody));
                    }
                });

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                // add header to client
                client.addHeader("Accept", "application/json");
                // send a get request to the api url
                client.get(api_url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d("api response", new String(responseBody));

                        try {
                            JSONObject json = new JSONObject(new String(responseBody));
                            Bundle bundle = new Bundle();

                            // load fragment based on tab position
                            // first tab - characters
                            // second tab - episodes
                            // third tab - locations
                            if (position == 0) {
                                String url = json.getString("characters");
                                // Bundle bundle = new Bundle();
                                bundle.putString("url", url);
                                CharacterFragment characterFragment = new CharacterFragment();
                                characterFragment.setArguments(bundle);
                                loadFragment(characterFragment);
                            }
                            else if (position == 1) {
                                String url = json.getString("episodes");
                                // Bundle bundle = new Bundle();
                                bundle.putString("url", url);
                                EpisodeFragment episodeFragment = new EpisodeFragment();
                                episodeFragment.setArguments(bundle);
                                loadFragment(episodeFragment);
                            }
                            else if (position == 2) {
                                String url = json.getString("locations");
                                System.out.println(url);
                                bundle.putString("url", url);
                                LocationFragment locationFragment = new LocationFragment();
                                locationFragment.setArguments(bundle);
                                loadFragment(locationFragment);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.e("api error", new String(responseBody));
                    }
                });
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