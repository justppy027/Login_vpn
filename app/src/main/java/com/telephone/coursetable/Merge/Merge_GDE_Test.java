package com.telephone.coursetable.Merge;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import com.telephone.coursetable.Gson.GraduationDegreeEvaluation;
import com.telephone.coursetable.Gson.GraduationDegreeEvaluation_s;


import java.util.List;

public class Merge_GDE_Test {

    public static void gradeDegreeEva(@NonNull String origin_gde){
        GraduationDegreeEvaluation_s gdeS = new Gson().fromJson(origin_gde,GraduationDegreeEvaluation_s.class);
        List<GraduationDegreeEvaluation> gde_list = gdeS.getData();




    }




}
