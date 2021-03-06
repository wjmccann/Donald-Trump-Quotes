package com.wjmccann.donaldtrumpquotes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static String url = "https://api.tronalddump.io/random/quote";
    private ProgressDialog pDialog;
    private String TAG = MainActivity.class.getSimpleName();

    public String q;
    public String s;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new GetQuote().execute();

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void refreshQuote(View view){
        new GetQuote().execute();
    }

    public void getSource(View view){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(s));
        startActivity(i);
    }

    public void shareIt(View view){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Trump Quotes");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, q);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private class GetQuote extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //pDialog = new ProgressDialog(MainActivity.this);
            //pDialog.setMessage("Please wait...");
            //pDialog.setCancelable(false);
            //pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0){
            HttpHandler handler = new HttpHandler();

            String jsonStr = handler.makeServiceCall(url);
            //Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null){
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONObject embed = jsonObj.getJSONObject("_embedded");
                    JSONArray source = embed.getJSONArray("source");
                    JSONObject mySource = source.getJSONObject(0);
                    q = jsonObj.getString("value");
                    s = mySource.getString("url");

                    Log.e(TAG, "String found: " + s);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //if (pDialog.isShowing())
               // pDialog.dismiss();

            final Animation in = new AlphaAnimation(0.0f, 1.0f);
            in.setDuration(500);

            final Animation out = new AlphaAnimation(1.0f, 0.0f);
            out.setDuration(500);

            final TextView quote = (TextView)findViewById(R.id.quote);

            out.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationEnd(Animation animation) {
                    quote.setText(q);
                    quote.startAnimation(in);

                }
                @Override
                public void onAnimationRepeat(Animation animation){

                }
                public void onAnimationStart(Animation animation){

                }
            });

            quote.startAnimation(out);




        }
    }
}
