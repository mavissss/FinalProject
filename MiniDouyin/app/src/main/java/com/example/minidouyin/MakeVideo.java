package com.example.minidouyin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.minidouyin.bean.Feed;
import com.example.minidouyin.bean.FeedResponse;
import com.example.minidouyin.bean.PostVideoResponse;
import com.example.minidouyin.network.IMiniDouyinService;
import com.example.minidouyin.utils.ResourceUtils;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MakeVideo extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final int PICK_VIDEO = 2;
    private static final int UP_VIDEO=3;
    private static final int BACK=4;
    private static final int GRANT_PERMISSION = 3;
    private Uri videoUri;
    private Uri imageUri;
    private static final String TAG = "Solution2C2Activity";
    private List<Feed> mFeeds = new ArrayList<>();

    public Button mBtn;
    public VideoView videoView;
    public ImageView imageView;
    public int btnFlag;

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
        setContentView(R.layout.activity_select_video);
        mBtn=findViewById(R.id.nextToSelectPicture_btn);
        btnFlag=PICK_VIDEO;
        videoView=findViewById(R.id.videoView);
        imageView=findViewById(R.id.img);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnFlag==PICK_VIDEO){

                    chooseVideo();
                }

                else if(btnFlag==PICK_IMAGE){
                    choodeImage();

                }
                else if(btnFlag==UP_VIDEO)
                {
                    postVideo();
                }
                else if(btnFlag==BACK)
                {
                    startActivity(new Intent(MakeVideo.this, HomeActivity.class));
                }
            }
        });
    }

    private void postVideo() {
        mBtn.setText("上传中");
        //mBtn.setEnabled(false);

        // TODO-C2 (6) Send Request to post a video with its cover image
        // if success, make a text Toast and show
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("http://test.androidcamp.bytedance.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IMiniDouyinService iMiniDouyinService=retrofit.create(IMiniDouyinService.class);
        Call<PostVideoResponse> postVideoResponseCall=iMiniDouyinService.createVideo("1120161971","zls",
                getMultipartFromUri("cover_image",imageUri),getMultipartFromUri("video",videoUri));
        postVideoResponseCall.enqueue(new Callback<PostVideoResponse>() {
            @Override
            public void onResponse(Call<PostVideoResponse> call, Response<PostVideoResponse> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(MakeVideo.this, "上传成功！", Toast.LENGTH_SHORT).show();
                    mBtn.setText("上传成功，返回首页");
                    mBtn.setEnabled(true);
                    btnFlag=BACK;
                    if(response.body().getItem()!=null){
                        Log.d(TAG, "#############成功############" );
                    }
                }
                else
                {
                    Toast.makeText(MakeVideo.this,"上传失败",Toast.LENGTH_SHORT).show();
                    mBtn.setText("上传失败 点击重新选择视频");
                    mBtn.setEnabled(true);
                    btnFlag=PICK_VIDEO;
                }
            }

            @Override
            public void onFailure(Call<PostVideoResponse> call, Throwable t) {
                Toast.makeText(MakeVideo.this,"上传失败",Toast.LENGTH_SHORT).show();
                mBtn.setText("上传失败 点击重新选择视频");
                Log.d(TAG, "onFailure: 失败");
                mBtn.setEnabled(true);
                btnFlag=PICK_VIDEO;
            }
        });
    }

    public void fetchFeed(View view) {


        // TODO-C2 (9) Send Request to fetch feed
        // if success, assign data to mFeeds and call mRv.getAdapter().notifyDataSetChanged()
        // don't forget to call resetRefreshBtn() after response received
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("http://test.androidcamp.bytedance.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IMiniDouyinService iMiniDouyinService=retrofit.create(IMiniDouyinService.class);
        Call<FeedResponse> feedResponseCall=iMiniDouyinService.fetchFeed();
    }



    private MultipartBody.Part getMultipartFromUri(String name, Uri uri) {
        // if NullPointerException thrown, try to allow storage permission in system settings
        File f = new File(ResourceUtils.getRealPath(MakeVideo.this, uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }

    public void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO);

    }

    public void choodeImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            //todo 显示选择的图片
            imageUri=intent.getData();
            imageView.setImageURI(imageUri);
            mBtn.setText("点击上传");
            btnFlag=UP_VIDEO;

        }

        else if (requestCode == PICK_VIDEO && resultCode == RESULT_OK) {
            //todo 播放选择的视频
            videoUri=intent.getData();
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(videoUri);
            videoView.start();
            mBtn.setText("选择封面图");
            btnFlag=PICK_IMAGE;
        }
    }

}
