package esi.siw.map.activities;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import esi.siw.map.Model.Hopital;
import esi.siw.map.R;

import static esi.siw.map.activities.IncidentActivity.listeHopitaux;

public class ServiceActivity extends AppCompatActivity {

    public static List<Hopital> final_hopitals = new ArrayList<Hopital>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewservice);

        final Spinner spinnerService = (Spinner) findViewById(R.id.TypeService);
        List<String> itemService = new ArrayList<String>();
        fill_ServiceListe(itemService);

        //String[] items = new String[]{"Cardio-vasculaire","Fractures","Blessures","Brulures","Noyades","Intoxications alimentaires","Intoxication par émanation de gaz","Empoisonnement","Hypoglycimé du diabète"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemService);
        spinnerService.setAdapter(adapter);


        final Button locate_btn = (Button) findViewById(R.id.btn_locate);
        final Button detail_btn = (Button) findViewById(R.id.btn_hopital);

        locate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //filtrer les hopitaux pour le résultat final
                get_hopitals(listeHopitaux,final_hopitals, spinnerService.getSelectedItem().toString());

                final ProgressDialog progressdialog = new ProgressDialog(ServiceActivity.this, R.style.AppCompatAlertDialogStyle);
                progressdialog.setMessage("Please Wait....");
                progressdialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        progressdialog.dismiss();
                        Intent intent =new Intent(ServiceActivity.this, MapActivity.class);
                        startActivity(intent);
                    }
                }, 1000);



            }
        });

        detail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //filtrer les hopitaux pour le résultat final
                get_hopitals(listeHopitaux,final_hopitals, spinnerService.getSelectedItem().toString());

                final Dialog dialog = new Dialog(ServiceActivity.this);
                dialog.setContentView(R.layout.detail_dialog);
                dialog.setTitle("Detail hopitaux");

                Button btnOK = (Button) dialog.findViewById(R.id.btn_ok);
                TextView detail_content = (TextView) dialog.findViewById(R.id.content);

                detail_content.setText(getDetail_hopital());

                dialog.show();


                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

    }

    public Boolean exist(List<String> liste,String service){
        Boolean b = false;
        for(int i=0; i<liste.size(); i++){
            if(liste.get(i).equals(service)){
                b =  true;
            }
        }
        return b;
    }

    public void fill_ServiceListe(List<String> liste){
        liste.clear();
        for(Hopital h:listeHopitaux){
            if(!exist(liste, h.getService())){
                liste.add(new String(h.getService()));
            }
        }

    }

    //filtre des hopitaux
    public void get_hopitals(List<Hopital> hopitals,List<Hopital> final_hopitals,String service){
        final_hopitals.clear();
        for(Hopital h:hopitals){
            if(h.getService().equals(service) && h.getLits() > 0){
                final_hopitals.add(h);
            }
        }
    }

    public String getDetail_hopital(){
        String detail = null;
        for(Hopital h:final_hopitals){
            if(detail != null){
                detail = detail+"\n"+
                        h.getType()+" : "+h.getNom()+"\n"+
                        "Tel : "+h.getTel()+"\n"+
                        "Adresse : "+h.getAdresse()+"\n"+
                        "___________________________"+"\n";
            }else{
                detail =
                        h.getType()+" : "+h.getNom()+"\n"+
                        "Tel : "+h.getTel()+"\n"+
                        "Adresse : "+h.getAdresse()+"\n"+
                        "___________________________"+"\n";
            }


        }
        return detail;
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
