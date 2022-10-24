/**
 * EAD - FuelMe Mobile App
 *
 * @author H.G. Malwatta - IT19240848
 * @version 1.0.0
 */

package com.example.fuelme.ui.mainscreen.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fuelme.R;
import com.example.fuelme.commonconstants.CommonConstants;
import com.example.fuelme.models.FuelStation;
import com.example.fuelme.ui.feedback.FeedbackList;
import com.example.fuelme.ui.feedback.ViewFeedback;
import com.example.fuelme.ui.mainscreen.MainActivity;
import com.example.fuelme.ui.mainscreen.adapters.FavouriteRecycleViewAdapter;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouriteStationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteStationsFragment extends Fragment {

    private final OkHttpClient client = new OkHttpClient(); //okhttp client instance
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    ArrayList<FuelStation> fuelStations = new ArrayList<>();
    ArrayList<FuelStation> fuelStationArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    FavouriteRecycleViewAdapter adapter;
    SharedPreferences preferences;

    private String username;
    SwipeRefreshLayout swipeRefreshLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavouriteStationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouriteStationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouriteStationsFragment newInstance(String param1, String param2) {
        FavouriteStationsFragment fragment = new FavouriteStationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //get username from shared preferences
        preferences = getActivity().getSharedPreferences("login_data", MODE_PRIVATE);
        username = preferences.getString("user_username", "");

        //get favourite stations from database
        fetchFavouriteStations(username);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite_stations, container, false);


        // Assign recycler view
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView_favourite);

        adapter = new FavouriteRecycleViewAdapter(getActivity(), fuelStationArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //swipe to refresh layout
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_favourite);

        /**
         * H.G. Malwatta - IT19240848
         *
         * Swipe to refresh
         */
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fuelStations.clear();
                fetchFavouriteStations(username);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        /**
         * H.G. Malwatta - IT19240848
         *
         * This method is used to delete a favourite station from the favourite list by swiping right to left
         */
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            /**
             * This method is used to set the swipe direction
             * @param recyclerView
             * @param viewHolder
             * @return int
             */
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(
                        ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT
                );
            }

            /**
             * This method is used to move the item in the recycler view
             * @param recyclerView
             * @param viewHolder
             * @param target
             * @return true if the item is moved
             */
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            /**
             * This method is used to delete the item from the recycler view
             * @param viewHolder
             * @param direction
             * @return view
             */
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                //get the position of the item
                int position = viewHolder.getAdapterPosition();
                FuelStation fuelStation = fuelStations.get(position);

                //check the direction of the swipe
                if (direction == ItemTouchHelper.LEFT) {

                    //create a alert dialog to confirm the delete
                    AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
                    alert.setCancelable(false);
                    alert.setTitle("Are you sure?");
                    alert.setMessage("Do you want to delete this station from your favourite list?");

                    //set the positive button and its action to delete the item
                    alert.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fuelStations.remove(position);
                            adapter.notifyItemRemoved(position);
                            deleteFavourite(fuelStation.getId());
                        }
                    });

                    //set the negative button and its action to cancel the delete
                    alert.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fuelStations.add(position, fuelStation);
                            adapter.notifyItemInserted(position);
                            dialog.dismiss();
                        }
                    });
                    alert.show();
                }
            }
        });

        //attach the helper to the recycler view
        helper.attachToRecyclerView(recyclerView);

        return view;
    }

    /**
     * H.G. Malwatta - IT19240848
     *
     * This method is used to fetch the favourite stations of the current login user
     *
     * @param username
     */
    public void fetchFavouriteStations(String username) {

        //set the username to the URI
        String BASE_URL = CommonConstants.REMOTE_URL_GET_FAVORITE_STATIONS + username;

        //build the url using Url builder
        HttpUrl url = HttpUrl.parse(BASE_URL).newBuilder().build();

        //build the request
        Request request = new Request.Builder()
                .url(url)
                .build();

        //send the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseBody = response.body().string();
                    Log.d("API_CALL_FAV", "onResponse success : " + responseBody);
                    try {
                        JSONArray jsonArray = new JSONArray(responseBody); //get the json array

                        //iterate through the JSON array
                        for (int i = 0; i < jsonArray.length(); i++) {

                            //get the json object by index
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            //instantiate fuel station object
                            FuelStation fuelStation = new FuelStation();

                            //assign attributes to the fuel station object
                            fuelStation.setId(jsonObject.getString("id"));
                            fuelStation.setLicense(jsonObject.getString("license"));
                            fuelStation.setOwnerUsername(jsonObject.getString("ownerUsername"));
                            fuelStation.setStationName(jsonObject.getString("stationName"));
                            fuelStation.setStationAddress(jsonObject.getString("stationAddress"));
                            fuelStation.setStationPhoneNumber(jsonObject.getString("stationPhoneNumber"));
                            fuelStation.setStationEmail(jsonObject.getString("stationEmail"));
                            fuelStation.setStationWebsite(jsonObject.getString("stationWebsite"));
                            fuelStation.setOpenStatus(jsonObject.getString("openStatus"));
                            fuelStation.setPetrolQueueLength(jsonObject.getInt("petrolQueueLength"));
                            fuelStation.setDieselQueueLength(jsonObject.getInt("dieselQueueLength"));
                            fuelStation.setPetrolStatus(jsonObject.getString("petrolStatus"));
                            fuelStation.setDieselStatus(jsonObject.getString("dieselStatus"));
                            fuelStation.setLocationLatitude(jsonObject.getInt("locationLatitude"));
                            fuelStation.setLocationLongitude(jsonObject.getInt("locationLongitude"));

                            //add the fuel station to fuel stations list
                            fuelStations.add(fuelStation);
                        }


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //parse the response
                                adapter = new FavouriteRecycleViewAdapter(getActivity(), fuelStations);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    //handle unsuccessful response
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();
                    Log.d("API_CALL_FAV", "onResponse failure : " + body);
                }
            }
        });
    }

    /**
     * H.G. Malwatta - IT19240848
     *
     * This method is used to delete the favourite station
     *
     * @param id
     */
    public void deleteFavourite(String id) {

        //set the id to the URI
        String BASE_URL = CommonConstants.REMOTE_URL_DELETE_FAVORITE_STATIONS + id;

        //build the url using Url builder
        HttpUrl url = HttpUrl.parse(BASE_URL).newBuilder().build();

        //build the request
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        //send the request
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                e.printStackTrace();
                Log.d("API_CALL_DELETE", "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //check the response status
                if (response.isSuccessful()) {

                    //get the response body
                    final String myResponse = response.body().string();
                    Log.d("API_CALL_DELETE", "onSuccess: " + myResponse);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //parse the response
                            adapter = new FavouriteRecycleViewAdapter(getActivity(), fuelStations);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }
                    });

                }
            }
        });
    }
}