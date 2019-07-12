package com.asfvisibook.android.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asfvisibook.android.BeliefDetails;
import com.asfvisibook.android.R;
import com.example.asfvisibook.Model.Belief;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
import static com.asfvisibook.android.common.Common.SHARED_PREFERENCES_FILE_NAME;

public class BeliefAdapter  extends RecyclerView.Adapter<BeliefAdapter.MyViewHolder>{
    private List<Belief> beliefList;
    private Context mcontex;
    public BeliefAdapter(Context mcontex, List<Belief> beliefList) {
        this.mcontex = mcontex;
        this.beliefList  = beliefList;
    }

    @NonNull
    @Override
    public BeliefAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mcontex);
        view = inflater.inflate(R.layout.belief_item, viewGroup, false);
        return new BeliefAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BeliefAdapter.MyViewHolder viewHolder, final int i) {

        viewHolder.id.setText(String.valueOf(beliefList.get(i).getId()));
        viewHolder.title.setText(beliefList.get(i).getTitle());
        viewHolder.bibletext.setText(beliefList.get(i).getBible_verse());
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = mcontex.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("id", i);
                editor.commit();
                mcontex.startActivity(new Intent(mcontex, BeliefDetails.class));

            }
        });


    }

    @Override
    public int getItemCount() {
        return beliefList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder  {
        @BindView(R.id.number)
        public TextView id;
        @BindView(R.id.belieftitle)
        public TextView title;
        @BindView(R.id.bibletext)
        public TextView bibletext;
        @BindView(R.id.container)
        public LinearLayout container;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }

}

