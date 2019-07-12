package com.asfvisibook.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asfvisibook.android.R;
import com.asfvisibook.android.common.Common;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.asfvisibook.Model.ArticleClass;
import com.example.asfvisibook.Model.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static com.asfvisibook.android.common.Common.SHARED_PREFERENCES_FILE_NAME_USER;

public class ArticlesDetails extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @BindView(R.id.errorlayout)
    RelativeLayout errorlayout;
    @BindView(R.id.author)
    TextView author;
    @BindView(R.id.retry)
    Button retry;
@BindView(R.id.title)
TextView title;
@BindView(R.id.time)
TextView time;
@BindView(R.id.articledetails)
TextView details;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.backdrop)
    ImageView backgroundimage;
    @BindView(R.id.loadImage)
    ProgressBar loadImage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.transparentbackground)
    LinearLayout transparentbackground;

    private String getdate, getdetails, gettitle, getauthor, getImage, gettime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            loadContent();
        } catch (ParseException e) {
            e.printStackTrace();
        }

retry.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(Common.isConnectedToInternet(getBaseContext())) {
            errorlayout.setVisibility(View.GONE);
            try {
                loadContent();
            } catch (ParseException e) {
                e.printStackTrace();

            }
        }else{

        }
    }
});
    }

    private void loadContent() throws ParseException {

        getdate = getIntent().getStringExtra("date");
        getdetails = getIntent().getStringExtra("details");
        gettitle = getIntent().getStringExtra("title");
        getauthor = getIntent().getStringExtra("author");
        getImage = getIntent().getStringExtra("image");
        gettime = getIntent().getStringExtra("time");

        transparentbackground.setVisibility(View.VISIBLE);


       DocumentReference documentReference = db.collection("Articles").document(gettitle);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    transparentbackground.setVisibility(GONE);
                    ArticleClass  model = task.getResult().toObject(ArticleClass.class);

                    date.setText(getdate);
                    title.setText(gettitle);
                    details.setText(getdetails);
                    author.setText(getauthor);
                    time.setText(Utils.DateToTimeFormat(gettime));
                    loadImage.setVisibility(View.VISIBLE);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(Utils.getRandomDrawbleColor());
                    requestOptions.error(Utils.getRandomDrawbleColor());
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                    requestOptions.centerCrop();
                    requestOptions.timeout(100000);

                    Glide.with(getBaseContext())
                            .load(model.getImage())
                            .apply(requestOptions)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    loadImage.setVisibility(GONE);


                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                    loadImage.setVisibility(GONE);

                                    return false;
                                }
                            })
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(backgroundimage);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                transparentbackground.setVisibility(GONE);
                errorlayout.setVisibility(View.VISIBLE);
                }
            });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_NAME_USER, MODE_PRIVATE);

        int id = item.getItemId();
        if(id==android.R.id.home){
            super.onBackPressed();
        }


        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}

