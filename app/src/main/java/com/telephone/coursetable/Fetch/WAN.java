package com.telephone.coursetable.Fetch;

import android.content.Context;
import android.content.res.Resources;

import com.telephone.coursetable.Https.Get;
import com.telephone.coursetable.Https.GetBitmap;
import com.telephone.coursetable.Http.HttpConnectionAndCode;
import com.telephone.coursetable.Https.Post;
import com.telephone.coursetable.R;

public class WAN {
    /**
     * @return
     * - obj != null : success
     * - obj == null : fail
     * @clear
     */
    public static HttpConnectionAndCode checkcode(Context c, String cookie){
        Resources r = c.getResources();
        return GetBitmap.get(
                r.getString(R.string.wan_get_check_code_url),
                null,
                r.getString(R.string.user_agent),
                r.getString(R.string.wan_get_check_code_referer),
                cookie,
                r.getString(R.string.cookie_delimiter)
        );
    }

    //个人信息
    public static HttpConnectionAndCode personInfo(Context c,String cookie){
        Resources r = c.getResources();
        return Post.post(
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/Student/GetPerson",
                null,
                r.getString(R.string.user_agent),
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/Login/MainDesktop",
                null,
                cookie,
                "]}",
                null,
                r.getString(R.string.lan_get_person_success_contain_response_text),
                null,
                null
        );
    }


    public static HttpConnectionAndCode termInfo(Context c,String cookie){
        Resources r = c.getResources();
        HttpConnectionAndCode rss =  Get.get(
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/Comm/GetTerm",
                null,
                r.getString(R.string.user_agent),
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/Login/MainDesktop",
                cookie,
                "]}",
                null,
                r.getString(R.string.lan_get_terms_success_contain_response_text),
                null,
                null
        );
        return rss;
    }


    //课表
    public static HttpConnectionAndCode goToClass_ClassInfo(Context c,String cookie,String term){
        Resources r = c.getResources();
        return Get.get(
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/student/getstutable",
                new String[]{"term="+term},
                r.getString(R.string.user_agent),
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/Login/MainDesktop",
                cookie,
                "]}",
                null,
                r.getString(R.string.lan_get_table_success_contain_response_text),
                null,
                null
        );
    }


    public static HttpConnectionAndCode hour(Context c,String cookie){
        Resources r = c.getResources();
        return Get.get(
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/Comm/gethours",
                null,
                r.getString(R.string.user_agent),
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/Login/MainDesktop",
                cookie,
                "]}",
                null,
                r.getString(R.string.lan_get_hours_success_contain_response_text),
                null,
                null
        );
    }



    //学生信息
    public static HttpConnectionAndCode studentInfo(Context c,String cookie){
        Resources r = c.getResources();
        return Post.post(
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/student/StuInfo",
                null,
                r.getString(R.string.user_agent),
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/Login/MainDesktop",
                null,
                cookie,
                "]}",
                null,
                null,
                null,
                null
        );
    }



    //有效学分
    public static HttpConnectionAndCode graduationScore(Context c,String cookie){
        Resources r = c.getResources();
        HttpConnectionAndCode gen = com.telephone.coursetable.Http.Post.post(
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/student/Getyxxf",
                null,
                r.getString(R.string.user_agent),
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/Login/MainDesktop",
                "stid=1",
                cookie,
                "}",
                null,
                "\"success\":true",
                null,
                null
        );
        return Get.get(
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/student/Getyxxf",
                null,
                r.getString(R.string.user_agent),
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/Login/MainDesktop",
                cookie,
                "]}",
                null,
                r.getString(R.string.lan_get_graduation_score_success_contain_response_text),
                null,
                null
        );
    }

    //计划学分
    public static HttpConnectionAndCode graduationScore2(Context c, String cookie){
        Resources r = c.getResources();
        HttpConnectionAndCode gen = com.telephone.coursetable.Http.Post.post(
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/student/Getyxxf",
                null,
                r.getString(R.string.user_agent),
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/Login/MainDesktop",
                "stid=1",
                cookie,
                "}",
                null,
                "\"success\":true",
                null,
                null
        );
        return com.telephone.coursetable.Http.Get.get(
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/student/getplancj",
                null,
                r.getString(R.string.user_agent),
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/Login/MainDesktop",
                cookie,
                "}]}",
                null,
                "\"success\":true",
                null,
                null
        );
    }




    public static HttpConnectionAndCode grades(Context c, String cookie){
        Resources r = c.getResources();
        return com.telephone.coursetable.Http.Get.get(
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/Student/GetStuScore",
                new String[]{"term="},
                r.getString(R.string.user_agent),
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/Login/MainDesktop",
                cookie,
                "}]}",
                null,
                r.getString(R.string.lan_get_grades_success_contain_response_text),
                null,
                null
        );
    }


    public static HttpConnectionAndCode examInfo(Context c, String cookie){
        Resources r = c.getResources();
        return com.telephone.coursetable.Http.Get.get(
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/student/getexamap?&term=",
                null,
                r.getString(R.string.user_agent),
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/Login/MainDesktop",
                cookie,
                "}]}",
                null,
                "\"success\":true",
                null,
                null
        );
    }


}
