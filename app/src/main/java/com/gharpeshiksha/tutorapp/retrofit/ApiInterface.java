package com.gharpeshiksha.tutorapp.retrofit;

import com.gharpeshiksha.tutorapp.data_model.AllIndiaModel;
import com.gharpeshiksha.tutorapp.data_model.ClassesEnquiryModel;
import com.gharpeshiksha.tutorapp.data_model.OtoPojo;
import com.gharpeshiksha.tutorapp.data_model.StudentDetailsModel;

import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    //Call interface type method which returns OtpPojo class type response
    @POST("TutorLogin/sendotp")
    @FormUrlEncoded
    Call<String> getOtp(@Field("phone") String phone);

    /**Api for student details*/
    @POST("Chats/viewStudentDetail")
    @FormUrlEncoded
    Call<List<Object>> getStudentDetail(@Field("studentUUID") String studentUUId, @Field("tutorUUID")
                                        String tutorUUId, @Field("enqId") String enqId);

    /**Below method is for post type api call to get allIndia data in pagination
     * @param phone user phone number
     * @param classes user classes
     * @param cursorStr It is use for pagination*/
    @POST("GetClasses/classesformeFilteredAllIndiaTestNew")
    @FormUrlEncoded
    Call<List<AllIndiaModel>> getAllIndiaData(@Field("phone") String phone, @Field("classes") String classes,
                                              @Field("cursorStr") String cursorStr);

    /**Below method is for post type api call to get archived data in pagination
     * @param phone user phone number
     * @param sortBy recency
     * @param cursorStr It is use for pagination*/
    @POST("GetClasses/classesformeArchivedTestNew")
    @FormUrlEncoded
    Call<ResponseBody> getArchivedData(@Field("phone") String phone, @Field("sort_by") String sortBy,
                                       @Field("cursorStr") String cursorStr);

    /**Below method to get class filter by enquiryId parameter
     * @phone tutor phone number
     * @enqId enquiryId of class*/
    @POST("GetClasses/filterByEnqId")
    @FormUrlEncoded
    Call<ClassesEnquiryModel> getClassByEnqId(@Field("phone") long phone, @Field("enqId") String enqId);
}
