package com.gharpeshiksha.tutorapp.localdb;

import android.provider.BaseColumns;

import com.google.gson.annotations.SerializedName;

public class Contract {
    //Contract class which contains URIs Table schema and db name, version constants
    public static final String DB_NAME = "tutorDB";
    public static final int DB_VERSION = 2;

    //Local class for classesforme api response
    public static class ClassesForMeAPI implements BaseColumns {
        public static final String TABLE_NAME = "ClassesForMeApi";
        public static final String ENQ_ID = "enqId";
        public static final String SOFT_TTL = "softTimeStamp";
        public static final String TEXT_MIN = "textMin";
        public static final String TEXT_VIEWS = "textViews";
        public static final String TEXT_NAME = "textName";
        public static final String TEXT_TUTOR_REQUIREMENT = "textTutorReq";
        public static final String TEXT_BUDGET = "textBudget";
        public static final String TEXT_LOC = "textLoc";
        public static final String FAVORITE = "favorite";
        public static final String TEXT_DIS = "textDes";
        public static final String STATUS = "status";
        public static final String PAYMENT_STATUS = "paymentStatus";
        public static final String ENQ_VIEWED = "enqViewed";
        public static final String FEEDBACK = "feedback";
        public static final String HIGH_COMP = "highComp";
        public static final String FREE_CLASS = "freeClass";
        public static final String STUDENT_UUID = "studentUUId";
        public static final String TUTOR_UUID = "tutorUUId";
        public static final String PIC_URL = "picUrl";
    }

    /**Below is classes table */
    public static class ClassesTable {
        public static final String TABLE_NAME = "classesTable";
        public static final String CLASSES_NAME = "className";
        public static final String CLASS_PIC_URL = "classPicUrl";
        public static final String FILTER_CLS = "filterCls";
        public static final String SOFT_TTL = "softTTL";
    }

    /**Below is ArchivedTable */
    public static class ArchivedTable implements BaseColumns {
        public static final String TABLE_NAME = "archivedTable";
        public static final String area = "area";
        public static final String studentUUID = "studentUUId";
        public static final String distance = "distance";
        public static final String subjects = "subjects";
        public static final String tutorUUID = "tutorUUId";
        public static final String enq_id = "enq_id";
        public static final String op_count = "op_count";
        public static final  String highComp1 = "highComp";
        public static final String freeCla = "freeClass";
        public static final String name = "name";
        public static final String paymentStatus = "payStatus";
        public static final String tutors_con = "tutorCon";
        public static final String time = "time";
        public static final String favorite = "favorite";
        public static final String classfor = "classFor";
        public static final  String budget = "budget";
        public static final  String status1 = "status";
        public static final String enq_viewed = "enqViewed";
        public static final  String cursor = "cursor";
        public static final  String timestamp = "timestamp";
        public static final String sofTTL = "softTTL";
    }

    /**Below is favorite table */
    public static class FavoriteTable implements BaseColumns {
        public static final String TABLE_NAME = "favoriteTable";
        public static final String ENQ_ID = "enqId";
        public static final String TIME = "time";
        public static final String TUTOR_CON = "tutorCon";
        public static final String NAME = "name";
        public static final String TITLE = "title";
        public static final String BUDGET = "budget";
        public static final String UTF_8STR = "utfStr";
        public static final String FAVORITE = "favorite";
        public static final String DISTANCE = "distance";
        public static final String STATUS = "status";
        public static final String CHECK_STATUS = "checkStatus";
        public static final String ENQ_VIEWED = "enqViewed";
        public static final String FEEDBACK = "feedback";
        public static final String HIGH_COMP = "highComp";
        public static final String FREE_CLASS = "freeCls";
        public static final String STUDENT_UUID = "studentUuid";
        public static final String TUTOR_UUID = "tutorUuid";
        public static final String PIC_URL = "picUrl";
        public static final String SOFT_TTL = "softTL";
    }

    /**Below is Written Testimonials table. */
    public static class WrittenTestimonial implements BaseColumns {
        public static final String TABLE_NAME = "writtenTestimonialsTable";
        public static final String PIC_URL = "picUrl";
        public static final String DESC = "desc";
        public static final String TUTOR_ID = "tutorId";
        public static final String TUTOR_NAME = "tutorName";
        public static final String SOFT_TTL = "softTL";
    }

    public static class VideoTestimonial implements BaseColumns {
        public static final String TABLE_NAME = "videoTable";
        public static final String SOFT_TTL = "softTL";
        public static final String YOUTUBE_URL = "youtubeUrl";
        public static final String DESC = "desc";
        public static final String VIDEO_ID = "videoId";
        public static final String IMG_URL = "imgUrl";
    }

    public static class BasicInfo implements BaseColumns {
        public static final String TABLE_NAME = "basicInfoTable";
        public static final String PLAN_DETAIL = "planDet";
        public static final String NAME = "name";
        public static final String ABOUT = "about";
        public static final String PIC_URL = "picUrl";
        public static final String TUT_ID = "tutId";
        public static final String EMAIL = "email";
        public static final String STATUS = "status";
        public static final String SOFT_TL = "softTL";
        public static final String REMAINING_VIEWS = "remainingViews";
    }

    public static class DisplayChats implements BaseColumns {
        public static final String TABLE_NAME = "displayChatsTable";
        public static final String PIC_URL = "picUrl";
        public static final String STUDENT_UUID = "studentUUId";
        public static final String STUDENT_NAME = "studentName";
        public static final String TUTOR_UUId = "tutorUUId";
        public static final String MESSAGE = "message";
        public static final String TIMESTAMP = "timestamp";
        public static final String SOFT_TL = "softTL";
        public static final String TIMESTAMP_IN_MILLIS = "timestampInMilli";
        public static final String ENQ_ID = "enqId";
        public static final String IS_APPROVED = "isApproved";
        public static final String SEND_BY = "sendBy";
    }
}
