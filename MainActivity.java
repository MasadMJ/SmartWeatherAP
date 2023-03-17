package com.example.smartweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    final String APP_ID = "dab3af44de7d24ae7ff86549334e45bd";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static String cccM ;
    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;


    String Location_Provider = LocationManager.GPS_PROVIDER;
    TextView NameofCity, weatherState, Temperature;
    ImageView mweatherIcon;
    ImageView gpsLOC;
    Button buttonFAV;
    Button buttonFAVShow;
    Button buttonMapShow;
    MediaPlayer mediaPlayer;
    Database dbHelper = new Database(MainActivity.this);

    RelativeLayout mCityFinder;
    Button mhistoryBT;


    LocationManager mLocationManager;
    LocationListener mLocationListner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherState = findViewById(R.id.weatherCondition);
        Temperature = findViewById(R.id.temperature);
        mweatherIcon = findViewById(R.id.weatherIcon);
        mCityFinder = findViewById(R.id.cityFinder);
        mhistoryBT = findViewById(R.id.historyBT);

        NameofCity = findViewById(R.id.cityName);
        buttonFAV=findViewById(R.id.addFav);
        buttonFAVShow=findViewById(R.id.favoriteBtn);
        buttonMapShow=findViewById(R.id.showmapBtn);
        gpsLOC=findViewById(R.id.gpsLOC);


        mCityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, cityFinder.class);
                startActivity(intent);
            }
        });


        buttonMapShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, mapshow.class);
                startActivity(intent);
            }
        });



        mhistoryBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, historylog.class);
                startActivity(intent);
            }
        });


        buttonFAVShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavbActivity.class);
                startActivity(intent);
            }
        });



        gpsLOC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherForCurrentLocation();
                buttonFAV.setVisibility(View.INVISIBLE);
                buttonMapShow.setVisibility(View.INVISIBLE);
                playAudio("Weather of your Location");
                Toast.makeText(MainActivity.this,"Weather of your Location",Toast.LENGTH_SHORT).show();

            }
        });


        Intent mIntent=getIntent();
        String city= mIntent.getStringExtra("City");
        if (city==null){
            buttonFAV.setVisibility(View.INVISIBLE);
            buttonMapShow.setVisibility(View.INVISIBLE);
        }
        else{
            buttonFAV.setVisibility(View.VISIBLE);
            buttonMapShow.setVisibility(View.VISIBLE);
            playAudio(city);

        }
        buttonFAV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // callMyDb(city,"add","fav");
                dbHelper.insertData(city,"fav");
                Toast.makeText(MainActivity.this,"Added to Favorites",Toast.LENGTH_SHORT).show();



            }
        });


    }



   public static String getMYcity() {
        return cccM;
    }


 /*   @Override
   protected void onResume() {
       super.onResume();
       getWeatherForCurrentLocation();
    }*/

    @Override

    protected void onResume() {
        super.onResume();
        Intent mIntent=getIntent();
        String city= mIntent.getStringExtra("City");
        if(city!=null)
        {
            getWeatherForNewCity(city);
        }
        else
        {
            getWeatherForCurrentLocation();
        }


    }


    private void getWeatherForNewCity(String city)
    {
        RequestParams params=new RequestParams();
        params.put("q",city);
        params.put("appid",APP_ID);
        getWeatherUP(params);
        //callMyDb(city,"add","history");
        dbHelper.insertData(city,"log");
        cccM=city;

    }




    private void getWeatherForCurrentLocation() {

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListner = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                RequestParams params =new RequestParams();
                params.put("lat" ,Latitude);
                params.put("lon",Longitude);
                params.put("appid",APP_ID);
                getWeatherUP(params);




            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                //not able to get location
            }
        };


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
       mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, mLocationListner);

    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode==REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(MainActivity.this,"Locationget Succesffully",Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            }

        }


    }



    private  void getWeatherUP(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL,params,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

              //  Toast.makeText(MainActivity.this,"Data Get Success",Toast.LENGTH_SHORT).show();

                weatherData weatherD=weatherData.fromJson(response);
                updateUI(weatherD);


                // super.onSuccess(statusCode, headers, response);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });



    }

    private  void updateUI(weatherData weather){


        Temperature.setText(weather.getmTemperature());
        NameofCity.setText(weather.getMcity());
        weatherState.setText(weather.getmWeatherType());
        int resourceID=getResources().getIdentifier(weather.getMicon(),"drawable",getPackageName());
        mweatherIcon.setImageResource(resourceID);


    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationManager!=null)
        {
          //  mLocationManager.removeUpdates(mLocationListner);
        }
    }





    private void playAudio(String text) {
        String b= Base64.encodeToString(text.getBytes(), Base64.DEFAULT);
        String audioUrl = ("https://voice.reverso.net/RestPronunciation.svc/v1/output=json/GetVoiceStream/voiceName=Heather22k?voiceSpeed=100&inputText="+b);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
       // Toast.makeText(this, "Audio started playing..", Toast.LENGTH_SHORT).show();
    }




    private void callMyDb(String cityname, String cmd,String type ) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://cddhost.com/weather/apisql.php?type="+type+"&country="+cityname+"&cmd="+cmd;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // texttranslate.setText( response);
                if (!response.isEmpty()){
                    // playAudio(cityname);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //      texttranslate.setText( "Wait 1 Min and CLICK Get Text");
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }


        };
        Log.d("string", stringRequest.toString());
        requestQueue.add(stringRequest);




    }








}