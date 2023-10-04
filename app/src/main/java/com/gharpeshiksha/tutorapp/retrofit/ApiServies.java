package com.gharpeshiksha.tutorapp.retrofit;

import com.gharpeshiksha.tutorapp.data_model.Classes_Model;
import com.gharpeshiksha.tutorapp.data_model.Model_Chats;
import com.gharpeshiksha.tutorapp.data_model.Model_Imonials;
import com.gharpeshiksha.tutorapp.data_model.Model_archived;
import com.gharpeshiksha.tutorapp.data_model.SendMessage_Model;
import com.gharpeshiksha.tutorapp.data_model.SpinnerModel;
import com.gharpeshiksha.tutorapp.data_model.Students_Chat_Models;
import com.gharpeshiksha.tutorapp.data_model.Subject_Class_Model;
import com.gharpeshiksha.tutorapp.data_model.TeacherDetailsProfile;
import com.gharpeshiksha.tutorapp.data_model.Time_Set_Models;
import com.gharpeshiksha.tutorapp.data_model.Written_Testimonial;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiServies {

    @GET("/Tutor/flipperImageHome")
    Call<List<String>> getSlider();

    @GET("/Tutor/writtenTestimonial")
    Call<List<Written_Testimonial>> getTestimo();

    @GET("/Tutor/videoTestimonial")
    Call<List<Model_Imonials>> getVideoTestimonial();

    @FormUrlEncoded
    @POST("/Tutor/viewTeacherProfile")
    Call<List<TeacherDetailsProfile>> TeacherDetails(
            @Field("phone") String phone
    );

    //Chats Content End Point
    @FormUrlEncoded
    @POST("/Chats/tutorOpenChat")
    Call<List<Students_Chat_Models>> ChatOpen(
            @Field("studentUUID") String studentUUID,
            @Field("tutorUUID") String tutorUUID
    );
    @FormUrlEncoded
    @POST("/Chats/tutorSendMessage")
    Call<SendMessage_Model> SendMessages(
            @Field("sendBy") String sendBy,
            @Field("message") String message,
            @Field("studentUUID") String studentUUID,
            @Field("tutorUUID") String tutorUUID,
            @Field("enqId") String enqId
    );

    @FormUrlEncoded
    @POST("/Chats/tutorDisplayChats")
    Call<List<Model_Chats>> DidplayChat(
            @Field("phone") String phone, @Field("timestamp") String timestamp
    );

    //Time set API
    @FormUrlEncoded
    @POST("/Tutor/deleteAllotment")
    Call<Time_Set_Models> deleteAllotmentTime(
            @Field("timestamp") String timestamp

    );

    @FormUrlEncoded
    @POST("/Tutor/editAllotment")
    Call<Time_Set_Models> EditAllotmentTime(
            @Field("timestamp") String timestamp,
            @Field("day") String day,
            @Field("opening") String opening,
            @Field("closing") String closeing,
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST("/Tutor/addAllotment")
    Call<Time_Set_Models> addAllotmentTime(
            @Field("phone") String phone,
            @Field("day") String day,
            @Field("opening") String opening,
            @Field("closing") String closing

    );

    @FormUrlEncoded()
    @POST("/Tutor/viewAllotment")
    Call<List<SpinnerModel>> ViewAllotment(
            @Field("phone") long phone
    );

    //Classes and Subject End Point
    @GET("/Tutor/tutorClasses")
    Call<List<Classes_Model>> StudentsClass();
    @GET("/Tutor/tutorSubject")
    Call<List<Subject_Class_Model>> StudentsSubject();

    //Below API method is to get the Plan details
    @FormUrlEncoded
    @POST("Tutor/GetProfile")
    Call<JsonObject> getPlan(
            @Field("phone") String phone
    );

    //Archived class api
    @FormUrlEncoded
    @POST("/GetClasses/classesformeArchivedTestNew")
    Call<List<Model_archived>> classesformeArchived(
            @Field("phone") long phone,
            @Field("sort_by") String recency,
            @Field("cursorStr") String cursorStr
    );

    /**Below api to get video url
     * @param videoId it is endpoint of video */
    @POST("Tutor/play_video_testimonial")
    @FormUrlEncoded
    Call<ResponseBody> getVideoTestimonials(@Field("video_id") String videoId);

    /**Below api to update chat approval status
     * @param isApproved status to approve current ongoing chat */
    @POST("Chats/chatApproval")
    @FormUrlEncoded
    Call<ResponseBody> updateChatApproval(
            @Field("studentUUID") String studentUUID,
            @Field("tutorUUID") String tutorUUID,
            @Field("isApproved") String isApproved);

    /**Below method to get Google Map Api Key */
    @POST("/Tutor/common_config")
    @FormUrlEncoded
    Call<ResponseBody> getMapApiKey();

    @POST("GetClasses/enqinfoTest")
    @FormUrlEncoded
    Call<ResponseBody> getContactNum(@Field("phone") String phone, @Field("enq_id") String enqId);
}
