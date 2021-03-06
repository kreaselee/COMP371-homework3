package com.example.homework3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class LocationFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private ArrayList<Location> locations;

    private String api_url;
    private static AsyncHttpClient client = new AsyncHttpClient();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_loc);
        locations = new ArrayList<>();

        api_url = getArguments().getString("url");

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
                    JSONObject json =  new JSONObject(new String(responseBody));
                    JSONArray results = json.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++){
                        JSONObject locationObject = results.getJSONObject(i);
                        Location location = new Location(locationObject.getString("name"),
                                locationObject.getString("type"),
                                locationObject.getString("dimension"));
                        // add to ArrayList
                        locations.add(location);
                    }

                    // create location adapter to pass in the data
                    LocationAdapter adapter = new LocationAdapter(locations);
                    // attach the adapter to the recycler view to populate
                    recyclerView.setAdapter(adapter);
                    // layoutManager
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                    // smooth scrolling
                    recyclerView.setHasFixedSize(true);
                    // add a line between each row
                    RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
                    recyclerView.addItemDecoration(itemDecoration);

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
