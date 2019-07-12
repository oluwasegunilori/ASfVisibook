package com.asfvisibook.android;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.asfvisibook.android.common.Common;
import com.asfvisibook.android.database.Database;
import com.example.asfvisibook.Model.Member;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    ContentResolver cr;
    String value;
    private static final  String SHARED_PREFERENCES_FILE_NAME ="User_Details" ;
    private static final String TAG = "";
    DotProgressBar progressBar;
    TextView notify;
    private static String[] PERMISSIONS_READ_CALENDAR_ = {Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUI();

    }

    private void gotoHome(int duration){
            progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, Home.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_oute);


            }

        }, duration);

    }
    private void initializeUI() {
        notify = findViewById(R.id.showLoading);
       progressBar = findViewById(R.id.dot_progress_bar);
        checkiftoNotify();


    }


    private void checkiftoNotify() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.getString("already", "a").equals("Yes")){
            progressBar.setVisibility(View.VISIBLE);
            gotoHome(2500);
        }
        else if (sharedPreferences.getString("already", "a").equals("a")){
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);

            alertdialog.setTitle("Prompt");
            alertdialog.setMessage("Do you want birthday notifications for members");
            alertdialog.setCancelable(false);
            alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();





                    if (ActivityCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_CALENDAR)
                            != PackageManager.PERMISSION_GRANTED) {


                        ActivityCompat.requestPermissions(
                                MainActivity.this,
                                PERMISSIONS_READ_CALENDAR_,
                                Common.PERMISSIONS_READ_CALENDAR
                        );
                    }
                    else{
                        scheduleNotification();
                    }
                }
            }).setNeutralButton("NEVER", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    gotoHome(2500);
                    editor.putString("already", "Never");
                    editor.commit();

                }
            }).setNegativeButton("NOT NOW", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    gotoHome(2500);
                    editor.putString("already", "a");
                    editor.commit();
                }
            });
            alertdialog.show();





            }
        else if (sharedPreferences.getString("already", "a").equals("Never")){
            gotoHome(2500);
        }


    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Common.PERMISSIONS_READ_CALENDAR: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    scheduleNotification();

                } else {

                    displayMessage("Prompt", "ASF Visibook Cannot Schedule Birthday Notification without Calendar Permission");
                }
                return;
            }


        }
    }

    private void displayMessage(String title, String content){
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);

        alertdialog.setTitle(title);
        alertdialog.setMessage(content);
        alertdialog.setCancelable(false);
        alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                gotoHome(2500);

            }
        }).setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                checkiftoNotify();
            }
        });
        alertdialog.show();




    }


    private void scheduleNotification() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        List<Member> memberList = new Database(this).getAllMembers();
        notify.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        for(Member member: memberList){
            if(member.getDateofbirth().contains("/")) {
                String[] details = member.getDateofbirth().split("/");
                Calendar cal = Calendar.getInstance();
                cal.set(2019, Integer.parseInt(details[1]), Integer.parseInt(details[0]), 06, 30);

                if(Calendar.getInstance().getTimeInMillis() >= cal.getTimeInMillis()) {
                    cal.set(2020, Integer.parseInt(details[1]), Integer.parseInt(details[0]),  06, 30);
                }
                value =  value = "FREQ=YEARLY" + ";UNTIL=" + String.valueOf(cal.get(Calendar.YEAR)) + formatMonthDay(cal) + "T000000Z";
                cr = getContentResolver();
                try {
                    ContentValues values = new ContentValues();
                    values.put(CalendarContract.Events.CALENDAR_ID, getCalendarId(getApplicationContext()));
                    values.put(CalendarContract.Events.DTSTART, cal.getTimeInMillis());
                    values.put(CalendarContract.Events.DTEND, cal.getTimeInMillis());
                    values.put(CalendarContract.Events.RRULE, value);
                    values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                    values.put(CalendarContract.Events.TITLE, "Visibook Birthday Notification");
                    values.put(CalendarContract.Events.DESCRIPTION, "Today is " + member.getName() + "'s" + "Birthday");

                    values.put(CalendarContract.Events.CALENDAR_ID, String.valueOf(getCalendarId(getApplicationContext())));

                    values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

                    Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);


                }
                catch(Exception e){

                }





            }

        }

        editor.putString("already", "Yes");
        editor.commit();
        gotoHome(2500);




    }

    private int getCalendarId(Context context) {

        Cursor cursor = null;
        ContentResolver contentResolver = context.getContentResolver();
        Uri calendars = CalendarContract.Calendars.CONTENT_URI;

        String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Calendars._ID,                           // 0
                CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
                CalendarContract.Calendars.OWNER_ACCOUNT,                 // 3
                CalendarContract.Calendars.IS_PRIMARY                     // 4
        };

        int PROJECTION_ID_INDEX = 0;
        int PROJECTION_ACCOUNT_NAME_INDEX = 1;
        int PROJECTION_DISPLAY_NAME_INDEX = 2;
        int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
        int PROJECTION_VISIBLE = 4;

        cursor = contentResolver.query(calendars, EVENT_PROJECTION, null, null, null);

        if (cursor.moveToFirst()) {
            String calName;
            long calId = 0;
            String visible;

            do {
                calName = cursor.getString(PROJECTION_DISPLAY_NAME_INDEX);
                calId = cursor.getLong(PROJECTION_ID_INDEX);
                visible = cursor.getString(PROJECTION_VISIBLE);
                if (visible.equals("1")) {
                    return (int) calId;
                }
                Log.e("Calendar Id : ", "" + calId + " : " + calName + " : " + visible);
            } while (cursor.moveToNext());

            return (int) calId;
        }
        return -1;
    }



    private static String formatMonthDay(Calendar calendar) {
        String myFormat = "MMdd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        return sdf.format(calendar.getTime());

    }

    private static String formatYear(Calendar calendar) {
        String myFormat = "yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        return sdf.format(calendar.getTime());

    }

}
