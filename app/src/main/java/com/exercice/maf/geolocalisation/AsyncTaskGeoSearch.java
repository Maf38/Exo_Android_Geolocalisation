package com.exercice.maf.geolocalisation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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

    public AsyncTaskGeoSearch(Activity activity, String longitude,String latitude) {
        this.context = activity.getApplicationContext();
        this.activity = activity;
        this.longitude=longitude;
        this.latitude=latitude;

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
        Log.d("testREST",inputstream.toString());
         /*   //JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = new JSONObject(inputstream);
                    new InputStreamReader(inputStream, "UTF-8"));

            reader = new InputStreamReader(inputstream, "UTF-8");

            */

         Log.d("testREST",convertInputStreamToString(inputstream));

        } catch (UnsupportedEncodingException e) {
            Log.d("testREST",e.toString());
        } catch (IOException e) {
            e.printStackTrace();
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
