package com.asfvisibook.android;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.asfvisibook.android.R;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home extends AppCompatActivity {

    private static final String TAG = "" ;
    @BindView(R.id.intelligent)
TextView intelligenttext;
BottomNavigationView navigation;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.beliefs:
                    startActivity(new Intent(Home.this, Beliefs.class));

                    return true;
                case R.id.home:

                    return true;
                case R.id.about:
                    startActivity(new Intent(Home.this,About.class));

                    return true;
                case R.id.excos:
                    startActivity(new Intent(Home.this,Excos.class));

                    return true;
                case R.id.members:
                    startActivity(new Intent(Home.this, Members.class));


                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Calendar c = Calendar.getInstance();
        int time = c.get(Calendar.HOUR_OF_DAY);
        setTime(time);


        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private void setTime(int time) {
        if(time >=0 && time<12){
            intelligenttext.setText("Good Morning");
        }
        else if (time >=12 && time<16){
            intelligenttext.setText("Good Afternoon");
        }
        else if(time >=17 && time<=23){
            intelligenttext.setText("Good Evening");
        }
    }
    public void facebookGO(View view) {
        try {
            int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + "https://www.facebook.com/officialasfoau")));
            }else{
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/100019274070834")));

            }
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/officialasfoau")));
        }
    }

    public void twitterGo(View view) {
        Intent intent = null;
        try {
            // get the Twitter app if possible
            this.getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=883085402331955200"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/officialasfoau"));
        }
        this.startActivity(intent);
    }


    public void youtubeGo(View view) {
        Intent intent = new Intent(
                Intent.ACTION_VIEW ,
                Uri.parse("https://www.youtube.com/channel/UC3LJOtYfOxCkOyPwyplNS8A"));
        intent.setComponent(new ComponentName("com.google.android.youtube","com.google.android.youtube.PlayerActivity"));

        PackageManager manager = getPackageManager();
        List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
        if (infos.size() > 0) {
           startActivity(intent);
        }else{
           startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UC3LJOtYfOxCkOyPwyplNS8A"))); //No Application can handle your intent
        }

    }

    public void shareApp(View view) {

    }

    public void sendApp(View view) {
try {
    ApplicationInfo app = getApplicationContext().getApplicationInfo();
    String filePath = app.sourceDir;

    Intent intent = new Intent(Intent.ACTION_SEND);

    // MIME of .apk is "application/vnd.android.package-archive".
    // but Bluetooth does not accept this. Let's use "*/*" instead.
    intent.setType("*/*");


    // Append file and send Intent
    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
    startActivity(Intent.createChooser(intent, "Share app via"));
}catch(Exception e){

}
    }

    public void openBlog(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://asfoau.wordpress.com"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.d(TAG, e.toString());
        }

    }



    public void playMusic(View view) {

    }

    public void articles(View view) {
        startActivity(new Intent(Home.this, Articles.class));



    }

    @Override
    protected void onResume() {
        navigation.setSelectedItemId(R.id.home);
        Calendar c = Calendar.getInstance();
        int time = c.get(Calendar.HOUR_OF_DAY);
        setTime(time);
        super.onResume();
    }

    @Override
    public void onBackPressed() {

        this.moveTaskToBack(true);

    }
}



