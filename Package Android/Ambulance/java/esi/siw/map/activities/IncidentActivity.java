package esi.siw.map.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import esi.siw.map.Model.Hopital;
import esi.siw.map.Model.Intervention;
import esi.siw.map.R;



public class IncidentActivity extends AppCompatActivity {

    EditText nom_victime;
    EditText prenom_victime;
    EditText age_victime;
    Spinner spinnerIncidents;
    Spinner spinnerState;
    Spinner spinnerGroupage;
    String sexe_victime;
    RadioGroup RGsexe;

    InputStream is=null;
    String result=null;
    String line=null;
    int code;

    public static Intervention intervention;

    public static List<Hopital> listeHopitaux = new ArrayList<Hopital>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewincident);

        getServices();

        spinnerIncidents = (Spinner) findViewById(R.id.TypeIncidents);
        //String[] itemsIncidents = new String[]{"Fractures","Blessures","Cardio-vasculaire","Brulures","Noyades","Intoxications alimentaires","Intoxication par émanation de gaz","Empoisonnement","Hypoglycimé du diabète"};
        String[] itemsIncidents = new String[]{"Secours et Evacuation","Accidents de la circulation","Incendies","Operation diverses"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsIncidents);
        spinnerIncidents.setAdapter(adapter);

        spinnerState = (Spinner) findViewById(R.id.StateVictime);
        //String[] itemsStates = new String[]{"Mauvaise","Grave","Trés grave"};
        String[] itemsStates = new String[]{"dcd","malade","blessé","autre"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsStates);
        spinnerState.setAdapter(adapter1);

        spinnerGroupage = (Spinner) findViewById(R.id.GroupageVictime);
        String[] itemsGroupage = new String[]{"A+","A-","B+","B-","O+","O-"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsGroupage);
        spinnerGroupage.setAdapter(adapter2);

        nom_victime = findViewById(R.id.nom_victime) ;
        prenom_victime = findViewById(R.id.prenom_victime);
        age_victime = findViewById(R.id.age_victime);

        RGsexe = (RadioGroup) findViewById(R.id.sexe);

        final Button btn_service = (Button) findViewById(R.id.btn_service);

        btn_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(RGsexe.getCheckedRadioButtonId() == -1 || age_victime.getText().toString().isEmpty()){
                    Toast.makeText(IncidentActivity.this,
                            "Veullez saisir les champs svp !", Toast.LENGTH_LONG).show();
                }else{
                    sexe_victime = ((RadioButton)findViewById(RGsexe.getCheckedRadioButtonId())).getText().toString();

                    intervention = new Intervention(nom_victime.getText().toString(), prenom_victime.getText().toString(),
                            age_victime.getText().toString(), sexe_victime, spinnerIncidents.getSelectedItem().toString(), spinnerState.getSelectedItem().toString(), spinnerGroupage.getSelectedItem().toString());

                    Intent intent =new Intent(IncidentActivity.this, ServiceActivity.class);
                    intent.putExtra("object", intervention);
                    startActivity(intent);
                }

            }
        });
    }



    public int getServices(){
        String URL="http://192.168.43.120:8000/api/h";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Rest Response",response.toString());
                        //System.out.println(response.toString());
                        try {

                            JSONArray jsonArray = response.getJSONArray("data");
                            for(int i = 0; i< jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                JSONArray hopitalArray = jsonObject.getJSONArray("hopital");
                                JSONObject hopitalObject = hopitalArray.getJSONObject(0);

                                JSONArray serviceArray = jsonObject.getJSONArray("service");
                                JSONObject serviceObject = serviceArray.getJSONObject(0);

                                listeHopitaux.add(new Hopital(
                                        jsonObject.getInt("id"),
                                        hopitalObject.getString("designation"),
                                        hopitalObject.getString("type"),
                                        serviceObject.getString("designation"),
                                        jsonObject.getInt("lits"),
                                        hopitalObject.getString("longitude"),
                                        hopitalObject.getString("latitude"),
                                        hopitalObject.getString("adresse"),
                                        hopitalObject.getString("tel")

                                ));


                                System.out.println(listeHopitaux.size());

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response",error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);
        return listeHopitaux.size();
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_exit:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialoguexit);
                dialog.setTitle("Close confirmation");
                Button btnExit = (Button) dialog.findViewById(R.id.btn_exit);
                Button btnNoExit = (Button) dialog.findViewById(R.id.btn_noexit);

                dialog.show();
                btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                        dialog.dismiss();
                    }
                });

                btnNoExit.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

        }
        switch (item.getItemId()) {

            case R.id.action_help:
                final Dialog dialog2 = new Dialog(this);
                dialog2.setContentView(R.layout.help_dialogue);
                dialog2.setTitle("A Propos de");
                Button btnHelp = (Button) dialog2.findViewById(R.id.btn_help);

                dialog2.show();
                btnHelp.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        dialog2.dismiss();
                    }
                });

            default: return super.onOptionsItemSelected(item);
        }

    }

}
