package com.telephone.coursetable;

import android.content.Context;
import android.content.res.Resources;

import com.telephone.coursetable.Http.HttpConnectionAndCode;
import com.telephone.coursetable.Http.Post;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class GradePoint_Test {

    //得到一段html文本
    public static String grade_point_html(Context c,String cookie){

        Resources r = c.getResources();
        HttpConnectionAndCode gpc = Post.post(
                "http://172.16.1.99/student/xuefenji.asp\n",
                null,
                " Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 Edge/18.18363",
                "http://172.16.1.99/student/xuefenji.asp",
                null,
                cookie,
                null,
                null,
                null,
                null,
                null
        );
        if(gpc.code == 0){
            return gpc.comment;
        }

        return null;
    }


    //处理得到的html文本
    public static List<String> grade_point_array(String html){

        String sid = null;
        String grade_point_aver = null;
        String Calculate_Type = null;

        List<String> gp_arr = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements element1 = doc.select("html > body > table > tr > td#pagecontrol > table > tr > th ");
        Elements element2 = doc.select("html > body > table >td#pagecontrol > table > tr > td > B > font ");

        gp_arr.add(element1.get(0).ownText());
        gp_arr.add(element2.get(0).ownText());
        gp_arr.add(element1.get(1).ownText());

        return gp_arr;
    }


}
