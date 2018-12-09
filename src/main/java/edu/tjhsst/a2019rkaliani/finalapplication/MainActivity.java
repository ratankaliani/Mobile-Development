package edu.tjhsst.a2019rkaliani.finalapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;


public class MainActivity extends AppCompatActivity {


    ArrayList<Earthquake> finalQuakes = new ArrayList<>();
    TextView earthquakeDisplay;

    class earthquakeArrayAdapter extends ArrayAdapter<Earthquake> {

        private Context context;
        private List<Earthquake> listEarthquake;

        //constructor, call on creation
        public earthquakeArrayAdapter(Context context, int resource, ArrayList<Earthquake> objects) {
            super(context, resource, objects);

            this.context = context;
            this.listEarthquake = objects;
        }

        //called when rendering the list

        public View getView(int position, View convertView, ViewGroup parent) {

            //get the property we are displaying
            Earthquake earthquake = listEarthquake.get(position);

            //get the inflater and inflate the XML layout for each item
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.earthquake_layout, null);

            TextView description = (TextView) view.findViewById(R.id.description);
            TextView magnitude = (TextView) view.findViewById(R.id.magnitude);
            TextView location = (TextView) view.findViewById(R.id.location);
            TextView place = (TextView) view.findViewById(R.id.place);
            TextView time = (TextView) view.findViewById(R.id.time);
            double userLocationLatitude = 38.818662;
            double userLocationLongitude = -77.168763;


            //set address and description


            //display trimmed excerpt for description


            //set price and rental attributes
            double earthquakeMag = earthquake.getMyMag();
            description.setText(earthquake.getMyTitle());
            magnitude.setText(earthquakeMag+"");
            place.setText(earthquake.getMyPlace());
            String longitude = earthquake.getMyLon().toString();
            String latitude = earthquake.getMyLat().toString();
            double dLat = Math.toRadians(earthquake.getMyLat() - userLocationLatitude);
            double dLon = Math.toRadians(earthquake.getMyLon() - userLocationLongitude);
            Double lat1 = Math.toRadians(userLocationLatitude);
            Double lat2 = Math.toRadians(earthquake.getMyLat());
            final double R = 6372.8;
            double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
            double c = 2 * Math.asin(Math.sqrt(a));
            final Double distanceBetween =  R * c;
            final String distanceStr = distanceBetween.toString().substring(0,8);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast toast = Toast.makeText(getApplicationContext(),"You are " + distanceStr + " kilometers away from the earthquake!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    ViewGroup group = (ViewGroup) toast.getView();
                    TextView messageTextView = (TextView) group.getChildAt(0);
                    messageTextView.setTextSize(25);
                    messageTextView.setGravity(Gravity.CENTER);
                    toast.show();
                }
            });
            if(longitude.length()>6){
                longitude = longitude.substring(0,6);
            }
            if(latitude.length()>6){
                latitude = latitude.substring(0,6);
            }
            location.setText("Coordinates: "+"["+latitude+", "+longitude+"]");
            time.setText("Time: "+earthquake.getMyTime());
            int tsustatus = earthquake.getMyTsu();
            if(tsustatus == 0){
                description.setText("No Tsunami Alert");
            }else{
                description.setText("Tsunami Alert!");
            }
            if(earthquakeMag>3.0 && earthquakeMag<4.0){
                magnitude.setBackgroundColor((Color.parseColor("#FFF533")));
            }
            if(earthquakeMag>=4.0 && earthquakeMag<5.0){
                magnitude.setBackgroundColor((Color.parseColor("#FFD933")));
            }
            if(earthquakeMag>=5.0 && earthquakeMag<6.0){
                magnitude.setBackgroundColor((Color.parseColor("#FFB133")));
            }
            if(earthquakeMag>=6.0 && earthquakeMag<7.0){
                magnitude.setBackgroundColor((Color.parseColor("#FF8C33")));
            }
            if(earthquakeMag>=7.0 && earthquakeMag<8.0){
                magnitude.setBackgroundColor((Color.parseColor("#FF6133")));
            }
            if(earthquakeMag>=8.0 && earthquakeMag<10.0){
                magnitude.setBackgroundColor((Color.parseColor("#FF3333")));
            }


            //get the image associated with this property


            return view;
        }
    }
    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }

    private class JsonTask extends AsyncTask<String, String, ArrayList<Earthquake>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }
        @Override
        protected ArrayList<Earthquake> doInBackground(String... params) {

            ArrayList<Earthquake> listEarthquakes = new ArrayList<>();
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String result = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    //Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }
                Log.d("Total", buffer.toString());

                result = buffer.toString();
                try {


                    JSONObject jsnobject = new JSONObject(result);
                    JSONArray jsonArray = jsnobject.getJSONArray("features");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        double magnitude = jsonArray.getJSONObject(i).getJSONObject("properties").getDouble("mag");
                        Log.v("Mag", magnitude+"");
                        String title = jsonArray.getJSONObject(i).getJSONObject("properties").getString("title");
                        Log.v("Title",title);
                        String place = jsonArray.getJSONObject(i).getJSONObject("properties").getString("place");
                        Log.v("Place",place);
                        int tsunami = jsonArray.getJSONObject(i).getJSONObject("properties").getInt("tsunami");
                        Log.v("tsu",tsunami+"");
                        JSONArray coordinates = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates");
                        double longitude = coordinates.getDouble(0);
                        double latitude = coordinates.getDouble(1);
                        Log.v("coord", latitude+", "+longitude);
                        long timeLong = jsonArray.getJSONObject(i).getJSONObject("properties").getLong("time");
                        Log.v("time", timeLong+"");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = sdf.format(new Date(timeLong));
                        Log.v("timeStr", time);
                        Earthquake newQuake = new Earthquake(title,place,magnitude,tsunami,latitude,longitude,time);
                        Log.v("Quake: ", newQuake.toString());
                        Log.v("QuakeList",listEarthquakes.toString());
                        listEarthquakes.add(newQuake);
                        Log.v("Added",listEarthquakes.toString());

                        //double longitude = jsonArray.getJSONObject(i).getJSONObject("geometry").get("coordinates")[0];
                    }
                    Log.v("bump", listEarthquakes.toString());
                    return listEarthquakes;


                }
                catch(JSONException e){
                    Log.v("ERROR", "ERROR");
                }




            } catch (MalformedURLException e) {
                Log.d("Malformed", "error");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("IO", "error");
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    Log.d("connecdisconnect", "error");
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        Log.d("readclose", "error");
                        reader.close();
                    }
                } catch (IOException e) {
                    Log.d("ioexcept", "error");
                    e.printStackTrace();
                }
                Log.d("Null", result);

            }

              return listEarthquakes;


        }

        @Override
        protected void onPostExecute(ArrayList<Earthquake> result) {


            finalQuakes = result;
            Log.v("Added Quakes", "we good");
            ArrayAdapter<Earthquake> adapter = new earthquakeArrayAdapter(MainActivity.this, 0, finalQuakes);
            ListView listView = (ListView) findViewById(R.id.customListView);
            Log.v("APPLE",listView.toString());
            listView.setAdapter(adapter);


        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF002E")));
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#BFC9CA"));
        //getActionBar().setTitle("Significant Earthquakes");


        new JsonTask().execute("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_month.geojson");







    }
}





