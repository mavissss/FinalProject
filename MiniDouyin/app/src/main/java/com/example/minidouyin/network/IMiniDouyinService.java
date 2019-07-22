package com.example.minidouyin.network;





import com.example.minidouyin.bean.FeedResponse;
import com.example.minidouyin.bean.PostVideoResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * @author Xavier.S
 * @date 2019.01.17 20:38
 */
public interface IMiniDouyinService {


    @Multipart
    @POST("mini_douyin/invoke/video")
    Call<PostVideoResponse> createVideo(
            @Query("student_id") String student_id,
            @Query("user_name") String user_name,
            @Part MultipartBody.Part image,@Part MultipartBody.Part video);


    @GET("mini_douyin/invoke/video")
    Call<FeedResponse> fetchFeed();
}
