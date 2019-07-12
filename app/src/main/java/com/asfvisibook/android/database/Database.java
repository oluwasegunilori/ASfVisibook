package com.asfvisibook.android.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.asfvisibook.Model.Belief;
import com.example.asfvisibook.Model.Excos;
import com.example.asfvisibook.Model.Member;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SHEGZ on 1/1/2018.
 */
public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "asfvisibook.sqlite";
    private static final int DB_VERSION = 1;


    public Database(Context context) {



        super(context, DB_NAME, null, DB_VERSION);
    }


    public List<Belief>  getBeliefs(){
        List<Belief> details = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM  tb_adventistbeliefs");
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        if(cursor.moveToFirst())

        {
            do{
                details.add(new Belief(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("title")),
                        cursor.getString(cursor.getColumnIndex("details")), cursor.getString(cursor.getColumnIndex("Bible_Verse"))));
            }while(cursor.moveToNext());


        }


        cursor.close();
        return details;
    }

    public List<Member>  getAllMembers (){
        List<Member> details = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM  Visibook ORDER BY name ASC");
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        if(cursor.moveToFirst())

        {
            do{
                details.add(new Member(cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("sex")),
                        cursor.getString(cursor.getColumnIndex("dateofbirth")),
                        cursor.getString(cursor.getColumnIndex("phoneno")),
                        cursor.getString(cursor.getColumnIndex("email")),
                        cursor.getString(cursor.getColumnIndex("faculty")),
                        cursor.getString(cursor.getColumnIndex("department")),
                        cursor.getString(cursor.getColumnIndex("address")),
                        cursor.getString(cursor.getColumnIndex("part")),
                        cursor.getString(cursor.getColumnIndex("postheld"))));
            }while(cursor.moveToNext());


        }


        cursor.close();
        return details;
    }

    public Member  getMember (String name){
        Member member = new Member();
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM  Visibook where name = '%s'", name);
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        if(cursor.moveToFirst())

        {
            do{
                member = new Member(cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("sex")),
                        cursor.getString(cursor.getColumnIndex("dateofbirth")),
                        cursor.getString(cursor.getColumnIndex("phoneno")),
                        cursor.getString(cursor.getColumnIndex("email")),
                        cursor.getString(cursor.getColumnIndex("faculty")),
                        cursor.getString(cursor.getColumnIndex("department")),
                        cursor.getString(cursor.getColumnIndex("address")),
                        cursor.getString(cursor.getColumnIndex("part")),
                        cursor.getString(cursor.getColumnIndex("postheld")));
            }while(cursor.moveToNext());


        }


        cursor.close();
        return member;
    }


    public List<Excos>  getExcos(){
        List<Excos> details = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM  Visibook where postheld != '%s'" , "Nil");
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        if(cursor.moveToFirst())

        {
            do{

                details.add(new Excos(cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("sex")),
                        cursor.getString(cursor.getColumnIndex("dateofbirth")),
                        cursor.getString(cursor.getColumnIndex("phoneno")),
                        cursor.getString(cursor.getColumnIndex("email")),
                        cursor.getString(cursor.getColumnIndex("faculty")),
                        cursor.getString(cursor.getColumnIndex("department")),
                        cursor.getString(cursor.getColumnIndex("address")),
                        cursor.getString(cursor.getColumnIndex("part")),
                        cursor.getString(cursor.getColumnIndex("postheld"))));
            }while(cursor.moveToNext());

        }


        cursor.close();
        return details;
    }





    public String getBibleText(int id){
        String bibletext = "";
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM  tb_adventistbeliefs where id = '%d'", id);
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        if(cursor.moveToFirst())

        {
            bibletext =cursor.getString(cursor.getColumnIndex("Bible_Verse"));

        }


        cursor.close();
        return bibletext;
    }










}
