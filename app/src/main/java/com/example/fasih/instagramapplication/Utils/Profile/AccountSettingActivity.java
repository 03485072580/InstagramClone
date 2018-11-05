package com.example.fasih.instagramapplication.Utils.Profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fasih.instagramapplication.R;
import com.example.fasih.instagramapplication.Utils.Utils.ClickRecyclerViewItem;
import com.example.fasih.instagramapplication.Utils.Utils.FirebaseMethods;
import com.example.fasih.instagramapplication.Utils.Utils.SectionsPagerStateAdapter;

import java.util.ArrayList;

public class AccountSettingActivity extends AppCompatActivity implements ClickRecyclerViewItem {
    SectionsPagerStateAdapter adapter;
    ViewPager viewPager;
    private RelativeLayout relLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        setUpAccountList();
        setUpBackNavigation();
        setUpAccountListFragments();
        getIncomingIntent();
    }

    private void setUpAccountListFragments() {
        adapter = new SectionsPagerStateAdapter(getSupportFragmentManager());
        adapter.addFragment(new EditProfileFragment(), getString(R.string.edit_profile));
        adapter.addFragment(new SignOutFragment(), getString(R.string.sign_out));

    }

    private void setUpBackNavigation() {
        ImageView imageView = findViewById(R.id.backArrow);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.fade_in_nav, R.anim.fade_out_nav);
                finish();
            }
        });
    }

    private void setUpAccountList() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final ArrayList arrayList = new ArrayList();
        arrayList.add(getString(R.string.edit_profile));
        arrayList.add(getString(R.string.sign_out));
        recyclerView.setAdapter(new RecyclerView.Adapter<MyViewHolder>() {

            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                MyViewHolder holder = new MyViewHolder(AccountSettingActivity.this
                        .getLayoutInflater()
                        .inflate(R.layout.account_settings_single_row, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(MyViewHolder holder, int position) {
                holder.textView.setText((String) arrayList.get(position));

            }

            @Override
            public int getItemCount() {
                return arrayList.size();
            }
        });
    }

    @Override
    public void onClick(View view, int position) {
        setUpFragment(position);
    }

    public void setUpFragment(int upFragment) {
        viewPager = findViewById(R.id.container);
        relLayout = findViewById(R.id.relLayout);
        relLayout.setVisibility(View.GONE);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(upFragment);

    }

    public int getTask() {
        if(getIntent()!=null){
            getIntent().getFlags();
        }
        return 0;
    }

    public void getIncomingIntent() {
        try {
            Intent intent = getIntent();
            if (intent.hasExtra(getString(R.string.calling_activity))) {
                setUpFragment(adapter.getFragmentNumber(getString(R.string.edit_profile)));
            }
            if (intent.hasExtra(getString(R.string.return_to_fragment_editprofile))) {
                String imgUrl=intent.getStringExtra(getString(R.string.selected_gallery_image));
                FirebaseMethods firebaseMethods=new FirebaseMethods(AccountSettingActivity.this);
                firebaseMethods.addNewPhoto(getString(R.string.profile_photo),null,0,imgUrl,null);
                setUpFragment(adapter.getFragmentNumber(getString(R.string.edit_profile)));
            }
            if(intent.hasExtra(getString(R.string.from_fragment_photo))){
                Bitmap bm=intent.getParcelableExtra(getString(R.string.camera_bitmap));
                FirebaseMethods firebaseMethods=new FirebaseMethods(AccountSettingActivity.this);
                firebaseMethods.addNewPhoto(getString(R.string.profile_photo),null,0,null,bm);
                setUpFragment(adapter.getFragmentNumber(getString(R.string.edit_profile)));
            }

        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ClickRecyclerViewItem item;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = AccountSettingActivity.this;
            textView = itemView.findViewById(R.id.account_settings_row);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.onClick(v, getAdapterPosition());

                }
            });
        }
    }
}
