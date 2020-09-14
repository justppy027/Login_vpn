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
    public static String grade_point_html( Context c, String cookie){

        Resources r = c.getResources();
        HttpConnectionAndCode gpc = Post.post(
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421a1a013d2766626013051d0/student/xuefenji.asp?",
                new String[]{
                        "xn=",
                        "lwPageSize=1000",
                        "lwBtnquery=(无法对值进行解码)"
                },
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36 Edg/85.0.564.51",
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421a1a013d2766626013051d0/student/xuefenji.asp",
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
    public static List<String> grade_point_array(Context c, String cookie){

        List<String> gp_arr = new ArrayList<>();
        String html = GradePoint_Test.grade_point_html(c, cookie);
        Elements element1;
        Elements element2;
        Document doc;

        do {
            doc = Jsoup.parse( html );
            element1 = doc.select("html > body > table > tbody > tr > td > table > tbody > tr > th");
            element2 = doc.select("html > body > table > tbody > tr > td > table > tbody > tr > td > B > font ");
        }while( element1.isEmpty() || element2.isEmpty() );

        gp_arr.add(element1.get(3).ownText());
        gp_arr.add(element2.get(0).ownText());
        gp_arr.add(element1.get(4).ownText());

        return gp_arr;
    }


}
