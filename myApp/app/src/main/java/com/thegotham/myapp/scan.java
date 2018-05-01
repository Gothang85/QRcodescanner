package com.thegotham.myapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
public class scan extends AppCompatActivity {
    private Button scanbtn;
    DynamoDBMapper dynamoDBMapper;
    GarageScheduleDO uitem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        //AWSMobileClient.getInstance().initialize(getApplicationContext()).execute();
        //set up db communication
        //AWSMobileClient.getInstance().initialize(this,).execute();
        AmazonDynamoDB dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .build();
        System.out.println("hello\n");
        scanbtn = (Button) findViewById(R.id.scanbtn);
        final Activity activity = this;
        scanbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // Doing
        // string userID
        // (UserID is a random name I came up with it) which is the user's unique UserID and then,
        // userID = result.getContents();
        // which you should use to compare this userID variable with the database of userID's
        //I suggest adding something that would give a green checkmark to the user with the qr code on their phone
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else{
                //GarageScheduleDO uItem = new GarageScheduleDO();
                final String id = result.getContents();
                final Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        uitem = dynamoDBMapper.load(
                                GarageScheduleDO.class,
                                id);

                        // Item read
                        // Log.d("News Item:", newsItem.toString());
                    }
                });
                t1.start();
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(uitem == null){
                    Toast.makeText(this, "Not a valid QR code", Toast.LENGTH_LONG).show();
                }else {
                    uitem.setCheckedIn(Boolean.TRUE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            dynamoDBMapper.save(uitem);
                        }
                    }).start();
                    Toast.makeText(this, "Checked into Garage", Toast.LENGTH_LONG).show();
                }
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);

        }

    }
}
