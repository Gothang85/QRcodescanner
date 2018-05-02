package com.thegotham.myapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;


import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class walkIn extends AppCompatActivity {
NumberPicker hp, mp;
int spot;
DatePicker dp;
DynamoDBMapper dynamoDBMapper;
    public static PinpointManager pinpointManager;

    public static TextView tvr;

    public static ViewAnimator va;

    final int capacity;
    {
        capacity = 5;
    }
public boolean found;
Calendar startTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_in);
        hp = (NumberPicker) findViewById(R.id.hourPicker);
        hp.setMinValue(0);
        hp.setMaxValue(23);
        hp.setWrapSelectorWheel(true);
        mp = (NumberPicker) findViewById(R.id.minutePicker);
        mp.setMinValue(0);
        mp.setMaxValue(59);
        mp.setWrapSelectorWheel(true);
        checkTimes();
    }
    /*public void checkIn() {
        Button but = (Button)findViewById(R.id.button2);

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            System.out.print("Hello");
            }
        });
    }
*/
    public void checkTimes(){
        Button confirm = (Button)findViewById(R.id.button2);
        confirm.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                //System.out.print("Hola");
                Date currentTime = Calendar.getInstance().getTime();
                int hour1, hour2, min1, min2, tot1, tot2;
                Calendar start = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH),Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE));
                Calendar end = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH),Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+hp.getValue(),Calendar.getInstance().get(Calendar.MINUTE)+mp.getValue());
                String startatt = String.valueOf(start.getTimeInMillis()/1000);
                String endatt = String.valueOf(end.getTimeInMillis()/1000);


                Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();


                final DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                scanExpression.addFilterCondition("StartTime",
                        new Condition()
                                .withComparisonOperator(ComparisonOperator.LE)
                                .withAttributeValueList(new AttributeValue().withN(endatt)));
                scanExpression.addFilterCondition("EndTime",
                        new Condition()
                                .withComparisonOperator(ComparisonOperator.GE)
                                .withAttributeValueList(new AttributeValue().withN(startatt)));

                final int[] size = new int[1];
                final Thread t = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        List<GarageScheduleDO> scanResult = dynamoDBMapper.scan(GarageScheduleDO.class,scanExpression);
                        Set<Integer> spotList = new HashSet<Integer>();
                        //System.out.println(scanResult.size());
                       /* if(scanResult.size() >= capacity){
                            found = false;
                            return;
                        }*/
                      //  else {
                            found = true;
                            size[0] = scanResult.size();
                            for (GarageScheduleDO scan : scanResult) {
                                spotList.add(scan.getParkID().intValue());
                            }
                            int i;
                            for(i = 1; i <= capacity; i++){
                                if(!spotList.contains(i)){
                                    spot = i;
                                    break;
                                }
                            }

                            //schedule.setSpotID((double)i);
                            //dynamoDBMapper.save(schedule);
                      //  }
                    }
                });
                /*t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                */
                //pb.setVisibility(View.INVISIBLE);
              //  if(!found){
                    Toast.makeText(getBaseContext(), "There are no available parking spots at this time", Toast.LENGTH_LONG).show();
               // }
              //  else{
                    Toast.makeText(getBaseContext(), "Parking Spot Found", Toast.LENGTH_LONG).show();
                    long duration = (start.getTimeInMillis() - end.getTimeInMillis())/60000; //minutes duration

                    Intent transition = new Intent(getBaseContext(), confirmationActivity.class);
                    transition.putExtra("ParkID", (double)spot);
                    transition.putExtra("StartTime", Double.parseDouble(startatt));
                    transition.putExtra("EndTime", Double.parseDouble(endatt));
                    transition.putExtra("StartString", start.getTime().toString());
                    transition.putExtra("EndString",end.getTime().toString());
                    transition.putExtra("Duration",duration);
                    startActivity(transition);

                //}


            }



        });

    }
    public void openAcitivity4() {
        Intent intentcon = new Intent(this,Confirmed.class);
        startActivity(intentcon);
    }

}
