package com.example.homework3;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class EpisodeFragment extends Fragment {

    private View view;

    private TextView textView_number;
    private TextView textView_name;
    private TextView textView_date;
    private TextView textView_dateLabel;
    private TextView textView_charactersLabel;
    private RecyclerView recyclerView_characters;

    private ArrayList<Character> characters;

    private Button button_moreInfo;

    private String api_url;
    private static AsyncHttpClient client = new AsyncHttpClient();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_episode, container, false);

        // find views by id
        textView_number = view.findViewById(R.id.textView_ep_number);
        textView_name = view.findViewById(R.id.textView_ep_name);
        textView_date = view.findViewById(R.id.textView_ep_date);
        textView_dateLabel = view.findViewById(R.id.textView_ep_dateLabel);
        textView_charactersLabel = view.findViewById(R.id.textView_ep_charactersLabel);

        button_moreInfo = view.findViewById(R.id.button_ep_moreInfo);

        recyclerView_characters = view.findViewById(R.id.recyclerView_ep_characters);
        characters = new ArrayList<>();

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
                            textView_dateLabel.setText("Aired On");
                            textView_charactersLabel.setText("Character Appearances");
                            button_moreInfo.setVisibility(View.VISIBLE);

                            try {
                                JSONObject json = new JSONObject(new String(responseBody));
                                JSONArray charArray = json.getJSONArray("characters");

                                String number = json.getString("episode");
                                String name = json.getString("name");

                                // set views accordingly
                                textView_number.setText(number);
                                textView_name.setText(name);
                                textView_date.setText(json.getString("air_date"));

                                button_moreInfo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        sendNotification(number, name);
                                    }
                                });

                                // add characters to arrayList
                                for (int i = 0; i < charArray.length(); i++) {
                                    getCharInfo(charArray.get(i).toString(), characters,
                                            recyclerView_characters, i, charArray.length()-1);
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

    // method to send notification
    public void sendNotification(String number, String name) {
        int NOTIFICATION_ID = 14;
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID="";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CHANNEL_ID = "my_channel_01";
            CharSequence channelName = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(Description);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            channel.setShowBadge(false);
            notificationManager.createNotificationChannel(channel);
        }

        String infoUrl = "https://rickandmorty.fandom.com/wiki/" + name.replace(" ", "_");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(number + ": " + name)
                .setContentText("To read more information about Episode " + number + ", " +
                        "please visit: " + infoUrl)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("To read more information about Episode " + number + ", " +
                                "please visit: " + infoUrl));

        Intent notifyIntent  = new Intent(Intent.ACTION_VIEW);
        notifyIntent.setData(Uri.parse(infoUrl));
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                getContext(), 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        builder.setContentIntent(notifyPendingIntent);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    // to set recyclerView with starring characters
    public void getCharInfo(String url, ArrayList<Character> list, RecyclerView recyclerView, int position, int last) {
        // get character info based on id
        client.addHeader("Accept", "application/json");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("api response", new String(responseBody));

                try {
                    JSONObject json = new JSONObject(new String(responseBody));

                    Character character = new Character(json.getString("image"), json.getString("name"));
                    // add to ArrayLists
                    list.add(character);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                finally {
                    if (position == last) {
                        // create character adapter to pass in the data
                        CharacterAdapter adapter = new CharacterAdapter(list);
                        // attach the adapter to the recycler view to populate
                        recyclerView.setAdapter(adapter);
                        // layoutManager
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

                        // smooth scrolling
                        recyclerView.setHasFixedSize(true);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("api error", new String(responseBody));
            }
        });
    }
}
