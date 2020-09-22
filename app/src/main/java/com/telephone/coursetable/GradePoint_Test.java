package com.telephone.coursetable;

import android.content.Context;
import android.content.res.Resources;

import com.google.android.material.snackbar.Snackbar;
import com.telephone.coursetable.Http.HttpConnectionAndCode;
import com.telephone.coursetable.Http.Post;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class GradePoint_Test {

    //学分绩学年
    public static String grade_year_html(  Context c, String cookie ) {
        Resources r = c.getResources();
        HttpConnectionAndCode gpc = Post.post(
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421a1a013d2766626013051d0/student/xuefenji.asp",
                null,
                r.getString(R.string.user_agent),
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

    //教务处登录后
    //得到一段html文本
    public static String grade_point_html( Context c, String cookie, String syear){
        Resources r = c.getResources();
        HttpConnectionAndCode gpc = Post.post(
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421a1a013d2766626013051d0/student/xuefenji.asp",
                new String[]{
                        "xn=" + syear,
                        "lwPageSize=1000",
                        "lwBtnquery=(无法对值进行解码)"
                },
                r.getString(R.string.user_agent),
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


    //学分系统登录后


    //处理得到的html文本
    public static List<String> grade_point_array(Context c, String cookie, String sid){

        List<String> gp_arr = new ArrayList<>();
        String html;
        String xml;
        Elements sno_sterm_html;
        Elements sscore_html;
        Elements years_xml;
        Document doc;
        int time;

        String[] syears = sid.split("");
        String sy;
        String syear = "";
        sy = syears[0] + syears[1];

        xml = GradePoint_Test.grade_year_html(c, cookie);
        doc = Jsoup.parse( xml );
        years_xml = doc.select("html > body > form > table > tbody > tr > td > select > option");
        for ( int i = 0 ; i < years_xml.size() ; i++ ) {
            Element year = years_xml.get(i);
            syear = syear + year.ownText();
            if (year.ownText().contains(sy)) {
                break;
            }
            syear = syear + ",";
        }

        syears = syear.split(",");
        for ( String year : syears ) {
            time = 0;
            do {
                html = grade_point_html(c, cookie, year);
                doc = Jsoup.parse(html);
                sno_sterm_html = doc.select("html > body > table > tbody > tr > td > table > tbody > tr > th");
                sscore_html = doc.select("html > body > table > tbody > tr > td > table > tbody > tr > td > B > font ");
            } while ((sno_sterm_html.isEmpty() && sscore_html.isEmpty()) && (++time) < 10);
            if (sno_sterm_html.isEmpty() || sscore_html.isEmpty()) {
                return null;
            }
            gp_arr.add(sno_sterm_html.get(3).ownText());
            gp_arr.add(sscore_html.get(0).ownText());
            gp_arr.add(sno_sterm_html.get(4).ownText());
        }

        return gp_arr;
    }


}
