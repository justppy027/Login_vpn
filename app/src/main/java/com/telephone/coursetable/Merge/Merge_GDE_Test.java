package com.telephone.coursetable.Merge;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import com.telephone.coursetable.Fetch.WAN;
import com.telephone.coursetable.Gson.GraduationDegreeEvaluation;
import com.telephone.coursetable.Gson.GraduationDegreeEvaluation_s;
import com.telephone.coursetable.Http.HttpConnectionAndCode;


import java.util.List;

public class Merge_GDE_Test {


    public static List<GraduationDegreeEvaluation> fetch_gde(Context c,String cookie){
        HttpConnectionAndCode res = WAN.graduationDegree(c,cookie);
        while(res.code != 0){
            res = WAN.graduationDegree(c,cookie);
        }

        GraduationDegreeEvaluation_s gdeS = new Gson().fromJson(res.comment,GraduationDegreeEvaluation_s.class);
        List<GraduationDegreeEvaluation> gde_list = gdeS.getData();

        return gde_list;
    }



}
