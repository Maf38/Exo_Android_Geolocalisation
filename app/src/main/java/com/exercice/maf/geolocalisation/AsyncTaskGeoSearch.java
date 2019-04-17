package com.exercice.maf.geolocalisation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.localisation.Json.bean.Feature;
import com.localisation.Json.bean.Localisation;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


//Aide= https://api-adresse.data.gouv.fr/reverse/?lon=2.37&lat=48.357

public class AsyncTaskGeoSearch extends AsyncTask<Void, Void, Boolean> {

    // On a besoin du contexte pour replacer l'AsyncTask
    private Context context;
    // On récupère l'activité d'appel, au cas où besoin dans le traitement
    private Activity activity;
    private String montant;
    private String longitude;
    private String latitude;
    private ProgressDialog progressDialog;
    private InputStream inputstream;
    private String action; //String permettant de savoir quel action effectuer à l'issu de la recherche de coordonnée

    public AsyncTaskGeoSearch(Activity activity, String longitude,String latitude,String action) {
        this.context = activity.getApplicationContext();
        this.activity = activity;
        this.longitude=longitude;
        this.latitude=latitude;
        this.action = action;

    }


    @Override
    protected Boolean doInBackground(Void... voids) {


        try {
         URL   url = new URL("https://api-adresse.data.gouv.fr/reverse/?lon="+longitude+"&lat="+latitude);
         HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
         inputstream = new BufferedInputStream(urlConnection.getInputStream());
        } catch (MalformedURLException e) {
          Log.d("testREST",e.toString());
        } catch (IOException e) {
            Log.d("testREST",e.toString());
        }
        catch(Exception e){
            Log.d("testREST",e.toString());
        }


        Gson gson= new Gson();

        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        /*
         * Cette fonction contiendra le code exécuté au préalable, par exemple:
         *  -Affichage d'une ProgressBar
         *      =rond qui tourne pour indiquer une attente
         *      =Barre de progression
         *  -...
         */
        progressDialog = ProgressDialog.show(activity, "Please wait...","Convertion in progress...", true);

    }

    @Override
    protected void onPostExecute(final Boolean success) {
        /*
         * Ici, le code exécuté une fois le traitement terminé, par exemple:
         *  -Mise à jour de l'affichage
         *  -Affichage d'une pop-up indiquant la fin du traitement
         *  -Désactivation de la ProgressBar
         *  -...
         */
        try {
        progressDialog.dismiss();
        /*
        Log.d("testREST",inputstream.toString());
         /*   //JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = new JSONObject(inputstream);
                    new InputStreamReader(inputStream, "UTF-8"));

            reader = new InputStreamReader(inputstream, "UTF-8");

            */

         //Log.d("testREST",convertInputStreamToString(inputstream));

         //test avec java bean
            Gson gson = new Gson();
            //recuperation du JSon en string foruni par le WEB service
            String json=convertInputStreamToString(inputstream);

            //Recupération du bean Localisation correspondant au JSON
            Localisation loc= gson.fromJson(json, Localisation.class);
            Log.d("testREST",loc.toString());

            //on récupère le premier feature où sont stockées les propriétés de la coordonnée cherchée
            List<Feature> listeFeature = loc.getFeatures();
            Feature premiereFeature= listeFeature.get(0);
            Log.d("testREST","la ville est :"+ premiereFeature.getProperties().getCity());


            String ville= premiereFeature.getProperties().getCity();
            switch (action){
                case "chercher":
                    Toast.makeText(context,"la ville située sur ces coordonnées est: " + premiereFeature.getProperties().getCity(), Toast.LENGTH_SHORT).show();


                    break;
                case "web"://lancement d'une page goole avec les images de la ville
                   Intent intentWeb = new Intent(activity,ActivityWEB.class);
                   intentWeb.putExtra("longitude",longitude);
                   intentWeb.putExtra("latitude",latitude);

                   if(ville!=null) {// cas où une ville a été trouvée
                        intentWeb.putExtra("ville",ville);
                   }
                   else{//si on a pas trouvé de ville on travaille avec la ville de Paris
                       intentWeb.putExtra("ville","Paris");
                   }

                   intentWeb.putExtra("url","https://www.google.fr/search?hl=fr&tbm=isch&sa=1&q="+ville);
                   activity.startActivity(intentWeb);
                   break;
                case "map"://lancement d'une google map avec les coordonnées
                    Intent intentMap = new Intent(activity,ActivityWEB.class);
                    intentMap.putExtra("longitude",longitude);
                    intentMap.putExtra("latitude",latitude);

                    if(ville!=null) {// cas où une ville a été trouvée
                        intentMap.putExtra("ville",ville);
                    }
                    else{//si on a pas trouvé de ville on travaille avec la ville de Paris
                        intentMap.putExtra("ville","Paris");
                    }

                    intentMap.putExtra("url","http://maps.google.fr/maps?q="+latitude+","+longitude+"&iwloc=A&hl=fr");

                    activity.startActivity(intentMap);
                    Toast.makeText(context,"http://maps.google.fr/maps?q="+latitude+","+longitude+"&iwloc=A&hl=fr", Toast.LENGTH_SHORT).show();
                    break;

                case "adresse"://affichage de l'adresse relevé par le GPS
                    String town = premiereFeature.getProperties().getCity();
                    String street=premiereFeature.getProperties().getStreet();
                    String number =premiereFeature.getProperties().getHousenumber();
                    String zipCode = premiereFeature.getProperties().getPostcode();


                    Toast.makeText(context,"Vous êtes situé "+ number+ " "+ street+ ", "+zipCode+ " "+town+".", Toast.LENGTH_LONG).show();
                    break;

                default :

                break;

            }

            Log.d("testREST","this is a city result:"+ loc.toString());

        } catch (UnsupportedEncodingException e) {
            Log.d("testREST",e.toString());
        } catch (IOException e) {
            Log.d("testREST",e.toString());
        }
        catch (Exception e) {
            Toast.makeText(context,"coordonnées non exploitables...", Toast.LENGTH_SHORT).show();
            Log.d("testREST",e.toString());
        }



    }

    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        return result.toString(StandardCharsets.UTF_8.name());

    }

}
