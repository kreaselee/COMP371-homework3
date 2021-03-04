package com.example.homework3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class EpisodeFragment extends Fragment {

    private View view;

    private TextView textView_number;
    private TextView textView_name;
    private TextView textView_date;
    private RecyclerView recyclerView_characters;

    private TextView textView_dateLabel;
    private TextView textView_charactersLabel;

    private Button button_moreInfo;

    private String api_url;
    private static AsyncHttpClient client = new AsyncHttpClient();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_episode, container, false);

        textView_number = view.findViewById(R.id.textView_ep_number);
        textView_name = view.findViewById(R.id.textView_ep_name);
        textView_date = view.findViewById(R.id.textView_ep_date);
        recyclerView_characters = view.findViewById(R.id.recyclerView_ep_characters);

        textView_dateLabel = view.findViewById(R.id.textView_ep_dateLabel);
        textView_charactersLabel = view.findViewById(R.id.textView_ep_charactersLabel);

        api_url = getArguments().getString("url");

        client.addHeader("Accept", "application/json");
        // send a get request to the api url
        client.get(api_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("api response", new String(responseBody));

                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                    JSONObject info = json.getJSONObject("info");

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
                            // did this here to ensure that all information loads simultaneously
                            textView_dateLabel.setText("Aired On");
                            textView_charactersLabel.setText("Character Appearances");

                            /*
                            LinearLayout linearLayout = view.findViewById(R.id.button_layout);
                            Button button = new Button(getActivity());
                            button.setText("More Information");
                            linearLayout.addView(button);

                             */

                            button_moreInfo = view.findViewById(R.id.button_ep_moreInfo);

                            try {
                                JSONObject json = new JSONObject(new String(responseBody));

                                textView_number.setText(json.getString("episode"));
                                textView_name.setText(json.getString("name"));
                                textView_date.setText(json.getString("air_date"));
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

        return view;
    }
}
