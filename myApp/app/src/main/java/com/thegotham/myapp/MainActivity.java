package com.thegotham.myapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.mobile.auth.ui.AuthUIConfiguration;
import com.amazonaws.mobile.auth.ui.SignInUI;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private Button button1;

    public static PinpointManager pinpointManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                AuthUIConfiguration config =
                        new AuthUIConfiguration.Builder()
                                .userPools(true)  // true? show the Email and Password UI
                                .logoResId(R.drawable.default_sign_in_logo) // Change the logo
                                //.backgroundColor(Color) // Change the backgroundColor
                                .isBackgroundColorFullScreen(true) // Full screen backgroundColor the backgroundColor full screenff
                                .fontFamily("sans-serif-light") // Apply sans-serif-light as the global font
                                .canCancel(false)
                                .build();
                SignInUI signin = (SignInUI) AWSMobileClient.getInstance().getClient(MainActivity.this, SignInUI.class);

                signin.login(MainActivity.this, scan.class).authUIConfiguration(config).execute();
            }
        }).execute();
        button =(Button)findViewById(R.id.btnCheckIn);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openAcitivity2();
            }
        });
        button1 =(Button)findViewById(R.id.btnWalkIn);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openAcitivity3();
            }
        });
    }
    public void openAcitivity2() {
        Intent intent = new Intent(this,scan.class);
        startActivity(intent);
    }
    public void openAcitivity3() {
        Intent intent1 = new Intent(this,walkIn.class);
        startActivity(intent1);
    }

}
