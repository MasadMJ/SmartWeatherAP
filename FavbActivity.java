package com.example.smartweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class FavbActivity extends AppCompatActivity {
    Button buttonClear;
    static TextView texttranslate;
    Database dbHelper = new Database(FavbActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favb);
        texttranslate=findViewById(R.id.texttranslate);
        buttonClear=findViewById(R.id.button);
        //callMyDb("info","fav");
        // texttranslate.setText("Loading");
        dbHelper.displayDataFav();
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteData("fav");
                texttranslate.setText("");
                Toast.makeText(FavbActivity.this,"Favorite Deleted",Toast.LENGTH_SHORT).show();



            }
        });



    }

    private void callMyDb(String cmd,String type ) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://cddhost.com/weather/apisql.php?type="+type+"&cmd="+cmd;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                texttranslate.setText( response);
                if (!response.isEmpty()){
                    //  playAudio(cityname);
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

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    JSONObject jsonBody = new JSONObject();
                    if (cmd=="add"){
                        jsonBody.put("type", type);
                        jsonBody.put("cmd", cmd);
                    }
                    else{
                        jsonBody.put("type", type);
                        jsonBody.put("cmd", cmd);

                    }
                    String requestBody = jsonBody.toString();
                    return requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException | JSONException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("id", "oneapp.app.com");
                params.put("key", "fgs7902nskagdjs");

                return params;
            }


        };
        Log.d("string", stringRequest.toString());
        requestQueue.add(stringRequest);




    }

    public static void setkkk(String k){
        texttranslate.append(k);

    }



}