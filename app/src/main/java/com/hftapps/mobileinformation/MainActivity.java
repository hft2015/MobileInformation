package com.hftapps.mobileinformation;


import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    EditText mobileNumber;
    ProgressDialog dialog;
    TextView requestedNumber, operatorType, circleType, simType, noResult;
    ImageView imageView;

    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DBHandler db = new DBHandler(this);
        List<Requests> requests = db.getAllRequests();

        if(requests.size() == 0){
            noResult = (TextView) findViewById(R.id.noResult);
            noResult.setVisibility(View.VISIBLE);
        }


        //Toast.makeText(this, "size : "+requests.size(), Toast.LENGTH_LONG).show();

        listView = (ListView) findViewById(R.id.list_requests);
        RequestListAdapter adapter = new RequestListAdapter(this, R.layout.list_item, requests);
        listView.setAdapter(adapter);


        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);

       /* listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/


        simType = (TextView) findViewById(R.id.simType);
        circleType = (TextView) findViewById(R.id.circleType);
        operatorType = (TextView) findViewById(R.id.operatorType);
        requestedNumber = (TextView) findViewById(R.id.requestedNumber);
        mobileNumber = (EditText) findViewById(R.id.mobileNumber);
        imageView = (ImageView) findViewById(R.id.logo);



        mobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int mobileNumberLength = s.length();
                if (mobileNumberLength == 10)
                    InitializeProgressBar(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void InitializeProgressBar(String mobileNumber) {
        Log.e("Mobile Number: ", "" + mobileNumber);


        Context context = getApplicationContext();
        int flag = 0;

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        flag = 1;
                        dialog = new ProgressDialog(this);
                        new GetAPIRequest(mobileNumber).execute();
                    }
                }
            }
        }

        if (flag == 0)
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();

    }

    private class GetAPIRequest extends AsyncTask<Void, Void, Void> {

        String Mob;
        String requestedResponse;

        public GetAPIRequest(String mob) {
            Mob = mob;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            dialog.setMessage("Loading.....");
            dialog.setCancelable(false);
            dialog.show();


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (dialog.isShowing()) {
                requestedNumber.setText(Mob);

                try {

                    String op, cr;

                    JSONObject jsonObject = new JSONObject(requestedResponse);

                    if (jsonObject.getString("operator") == "null")
                        op = "999";
                    else
                        op = jsonObject.getString("operator");

                    if (jsonObject.getString("circle") == "null")
                        cr = "999";
                    else
                        cr = jsonObject.getString("circle");


                    final DBHandler dbHandler = new DBHandler(getApplicationContext());
                    String result[] = dbHandler.getRequestedDetails(Mob, op, cr);


                    ImageView topLogo = (ImageView) findViewById(R.id.topLogo);
                    topLogo.setVisibility(View.GONE);

                    TextView topDescription = (TextView) findViewById(R.id.topDescription);
                    topDescription.setVisibility(View.GONE);



                    TextView topHeaderLayout = (TextView) findViewById(R.id.topHeader);
                    topHeaderLayout.setVisibility(View.VISIBLE);

                    LinearLayout resultLayout = (LinearLayout) findViewById(R.id.resultLayout);
                    resultLayout.setVisibility(View.VISIBLE);


                    mobileNumber.setText("");
                    operatorType.setText(result[0]);
                    circleType.setText(result[1]);
                    simType.setText(result[2]);

                    String uri = "@drawable/" + result[3];
                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                    imageView.setImageResource(imageResource);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
            }

        }

        @Override
        protected Void doInBackground(Void... params) {

            String url = "http://mcruze.hftapis.com/apis/getnetwork/"+Mob;
            String response = null;

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
            try {
                httpResponse = httpClient.execute(httpGet);
                httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
                requestedResponse = response;
                Log.e("result : ","-"+response);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}