package esi.siw.map.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.Polygon;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import esi.siw.map.Model.Hopital;
import esi.siw.map.Model.Intervention;
import esi.siw.map.R;

import static esi.siw.map.activities.IncidentActivity.intervention;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    InputStream is=null;
    String result=null;
    String line=null;
    int code;

    String hopital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewmap);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        for(Hopital h:ServiceActivity.final_hopitals){
            LatLng LL_hopital = new LatLng(Double.parseDouble(h.getLatitude()),Double.parseDouble(h.getLongitude()));
            mMap.addMarker(new MarkerOptions().position(LL_hopital).title(h.getType()+" : "+h.getNom()).snippet("Tel : "+h.getTel()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(LL_hopital));
            System.out.println(h.getNom());

        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.setMyLocationEnabled(true);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.e("Testing",marker.getTitle());
                hopital = marker.getTitle();
                return false;
            }

        });

      mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
          @Override
          public void onInfoWindowLongClick(Marker marker) {
              final Dialog dialog = new Dialog(MapActivity.this);
              dialog.setContentView(R.layout.dialoguepost);
              dialog.setTitle("Confirmation");

              Button btnPost = (Button) dialog.findViewById(R.id.btn_post);
              Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);

              dialog.show();


              btnPost.setOnClickListener(new View.OnClickListener() {
                  @Override public void onClick(View v) {

                      post_intervention(intervention.getNom(),intervention.getPrenom(),intervention.getSexe(),intervention.getAge(),intervention.getIncident(),intervention.getEtat(),intervention.getGroupage(), String.valueOf(get_idHopital(hopital)));

                      dialog.dismiss();
                      Toast.makeText(MapActivity.this,
                              "Notification réussie vers l'hopital", Toast.LENGTH_LONG).show();
                  }
              });

              btnCancel.setOnClickListener(new View.OnClickListener() {
                  @Override public void onClick(View v) {
                      dialog.dismiss();
                  }
              });

          }
      });



    }

    public void post_intervention(String nom,String prenom, String sexe, String age, String type, String etat, String groupage, String hopital)
    {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("nom",nom));
        nameValuePairs.add(new BasicNameValuePair("prenom",prenom));
        nameValuePairs.add(new BasicNameValuePair("sexe",sexe));
        nameValuePairs.add(new BasicNameValuePair("age",age));
        nameValuePairs.add(new BasicNameValuePair("type",type));
        nameValuePairs.add(new BasicNameValuePair("etat",etat));
        nameValuePairs.add(new BasicNameValuePair("groupage",groupage));
        nameValuePairs.add(new BasicNameValuePair("hopital",hopital));


        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.43.120" +
                    "/intervention.php");

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("pass 1", "connection success ");
        }
        catch(Exception e)
        {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

        try
        {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.e("pass 2", "connection success ");
        }
        catch(Exception e)
        {
            Log.e("Fail 2", e.toString());
        }

        try
        {
            JSONObject json_data = new JSONObject(result);
            code=(json_data.getInt("code"));

            if(code==1)
            {
                Toast.makeText(getBaseContext(), "Inserted Successfully",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getBaseContext(), "Sorry, Try Again",
                        Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception e)
        {
            Log.e("Fail 3", e.toString());
        }
    }

    public int get_idHopital(String hopital){
        int id = 0;

        for(Hopital h:ServiceActivity.final_hopitals){
            String title = h.getType()+" : "+h.getNom();
            if(title.equals(hopital)){
                id = h.getId();
            }
        }

        return id;
    }
}
