package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
import com.loopj.android.http.*;


import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTC";

    // Member Variables:
    TextView mPriceTextView;
    String BASE_URL2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the 'String array' and a 'spinner layout'
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the 'layout' to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // retrieve the selected item with parent.getItemAtPosition(position)
                Log.d("Bitcoin", "" + parent.getItemAtPosition(position));

                // only need to push 'symbol' ('market': global): convert to string
                String currency = String.valueOf(parent.getItemAtPosition(position));

                // Add GET parameters with RequestParams: 'market', 'symbol'
                // RequestParams params = new RequestParams();
                // params.put("symbol", currency);

                // modify the Base_URL
                BASE_URL2 = BASE_URL + currency;

                // call networking method
                letsDoSomeNetworking();

                Log.d(currency, "This is the currency");

                // prompt user of corresponding selection
                Toast.makeText(MainActivity.this, "You have selected " + currency, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("Bitcoin", "Nothing selected");

            }
        });

    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(BASE_URL2, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                Log.d("Bitcoin", "JSON: " + response.toString());

                // getString() to extract the 1st piece of text from the JSON response
                try {
                    String mSymbol = response.getString("ask");
                    mPriceTextView.setText(mSymbol);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Bitcoin", "Request fail! Status code: " + statusCode);
                Log.d("Bitcoin", "Fail response: " + response);
                Log.e("Bitcoin", "ERROR" + e.toString());

                // prompt user regarding failure status
                Toast.makeText(MainActivity.this, "Request Failed!", Toast.LENGTH_SHORT).show();

                Log.d("Bitcoin", BASE_URL + "This is the FINAL URL");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });


    }


}
