package com.asfvisibook.android;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asfvisibook.android.R;
import com.asfvisibook.android.common.Common;
import com.example.asfvisibook.Model.ArticleClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.VISIBLE;
import static com.asfvisibook.android.common.Common.CAMERA_REQUEST_CODE;
import static com.asfvisibook.android.common.Common.REQUEST_EXTERNAL_STORAGE;

public class CreateArticle extends AppCompatActivity {
@BindView(R.id.author)
    MaterialAutoCompleteTextView authorspinner;
@BindView(R.id.articledetails)
    MaterialEditText articledetails;
@BindView(R.id.transparentbackground)
    LinearLayout transparentbackground;
@BindView(R.id.title)
    MaterialEditText title;
    @BindView(R.id.uploadstatus)
    TextView uploadstatus;

    final List<String> namelist = new ArrayList<>();
    FirebaseFirestore  db = FirebaseFirestore.getInstance();
    Uri backgroundImage;
    FirebaseStorage storage;
    StorageReference storagereference;

    private static String[] PERMISSIONS_READ_EXTERNAL_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private static String[] PERMISSION_CAMERA =  {Manifest.permission.CAMERA,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_article);
        ButterKnife.bind(this);

        storage = FirebaseStorage.getInstance();
        storagereference = storage.getReference();
        loadTeam();

        authorspinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                authorspinner.showDropDown();
            }
        });

        authorspinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    hideSoftKeyboard(view);
                } catch (Exception e) {
                    // TODO: handle exception
                }



            }
        });


        authorspinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getX() >= (authorspinner.getWidth() - authorspinner.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if(authorspinner.isPopupShowing()){
                            authorspinner.dismissDropDown();
                        }else {
                            Drawable img = getApplicationContext().getResources().getDrawable( R.drawable.ic_arrow_drop_down_circle_black_24dp );
                            img.setBounds(0, 0, img.getMinimumWidth(),
                                    img.getMinimumHeight());
                            authorspinner.setCompoundDrawablesWithIntrinsicBounds(null, null,img, null );
                            authorspinner.showDropDown();
                        }

                        return true;
                    }
                }
                return false;
            }
        });


    }

    public static void hideSoftKeyboard(View v) {
        InputMethodManager in = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(v.getApplicationWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }


    private void loadTeam() {

            transparentbackground.setVisibility(View.VISIBLE);
        DocumentReference query  = db.collection("EditorialTeam").document("Team");

           query.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                   if (task.isSuccessful()) {
                       DocumentSnapshot documentSnapshot = task.getResult();
                       ArrayList<String> list = (ArrayList<String>) documentSnapshot.get("names");
                       for(Object o: list){
                           namelist.add(o.toString());
                        }
                       ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_dropdown,
                              namelist
                       );


                      authorspinner.setAdapter(adapter);
                       authorspinner.setThreshold(0);

                       transparentbackground.setVisibility(View.GONE);

                   }else{

                       transparentbackground.setVisibility(View.GONE);
                       Toast.makeText(getApplicationContext(), "Can't get Team List", Toast.LENGTH_SHORT);
                   }
               }
           });









    }

    public void createArticle(View view) {
    if(articledetails.getText().length()> 0 && authorspinner.getText().length() > 0 &&
            title.getText().length()>0 && backgroundImage != null) {
        if (Common.isConnectedToInternet(getBaseContext())) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            String timeformat = df2.format(cal.getTime());

            ArticleClass article = new ArticleClass(title.getText().toString(), articledetails.getText().toString(), authorspinner.getText().toString(), backgroundImage.toString(), timeformat);
             publishArticle(article);

        } else {
            displayMessage("Error:", "Please Check Your Internet Connection");
        }
    }
    else{

        if(articledetails.getText().length() == 0){
            articledetails.setError("Please fill me");
        }
        if(authorspinner.getText().length() == 0){
            authorspinner.setError("Please fill me");
        }
        if(title.getText().length() == 0){
            title.setError("Please fill me");
        }
        if(backgroundImage == null){
            displayMessage("Prompt", "Please choose an image");
        }
    }
    }

    private void publishArticle(final ArticleClass article) {
        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.setMessage("Uploading - - -");
        mDialog.show();
        mDialog.setCancelable(false);
        final String imagename = UUID.randomUUID().toString();
        final StorageReference imagefolder = storagereference.child("images/").child(imagename);
        imagefolder.putFile(backgroundImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mDialog.dismiss();
                Toast.makeText(CreateArticle.this, "Uploaded Image", Toast.LENGTH_SHORT).show();
                imagefolder.getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                mDialog.dismiss();
                                transparentbackground.setVisibility(View.VISIBLE);
                                article.setImage(uri.toString());
                                db.collection("Articles").document(article.getTitle())
                                        .set(article).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        transparentbackground.setVisibility(View.GONE);

                                       displayMessage("Success","Article has been published" );
                                        startActivity(new Intent(CreateArticle.this, Articles.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_oute);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        displayMessage("Error", e.toString());
                                    }
                                });



                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mDialog.dismiss();
                Toast.makeText(CreateArticle.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                mDialog.setMessage("Uploading Picture: " + progress + "%");

            }
        });


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

    public void chooseImage(View view) {

        AlertDialog.Builder alertdialog = new AlertDialog.Builder(CreateArticle.this);
        alertdialog.setTitle("This image shall be the background image for your article");
        alertdialog.setMessage("Choose Action");
        alertdialog.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (ActivityCompat.checkSelfPermission(CreateArticle.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    ActivityCompat.requestPermissions(
                            CreateArticle.this,
                            PERMISSION_CAMERA,
                            CAMERA_REQUEST_CODE
                    );
                }else{

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);

                }




            }
        });
        alertdialog.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (ActivityCompat.checkSelfPermission(CreateArticle.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    ActivityCompat.requestPermissions(
                            CreateArticle.this,
                            PERMISSIONS_READ_EXTERNAL_STORAGE,
                            REQUEST_EXTERNAL_STORAGE
                    );

                }else{

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), Common.PICK_IMAGE_REQUEST);

                }



            }
        });
        alertdialog.show();





    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData() !=null){
            backgroundImage = data.getData();
            uploadstatus.setText("Image Selected");
            uploadstatus.setVisibility(VISIBLE);



        }


        else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            backgroundImage = data.getData();
            uploadstatus.setText("Image Selected");
            uploadstatus.setVisibility(VISIBLE);
        }
        else{

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);


                } else {

                    displayMessage("Error", "Cannot use Camera without Permission");
                }
                return;
            }
            case REQUEST_EXTERNAL_STORAGE:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"), Common.PICK_IMAGE_REQUEST);


                } else {

                    displayMessage("Error", "Cannot Access Storage without permission");

                }
                return;
            }

            }



            // other 'case' lines to check for other
            // permissions this app might request
        }


    }

