package com.example.homework3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class CharacterFragment extends Fragment {

    private View view;
    private TextView textView_name;
    private ImageView imageView_char;
    private TextView textView_status;
    private TextView textView_species;
    private TextView textView_gender;
    private TextView textView_origin;
    private TextView textView_location;
    private TextView textView_episodes;

    private TextView textView_statusLabel;
    private TextView textView_speciesLabel;
    private TextView textView_genderLabel;
    private TextView textView_originLabel;
    private TextView textView_locationLabel;
    private TextView textView_episodesLabel;

    private String api_url;
    private static AsyncHttpClient client = new AsyncHttpClient();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_character, container, false);

        // find views by id
        textView_name = view.findViewById(R.id.textView_char_name);
        imageView_char = view.findViewById(R.id.imageView_char);
        textView_status = view.findViewById(R.id.textView_char_status);
        textView_species = view.findViewById(R.id.textView_char_species);
        textView_gender = view.findViewById(R.id.textView_char_gender);
        textView_origin = view.findViewById(R.id.textView_char_origin);
        textView_location = view.findViewById(R.id.textView_char_location);
        textView_episodes = view.findViewById(R.id.textView_char_episodes);

        textView_statusLabel = view.findViewById(R.id.textView_char_statusLabel);
        textView_speciesLabel = view.findViewById(R.id.textView_char_speciesLabel);
        textView_genderLabel = view.findViewById(R.id.textView_char_genderLabel);
        textView_originLabel = view.findViewById(R.id.textView_char_originLabel);
        textView_locationLabel = view.findViewById(R.id.textView_char_locationLabel);
        textView_episodesLabel = view.findViewById(R.id.textView_char_episodesLabel);

        api_url = getArguments().getString("url");

        // return view
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        client.addHeader("Accept", "application/json");
        // send a get request to the api url
        client.get(api_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("api response", new String(responseBody));

                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                    JSONObject info = json.getJSONObject("info");

                    // get random id and construct url
                    int count = info.getInt("count");
                    Random random = new Random();
                    int id = random.nextInt(count)+1;
                    String new_url = api_url + "/" + id;

                    // send a get request to the api url
                    client.get(new_url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Log.d("api response", new String(responseBody));

                            // set labels
                            // do this here to ensure that all information loads simultaneously
                            textView_statusLabel.setText("Status");
                            textView_speciesLabel.setText("Species");
                            textView_genderLabel.setText("Gender");
                            textView_originLabel.setText("Origin");
                            textView_locationLabel.setText("Location");
                            textView_episodesLabel.setText("Appears in Episode(s)");

                            try {
                                JSONObject json = new JSONObject(new String(responseBody));

                                // set views accordingly
                                textView_name.setText(json.getString("name"));
                                Picasso.get().load(json.getString("image")).into(imageView_char);
                                textView_status.setText(json.getString("status"));
                                textView_species.setText(json.getString("species"));
                                textView_gender.setText(json.getString("gender"));
                                JSONObject origin = json.getJSONObject("origin");
                                textView_origin.setText(origin.getString("name"));
                                JSONObject location = json.getJSONObject("location");
                                textView_location.setText(location.getString("name"));
                                JSONArray episode = json.getJSONArray("episode");
                                ArrayList<String> episodes = new ArrayList<>();
                                for (int i = 0; i < episode.length(); i++) {
                                    String ep = episode.getString(i).replace("https://rickandmortyapi.com/api/episode/", "");
                                    episodes.add(ep);
                                }
                                String eps = episodes.toString();
                                textView_episodes.setText(eps.substring(1, eps.length()-1));

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
}

