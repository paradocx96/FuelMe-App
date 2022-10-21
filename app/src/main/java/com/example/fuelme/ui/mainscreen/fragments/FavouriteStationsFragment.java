package com.example.fuelme.ui.mainscreen.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fuelme.R;
import com.example.fuelme.commonconstants.CommonConstants;
import com.example.fuelme.models.FuelStation;
import com.example.fuelme.ui.mainscreen.adapters.FavouriteRecycleViewAdapter;

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

        //Get current login user details
//        preferences = getSharedPreferences("login_data", MODE_PRIVATE);
//
//        username = preferences.getString("user_username", "");

        fetchFavouriteStations("navinda");
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

        return view;
    }

    //Fetch all the favourite stations from remote async
    public void fetchFavouriteStations(String username) {
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
                            //JSONObject jsonObject = new JSONObject(body);
                            JSONObject jsonObject = jsonArray.getJSONObject(i); //get the json object by index

                            FuelStation fuelStation = new FuelStation();//instantiate fuel station object

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
}