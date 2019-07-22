package com.example.minidouyin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.minidouyin.bean.Feed;
import com.example.minidouyin.bean.FeedResponse;
import com.example.minidouyin.network.IMiniDouyinService;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;




public class HomeActivity extends AppCompatActivity {

    private List<Feed> mFeeds=new ArrayList<>();

    private Button mBtnRefresh;
    public RecyclerView mRv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
            getWindow().setFlags
                    (
                            WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN
                    );
        }
        setContentView(R.layout.activity_home);
        findViewById(R.id.makevideo_btn).setOnClickListener(v -> {
            //todo 在这里申请相机、麦克风、存储的权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);
            startActivity(new Intent(HomeActivity.this, RecordActivity.class));
        });
        mBtnRefresh = findViewById(R.id.btn_refresh);
        initRecyclerView();

    }

    private void initRecyclerView() {
        mRv = findViewById(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(new RecyclerView.Adapter() {
            @NonNull @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                ImageView imageView = new ImageView(viewGroup.getContext());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageView.setAdjustViewBounds(true);
                return new MyViewHolder(imageView);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                ImageView iv = (ImageView) viewHolder.itemView;


                String url = mFeeds.get(i).getImage_url();
                Glide.with(iv.getContext()).load(url).into(iv);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String videoUrl=mFeeds.get(i).getVideo_url();
                        if(videoUrl==null)
                        {
                            Toast.makeText(HomeActivity.this,"视频不存在，请重试",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Intent playIntent=new Intent(HomeActivity.this,VideoPlay.class);
                            playIntent.putExtra("videoUrl",videoUrl);
                            startActivity(playIntent);
                        }
                    }
                });
            }

            @Override public int getItemCount() {
                return mFeeds.size();
            }
        });
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void fetchFeed(View view) {
        mBtnRefresh.setText("刷新中");
        mBtnRefresh.setEnabled(false);


        // if success, assign data to mFeeds and call mRv.getAdapter().notifyDataSetChanged()
        // don't forget to call resetRefreshBtn() after response received
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("http://test.androidcamp.bytedance.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IMiniDouyinService iMiniDouyinService=retrofit.create(IMiniDouyinService.class);
        Call<FeedResponse> feedResponseCall=iMiniDouyinService.fetchFeed();
        feedResponseCall.enqueue(new Callback<FeedResponse>() {
            @Override
            public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                resetRefreshBtn();
                if(response.isSuccessful()){
                    mFeeds= response.body().getFeeds();
                    mRv.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<FeedResponse> call, Throwable t) {
                resetRefreshBtn();
            }
        });
    }
    private void resetRefreshBtn() {
        mBtnRefresh.setText("刷新");
        mBtnRefresh.setEnabled(true);
    }
}
