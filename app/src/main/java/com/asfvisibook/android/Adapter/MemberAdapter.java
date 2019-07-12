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
import android.widget.Toast;

import com.asfvisibook.android.R;
import com.asfvisibook.android.UserDetails;
import com.example.asfvisibook.Model.Member;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MyViewHolder> implements SectionIndexer {
    private List<Member> memberList;
    private ArrayList<Integer> sectionPosition;
    private Context mcontex;
    public MemberAdapter(Context mcontex, List<Member> memberList) {
        this.mcontex = mcontex;
        this.memberList  = memberList;
    }

    @NonNull
    @Override
    public MemberAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mcontex);
        view = inflater.inflate(R.layout.member_item, viewGroup, false);
        return new MemberAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberAdapter.MyViewHolder viewHolder, final int i) {

        viewHolder.name.setText(memberList.get(i).getName());
        viewHolder.phoneNumber.setText(memberList.get(i).getPhoneno());
        viewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(memberList.get(i).getPhoneno().length() <7){
                    Toast.makeText(mcontex, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                }else {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + Uri.encode(memberList.get(i).getPhoneno())));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mcontex.startActivity(callIntent);
                }

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
