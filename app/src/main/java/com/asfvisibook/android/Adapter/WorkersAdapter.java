package com.asfvisibook.android.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.asfvisibook.android.R;
import com.example.asfvisibook.Model.Excos;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class WorkersAdapter extends RecyclerView.Adapter<WorkersAdapter.MyViewHolder> implements SectionIndexer {
    private List<Excos> workerList;
    private ArrayList<Integer> sectionPosition;
    private Context mcontex;
    public WorkersAdapter(Context mcontex, List<Excos> workerList) {
        this.mcontex = mcontex;
        this.workerList  = workerList;
    }

    @NonNull
    @Override
    public WorkersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mcontex);
        view = inflater.inflate(R.layout.excos_item, viewGroup, false);
        return new WorkersAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkersAdapter.MyViewHolder viewHolder, final int i) {

        viewHolder.name.setText(workerList.get(i).getName());
        viewHolder.phoneNumber.setText(workerList.get(i).getPhoneno());
        viewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+Uri.encode(workerList.get(i).getPhoneno())));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontex.startActivity(callIntent);



            }
        });

        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return workerList.size();
    }

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>(26);
        sectionPosition = new ArrayList<>(26);
        for (int i = 0, size = workerList.size(); i < size; i++) {
            String section = String.valueOf(workerList.get(i).getName().charAt(0)).toUpperCase();
            if (!sections.contains(section)) {
                sections.add(section);
                sectionPosition.add(i);
            }
        }
        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return  sectionPosition.get(sectionIndex);
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder  {
        @BindView(R.id.name)
        public TextView name;
        @BindView(R.id.phone)
        public TextView phoneNumber;
        @BindView(R.id.call)
        public CircleImageView call;
        @BindView(R.id.container)
        public LinearLayout container;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }

}
