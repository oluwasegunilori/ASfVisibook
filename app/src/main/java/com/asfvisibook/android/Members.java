package com.asfvisibook.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.asfvisibook.android.Adapter.MemberAdapter;
import com.asfvisibook.android.database.Database;
import com.example.asfvisibook.Model.Member;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

public class Members extends AppCompatActivity {
    List<Member> memberList;
    MemberAdapter adapter;
    @BindView(R.id.member_recycler)
    IndexFastScrollRecyclerView memberRecyclerView ;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        ButterKnife.bind(this);
        loadContent();
    }

    private void loadContent() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Members");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        memberList = new Database(this).getAllMembers();
        adapter = new MemberAdapter(this, memberList);
        memberRecyclerView.setAdapter(adapter);
        memberRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        memberRecyclerView.setIndexTextSize(12);
        memberRecyclerView.setIndexBarColor("#33334c");
        memberRecyclerView.setIndexBarCornerRadius(0);
        memberRecyclerView.setIndexBarTransparentValue((float) 0.4);
        memberRecyclerView.setIndexbarMargin(0);
        memberRecyclerView.setIndexbarWidth(40);
        memberRecyclerView.setPreviewPadding(0);
        memberRecyclerView.setIndexBarTextColor("#FFFFFF");

        memberRecyclerView.setIndexBarVisibility(true);
        memberRecyclerView.setIndexbarHighLateTextColor("#33334c");
        memberRecyclerView.setIndexBarHighLateTextVisibility(true);

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
