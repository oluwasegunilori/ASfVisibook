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
import com.asfvisibook.android.UserDetails;
import com.example.asfvisibook.Model.Excos;
import com.example.asfvisibook.Model.Member;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ExcosAdapter extends RecyclerView.Adapter<ExcosAdapter.MyViewHolder> implements SectionIndexer {
    private List<Excos> memberList;
    private ArrayList<Integer> sectionPosition;
    private Context mcontex;
    public ExcosAdapter(Context mcontex, List<Excos> memberList) {
        this.mcontex = mcontex;
        this.memberList  = memberList;
    }

    @NonNull
    @Override
    public ExcosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mcontex);
        view = inflater.inflate(R.layout.excos_item, viewGroup, false);
        return new ExcosAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcosAdapter.MyViewHolder viewHolder, final int i) {

        viewHolder.name.setText(memberList.get(i).getName());
        viewHolder.phoneNumber.setText(memberList.get(i).getPost());
        viewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+Uri.encode(memberList.get(i).getPhoneno())));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontex.startActivity(callIntent);


            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontex, UserDetails.class);
                intent.putExtra("name", memberList.get(i).getName()) ;
                mcontex.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>(26);
        sectionPosition = new ArrayList<>(26);
        for (int i = 0, size = memberList.size(); i < size; i++) {
            String section = String.valueOf(memberList.get(i).getName().charAt(0)).toUpperCase();
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
