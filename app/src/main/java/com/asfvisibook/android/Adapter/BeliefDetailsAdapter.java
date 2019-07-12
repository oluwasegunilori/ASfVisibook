package com.asfvisibook.android.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.asfvisibook.android.R;
import com.asfvisibook.android.database.Database;
import com.example.asfvisibook.Model.Belief;
import java.util.List;

public class BeliefDetailsAdapter extends PagerAdapter {
    Context context;
    List<Belief> beliefList;
    LayoutInflater inflater;

    public BeliefDetailsAdapter(Context context, List<Belief> belieflist) {
    this.context = context;
    this.beliefList = belieflist;
    inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return beliefList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        (container).removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = inflater.inflate(R.layout.beliefdetails_item, container, false);
        TextView title = view.findViewById(R.id.title);
        TextView details = view.findViewById(R.id.details);
        Button show = view.findViewById(R.id.showbibletext);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String bibletext =  new Database(context).getBibleText(position+1);
                displayMessage("Bible Text", bibletext);


            }
        });

        title.setText(beliefList.get(position).getTitle());
        details.setText(beliefList.get(position).getDetails());
        container.addView(view);
        return  view;
    }

    private void displayMessage(String title, String content){
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);

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
}
