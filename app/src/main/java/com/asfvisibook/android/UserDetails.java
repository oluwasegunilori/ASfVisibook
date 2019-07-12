package com.asfvisibook.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.asfvisibook.android.database.Database;
import com.example.asfvisibook.Model.Member;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDetails extends AppCompatActivity {
@BindView(R.id.name)
    MaterialEditText name;
    @BindView(R.id.phonenumber)
    MaterialEditText phonenumber;
    @BindView(R.id.postheld)
    MaterialEditText post;
    @BindView(R.id.part)
    MaterialEditText part;
    @BindView(R.id.address)
    MaterialEditText address;
    @BindView(R.id.faculty)
    MaterialEditText faculty;
    @BindView(R.id.department)
    MaterialEditText department;
    @BindView(R.id.email)
    MaterialEditText email;
    @BindView(R.id.birthday)
    MaterialEditText birthday;
    @BindView(R.id.sex)
    MaterialEditText sex;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    String username;
    String[] months = {"January", "February", "March", "April", "May", "June","July", "August", "September", "October", "November" , "December"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
        loadView();
    }

    private void loadView() {
       username =  getIntent().getStringExtra("name");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(username);

        Member member = new Database(this).getMember(username);
        name.setText(username);
        email.setText(member.getEmail());
        address.setText(member.getAddress());
        phonenumber.setText(member.getPhoneno());
        part.setText(member.getPart());
        department.setText(member.getDepartment());
        faculty.setText(member.getFaculty());
        post.setText(member.getPost());
        sex.setText(member.getSex());
        if(member.getDateofbirth().contains("/")) {
            String[] details = member.getDateofbirth().split("/");
            try{
                birthday.setText(details[0] + "th of "+months[Integer.parseInt(details[1])]);
            }catch(NumberFormatException e){
                birthday.setText(member.getDateofbirth());
            }

        }
        else{
            birthday.setText(member.getDateofbirth());
        }


    }

    public void message(View view) {
        if(phonenumber.getText().toString().equals("Nil")) {
            displayMessage("Error", "Invalid Phone Number");

        }
        else{

            Uri uri = Uri.parse("smsto:"+ phonenumber.getText().toString());
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra("sms_body", "");
            startActivity(intent);

        }

    }

    public void call(View view) {
        if(phonenumber.getText().toString().equals("Nil")) {
            displayMessage("Error", "Invalid Phone Number");

        }
        else{
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+Uri.encode(phonenumber.getText().toString())));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(callIntent);


        }

    }

    public void email(View view) {
        if(email.getText().toString().equals("nil")) {

            displayMessage("Error", "Invalid Email");

        }
        else{

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", email.getText().toString(), null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));

        }
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==android.R.id.home){
            super.onBackPressed();
        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

}
