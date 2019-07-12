package com.asfvisibook.android;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.asfvisibook.Model.Token;
import com.example.asfvisibook.Model.Utils;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.internal.Util;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.asfvisibook.android.common.Common.SHARED_PREFERENCES_FILE_NAME_USER;

public class Articles extends AppCompatActivity {
    private static final String TAG =  "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LinearLayoutManager linearLayoutManager;
    private FirestoreRecyclerAdapter adapter;
    @BindView(R.id.articlelist)
    RecyclerView articleList;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.transparentbackground)
    LinearLayout transparentbackground;
    @BindView(R.id.errorlayout)
    RelativeLayout errorlayout;
    @BindView(R.id.retry)
    Button retry;
    @BindView(R.id.messagetext)
    TextView messagetext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);
        ButterKnife.bind(this);
        intialize();
        loadArticles();

    }

    private void intialize() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lovely Articles");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.isConnectedToInternet(getBaseContext())){
                    errorlayout.setVisibility(View.GONE);
                    loadArticles();
                }else{

                }
            }
        });
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        articleList.setLayoutManager(linearLayoutManager);
    }

    private void loadArticles() {
    if(Common.isConnectedToInternet(getBaseContext())){
        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_NAME_USER, MODE_PRIVATE);
        transparentbackground.setVisibility(View.VISIBLE);
        messagetext.setVisibility(View.GONE);


        Query query = db.collection("Articles").orderBy("date");
        FirestoreRecyclerOptions<ArticleClass> response = new FirestoreRecyclerOptions.Builder<ArticleClass>()
                .setQuery(query, ArticleClass.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<ArticleClass, ScheduleHolder>(response) {
            @Override
            protected void onBindViewHolder(@NonNull final ScheduleHolder viewHolder, final int position, final ArticleClass model) {


                transparentbackground.setVisibility(View.GONE);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(Utils.getRandomDrawbleColor());
                requestOptions.error(Utils.getRandomDrawbleColor());
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                requestOptions.centerCrop();
                requestOptions.timeout(10000);
                Glide.with(getBaseContext())
                        .load(model.getImage())
                        .apply(requestOptions)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                viewHolder.progressBar.setVisibility(GONE);
                                messagetext.setVisibility(GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                viewHolder.progressBar.setVisibility(GONE);
                                messagetext.setVisibility(GONE);

                                return false;
                            }
                        })
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewHolder.image);
            String dater = model.getDate().substring(0,10);
                viewHolder.title.setText(model.getTitle());
                viewHolder.date.setText(Utils.DateFormat(dater));
                viewHolder.author.setText(model.getAuthor());
                if(sharedPreferences.getString("user", "").equals("Yes")){
                    viewHolder.delete.setVisibility(VISIBLE);
                }
                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Common.isConnectedToInternet(getBaseContext())) {

                            AlertDialog.Builder alertdialog = new AlertDialog.Builder(Articles.this);

                            alertdialog.setTitle("Prompt");
                            alertdialog.setMessage("Delete Article?");
                            alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {
                                    viewHolder.progressBardelete.setVisibility(VISIBLE);
                                    DocumentReference docRef = db.collection("Articles").document(model.getTitle());
                                    docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            dialog.dismiss();
                                            viewHolder.progressBardelete.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();


                                        }
                                    })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    dialog.dismiss();

                                                    Toast.makeText(getApplicationContext(), "Delete Failed", Toast.LENGTH_SHORT).show();
                                viewHolder.progressBardelete.setVisibility(GONE);
                                                }
                                            });

                                }
                            }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();


                                }
                            });
                            alertdialog.show();



                        }else{
                            displayMessage("Error:", "Check Internet Connection");
                        }


                    }
                });
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String dater = model.getDate().substring(0,10);


                        Intent intent = new Intent(Articles.this, ArticlesDetails.class);
                        intent.putExtra("details", model.getDetails());
                        intent.putExtra("author", model.getAuthor());
                        intent.putExtra("date", Utils.DateFormat(dater));
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("image", String.valueOf(model.getImage()));
                        intent.putExtra("time", String.valueOf(model.getDate()));
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_oute);


                    }
                });

            }

            @Override
            public ScheduleHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.articles_item, viewGroup, false);
                return new ScheduleHolder(view);
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDataChanged() {
                if(adapter.getItemCount()<=0){
                    transparentbackground.setVisibility(View.GONE);
                    messagetext.setText("No Article(s)");
                    messagetext.setVisibility(View.VISIBLE);
                }
                super.onDataChanged();

            }

            @Override
            public void onChildChanged(@NonNull ChangeEventType type, @NonNull DocumentSnapshot snapshot, int newIndex, int oldIndex) {

                super.onChildChanged(type, snapshot, newIndex, oldIndex);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        articleList.setAdapter(adapter);



    }
    else{
        errorlayout.setVisibility(View.VISIBLE);

    }

    }
    public class  ScheduleHolder extends RecyclerView.ViewHolder{
        public ImageView delete;
        public ImageView image;
        public TextView author;
        public TextView title;
        public TextView date;
        ProgressBar progressBar;

        ProgressBar progressBardelete;

        public ScheduleHolder(@NonNull View itemView) {
            super(itemView);
            progressBardelete = itemView.findViewById(R.id.progressdelete);
            progressBar = itemView.findViewById(R.id.progress_load_photo);
            date = itemView.findViewById(R.id.publishedAt);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            image = itemView.findViewById(R.id.img);
            delete = itemView.findViewById(R.id.delete);


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
     if (adapter!=null)
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter!=null)
            adapter.stopListening();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.article_menu,menu);
        return true;
    }


    private void displayMessage(String title, String content){
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);

        alertdialog.setTitle(title);
        alertdialog.setMessage(content);
        alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertdialog.show();




    }
    private void manualAdd(){
        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_NAME_USER, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Articles.this);
        alertDialog.setIcon(R.drawable.ic_lock_black_24dp);
        alertDialog.setTitle("For Editorial Team only");

        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.gotocreatearticle, null);
        final MaterialEditText password = v.findViewById(R.id.password);
        final ProgressBar progressBar = v.findViewById(R.id.progress);
        alertDialog.setView(v);
        alertDialog.setCancelable(true);


        alertDialog
                .setPositiveButton("LOG IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        final AlertDialog dialog = alertDialog.create();
        dialog.show();


        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(Common.isConnectedToInternet(getBaseContext())) {

                        if (password.getText().length() > 0) {
                            progressBar.setVisibility(VISIBLE);
                            password.setEnabled(false);
                            DocumentReference query = db.collection("EditorialTeam").document("Password");
                            query.get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                            if (documentSnapshot.get("lockkey").toString().equals(password.getText().toString())) {
                                                editor.putString("user", "Yes");
                                                editor.commit();
                                                dialog.dismiss();
                                                try {
                                                    hideSoftKeyboard(v);
                                                } catch (Exception e) {
                                                    // TODO: handle exception
                                                }
                                                startActivity(new Intent(getApplicationContext(), CreateArticle.class));
                                                overridePendingTransition(R.anim.fade_in, R.anim.fade_oute);
                                            } else {
                                                progressBar.setVisibility(GONE);
                                                password.setError("Invalid Password");
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            password.setError("Try again later");

                                        }
                                    });

                        } else {

                            password.setError("Fill me");

                        }

                }else{
                    password.setError("Check Internet Connection");
                }

                //Do stuff, possibly set wantToCloseDialog to true then...




                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you              set cancellable to false.
            }
        });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if                     you set cancellable to false.
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
            startActivity(new Intent(Articles.this, Home.class));
        }else if (id == R.id.add) {
            if (Common.isConnectedToInternet(getBaseContext())) {
                if(!sharedPreferences.getString("user","").equals("Yes")) {

                manualAdd();

                }else{
                    startActivity(new Intent(getApplicationContext(), CreateArticle.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_oute);
                }
            } else {

                displayMessage("Error:", "Check Internet Connection");
            }
        }

            //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
    public static void hideSoftKeyboard(View v) {
        InputMethodManager in = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(v.getApplicationWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
