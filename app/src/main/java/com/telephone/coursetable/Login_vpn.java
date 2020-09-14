package com.telephone.coursetable;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.telephone.coursetable.Clock.Clock;
import com.telephone.coursetable.Clock.Locate;
import com.telephone.coursetable.Clock.TimeAndDescription;
import com.telephone.coursetable.Database.AppDatabase;
import com.telephone.coursetable.Database.ClassInfoDao;
import com.telephone.coursetable.Database.ExamInfoDao;
import com.telephone.coursetable.Database.GoToClassDao;
import com.telephone.coursetable.Database.GradesDao;
import com.telephone.coursetable.Database.GraduationScoreDao;
import com.telephone.coursetable.Database.PersonInfoDao;
import com.telephone.coursetable.Database.TermInfo;
import com.telephone.coursetable.Database.TermInfoDao;
import com.telephone.coursetable.Database.User;
import com.telephone.coursetable.Database.UserDao;
import com.telephone.coursetable.Fetch.LAN;
import com.telephone.coursetable.Fetch.WAN;
import com.telephone.coursetable.Gson.LoginResponse;
import com.telephone.coursetable.Http.HttpConnectionAndCode;
import com.telephone.coursetable.Http.Post;
import com.telephone.coursetable.Library.LibraryActivity;
import com.telephone.coursetable.Merge.Merge;
import com.telephone.coursetable.OCR.OCR;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class Login_vpn extends AppCompatActivity {

    private boolean updating = false;
    //private AppDatabase db = null;

    //DAOs of the database of the whole app
    private GoToClassDao gdao = null;
    private ClassInfoDao cdao = null;
    private TermInfoDao tdao = null;
    private UserDao udao = null;
    private PersonInfoDao pdao = null;
    private GraduationScoreDao gsdao = null;
    private GradesDao grdao = null;
    private ExamInfoDao edao = null;
    private SharedPreferences.Editor editor = MyApp.getCurrentSharedPreferenceEditor();

    private String sid = null;
    private String aaw_pwd = null;//教务处密码
    private String sys_pwd = null;//学分系统密码
    private String vpn_pwd = null;
    private String cookie = null;
    private String ck = null;

    private StringBuilder cookie_builder;
    private String tip;

    //clear
    private void first_login() {
        setContentView(R.layout.activity_login_vpn_no_checkcode);
        ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
        ((TextView)findViewById(R.id.sid_input)).setText("1800301127");
        ((TextView)findViewById(R.id.passwd_input)).setText("080291");

   }

    //clear
    private void system_login(String sid) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpConnectionAndCode res = WAN.checkcode(Login_vpn.this,cookie);
                if (res.obj != null){
                    ck = OCR.getTextFromBitmap(Login_vpn.this, (Bitmap)res.obj, MyApp.ocr_lang_code);
                    cookie_builder.append(res.cookie);
                }

                runOnUiThread(() -> {

                    setContentView(R.layout.activity_login_vpn);
                    ((TextView) findViewById(R.id.sid_input)).setText(sid);
                    ((TextView) findViewById(R.id.sid_input)).setEnabled(false);

                    ((TextView) findViewById(R.id.passwd_input)).setText("080291");
                    ((TextView) findViewById(R.id.aaw_pwd_input)).setText("080291");

                    ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);

                });
            }
        }).start();
    }


    private void updateUserNameAutoFill() {
        final ArrayAdapter<String> ada = new ArrayAdapter<>(Login_vpn.this, android.R.layout.simple_dropdown_item_1line, udao.selectAllUserName());

        runOnUiThread(() -> {
            ((AutoCompleteTextView) findViewById(R.id.sid_input)).setAdapter(ada);
            ((AutoCompleteTextView) findViewById(R.id.sid_input)).setOnDismissListener(() -> {
                clearIMAndFocus();
                new Thread(() -> {
                    final List<User> userSelected = udao.selectUser(((AutoCompleteTextView) findViewById(R.id.sid_input)).getText().toString());
                    if (!userSelected.isEmpty()) {
                        runOnUiThread(() -> {
                            ((AutoCompleteTextView) findViewById(R.id.passwd_input)).setText(userSelected.get(0).password);
                            setFocusToEditText((EditText) findViewById(R.id.checkcode_input));

                        });
                    }
                }).start();
            });
        });

    }


    //clear
    private void lock(){
        ((Button)findViewById(R.id.button)).setEnabled(false);
        ((Button)findViewById(R.id.button2)).setEnabled(false);

        ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
    }

    //clear
    private void unlock(boolean clickable){
        ((Button)findViewById(R.id.button)).setEnabled(clickable);
        ((Button)findViewById(R.id.button2)).setEnabled(clickable);

        ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
    }

    //clear
    private void setFocusToEditText(EditText et) {
        if (et != null) {
            et.requestFocus();
            if (!et.getText().toString().isEmpty()) {
                et.clearFocus();
            }
        }
    }


    private void setFocusToEditText_Force(EditText et) {
        if (et != null) {
            et.requestFocus();
        }
    }

    //clear
    private void clearIMAndFocus() {
        EditText ets = (EditText) findViewById(R.id.sid_input);
        EditText etp = (EditText) findViewById(R.id.passwd_input);
        EditText etw = (EditText) findViewById(R.id.aaw_pwd_input);


        if (ets != null) {
            ets.setEnabled(!ets.isEnabled());
            ets.setEnabled(!ets.isEnabled());
            ets.clearFocus();
        }
        if (etp != null) {
            etp.setEnabled(!etp.isEnabled());
            etp.setEnabled(!etp.isEnabled());
            etp.clearFocus();
        }
        if (etw != null) {
            etw.setEnabled(!etw.isEnabled());
            etw.setEnabled(!etw.isEnabled());
            etw.clearFocus();
        }
    }


    //clear
    public static void deleteOldDataFromDatabase(GoToClassDao gdao, ClassInfoDao cdao, TermInfoDao tdao, PersonInfoDao pdao, GraduationScoreDao gsdao, GradesDao grdao, ExamInfoDao edao) {
        gdao.deleteAll();
        cdao.deleteAll();
        tdao.deleteAll();
        pdao.deleteAll();
        gsdao.deleteAll();
        grdao.deleteAll();
        edao.deleteAll();
    }

    //clear
    public static HttpConnectionAndCode login(Context c, String sid, String pwd, String ckcode, String cookie, @Nullable StringBuilder builder) {
        final String NAME = "login()";
        Resources r = c.getResources();
        String body = "us=" + sid + "&pwd=" + pwd + "&ck=" + ckcode;
        HttpConnectionAndCode login_res = Post.post(
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/Login/SubmitLogin",
                null,
                r.getString(R.string.user_agent),
                r.getString(R.string.wan_vpn_login_referer),
                body,
                cookie,
                "}",
                r.getString(R.string.cookie_delimiter),
                r.getString(R.string.lan_login_success_contain_response_text),
                null,
                null
        );
        if (login_res.comment != null && !login_res.comment.isEmpty()) {
            LoginResponse response = new Gson().fromJson(login_res.comment, LoginResponse.class);
            login_res.comment = response.getMsg();
        }
        if (login_res.code == 0 && builder != null) {
            if (!builder.toString().isEmpty()) {
                builder.append(r.getString(R.string.cookie_delimiter));
            }
            builder.append(login_res.cookie);
        }
        Log.e(NAME, "body: " + body + " code: " + login_res.code + " resp_code: " + login_res.resp_code + " comment/msg: " + login_res.comment);
        return login_res;
    }

    //教务处登录
    public static HttpConnectionAndCode outside_login_test(Context c, final String sid, final String pwd){
        final String NAME = "outside_login_test()";
        Resources r = c.getResources();
        String body = "username=" + sid + "&passwd=" + pwd + "&login=%B5%C7%A1%A1%C2%BC";
        Log.e(NAME + " " + "body", body);
        HttpConnectionAndCode login_res = Post.post(
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421a1a013d2766626013051d0/student/public/login.asp",
                null,
                r.getString(R.string.user_agent),
                "https://v.guet.edu.cn/http/77726476706e69737468656265737421e5e3529f69377d556a468ca88d1b203b/",
                body,
                null,
                null,
                r.getString(R.string.cookie_delimiter),
                null,
                null,
                false
        );
        if (login_res.code == 0 && login_res.resp_code == 302){
            Log.e(NAME + " " + "login status", "success");
        }else {
            if (login_res.code == 0){
                login_res.code = -6;
            }
            Log.e(NAME + " " + "login status", "fail" + " code: " + login_res.code);
        }
        return login_res;
    }


    /**
     * @param pwd origin password
     * @return - String : the encrypted password
     * - null : fail
     * @ui/non-ui get encrypted password
     * @clear
     */
    public static String encrypt(String pwd) {
        int[] key = {134, 8, 187, 0, 251, 59, 238, 74, 176, 180, 24, 67, 227, 252, 205, 80};
        //for good, pwd's length should not be 0
        int pwd_len = pwd.length();
        try {
            if (pwd.length() % 16 != 0) {
                int need_num = 16 - pwd.length() % 16;
                StringBuilder pwd_builder = new StringBuilder();
                pwd_builder.append(pwd);
                for (int i = 0; i < need_num; i++) {
                    pwd_builder.append("0");
                }
                pwd = pwd_builder.toString();
            }
            byte[] pwd_bytes = pwd.getBytes(StandardCharsets.UTF_8);
            for (int i = 0; i < pwd_bytes.length; i++) {
                pwd_bytes[i] ^= key[i % 16];
            }
            StringBuilder encrypt_builder = new StringBuilder();
            encrypt_builder.append("77726476706e6973617765736f6d6521");
            for (int i = 0; i < pwd_len; i++) {
                byte b = pwd_bytes[i];
                encrypt_builder.append(String.format("%02x", b));
            }
            return encrypt_builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return - cookie containing ticket : success
     * - {@link R.string#wan_vpn_ip_forbidden} : fail, ip forbidden
     * - null : fail
     * @non-ui try to login GUET web vpn, if success:
     * 1. return cookie containing web vpn ticket
     * @clear
     */
    public static String vpn_login(Context c, String id, String pwd) {
        final String NAME = "vpn_login()";
        Resources r = c.getResources();
        String body = "auth_type=local&username=" + id + "&sms_code=&password=" + pwd;
        Log.e(NAME + " " + "body", body);
        if (pwd.length() <= 0) {
            Log.e(NAME, "fail");
            return null;
        }
        pwd = encrypt(pwd);
        body = "auth_type=local&username=" + id + "&sms_code=&password=" + pwd;
        Log.e(NAME + " " + "encrypted body", body);
        HttpConnectionAndCode get_ticket_res = com.telephone.coursetable.Https.Get.get(
                r.getString(R.string.wan_vpn_get_ticket_url),
                null,
                r.getString(R.string.user_agent),
                r.getString(R.string.wan_vpn_get_ticket_referer),
                null,
                null,
                r.getString(R.string.cookie_delimiter),
                null,
                null,
                null
        );
        String cookie = get_ticket_res.cookie;
        if (cookie == null || cookie.isEmpty()){
            Log.e(NAME, "fail | can not get init vpn ticket");
            return null;
        }
        Log.e(NAME + " " + "ticket cookie", cookie);
        HttpConnectionAndCode try_to_login_res = com.telephone.coursetable.Https.Post.post(
                r.getString(R.string.wan_vpn_login_url),
                null,
                r.getString(R.string.user_agent),
                r.getString(R.string.wan_vpn_login_referer),
                body,
                cookie,
                "}",
                r.getString(R.string.cookie_delimiter),
                r.getString(R.string.wan_vpn_login_success_contain_response_text),
                null,
                null
        );
        if (try_to_login_res.comment != null) {
            Log.e(NAME + " " + "try to login response", try_to_login_res.comment);
        }
        if (try_to_login_res.code == 0) {
            Log.e(NAME, "success");
            return cookie;
        } else {
            if (try_to_login_res.comment != null && try_to_login_res.comment.contains(r.getString(R.string.wan_vpn_login_need_confirm_contain_response_text))) {
                Log.e(NAME + " " + "need confirm", "confirm...");
                HttpConnectionAndCode confirm_login_res = com.telephone.coursetable.Https.Post.post(
                        r.getString(R.string.wan_vpn_confirm_login_url),
                        null,
                        r.getString(R.string.user_agent),
                        r.getString(R.string.wan_vpn_confirm_login_referer),
                        null,
                        cookie,
                        null,
                        r.getString(R.string.cookie_delimiter),
                        null,
                        null,
                        null
                );
                if (confirm_login_res.code == 0) {
                    Log.e(NAME, "success");
                    return cookie;
                }
            } else if (try_to_login_res.comment != null && try_to_login_res.comment.contains(r.getString(R.string.wan_vpn_login_ip_forbidden_contain_response_text))) {
                Log.e(NAME, "fail | ip forbidden");
                return r.getString(R.string.wan_vpn_ip_forbidden);
            }
            Log.e(NAME, "fail");
            return null;
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.setRunning_activity(MyApp.RunningActivity.LOGIN);
        MyApp.setRunning_activity_pointer(this);
        AppDatabase db = MyApp.getCurrentAppDB();

        gdao = db.goToClassDao();
        cdao = db.classInfoDao();
        tdao = db.termInfoDao();
        udao = db.userDao();
        pdao = db.personInfoDao();
        gsdao = db.graduationScoreDao();
        grdao = db.gradesDao();
        edao = db.examInfoDao();

        cookie_builder = new StringBuilder();
        first_login();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        getSharedPreferences(getResources().getString(R.string.preference_file_name), MODE_PRIVATE).edit().putBoolean(getResources().getString(R.string.pref_user_updating_key), false).commit();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSharedPreferences(getResources().getString(R.string.preference_file_name), MODE_PRIVATE).edit().putBoolean(getResources().getString(R.string.pref_user_updating_key), updating).commit();
    }


    /**
     * @ui 1. call {@link #clearIMAndFocus()}
     * 2. get the username in the sid input box
     * 3. show an AlertDialog to warn user:
     * - if press yes, a new thread will be started:
     * 1. try to delete the user from the database with the username in the sid input box
     * 2. call {@link #updateUserNameAutoFill()}
     * 3. clear sid input box and password input box
     * 4. set focus to sid input box
     * 5. call
     * - if press no, nothing will happen
     * @clear
     */
    public void deleteUser(View view) {

        final String NAME = "deleteUser()";
        clearIMAndFocus();
        String sid = ((AutoCompleteTextView) findViewById(R.id.sid_input)).getText().toString();
         new Thread((Runnable) () -> {
                    udao.deleteUser(sid);
                    Log.e(NAME + " " + "user deleted", sid);
                    updateUserNameAutoFill();

                    runOnUiThread((Runnable) () -> {
                        ((AutoCompleteTextView) findViewById(R.id.sid_input)).setText("");
                        ((AutoCompleteTextView) findViewById(R.id.passwd_input)).setText("");
                        setFocusToEditText((EditText) findViewById(R.id.sid_input));
                    });
         }).start();

    }


    /**
     * @return - true : everything is ok
     * - false : something went wrong
     * @non-ui 1. pull all user-related data from internet
     * 2. save the pulled data to database and shared preference
     * @clear
     */
    public static boolean fetch_merge(Context c, String cookie, PersonInfoDao pdao, TermInfoDao tdao,
                                      GoToClassDao gdao, ClassInfoDao cdao, GraduationScoreDao gsdao,
                                      GradesDao grdao, ExamInfoDao edao ,SharedPreferences.Editor editor) {
        final String NAME = "fetch_merge()";
        HttpConnectionAndCode res;
        HttpConnectionAndCode res_add;

        res = WAN.personInfo(c, cookie);
        res_add = WAN.studentInfo(c, cookie);
        if (res.code != 0 || res_add.code != 0) {
            Log.e(NAME, "fail");
            return false;
        }
        Merge.personInfo(res.comment, res_add.comment, pdao);

        res = WAN.termInfo(c, cookie);
        if (res.code != 0) {
            Log.e(NAME, "fail");
            return false;
        }
        Merge.termInfo(c, res.comment, tdao);

        List<String> terms = tdao.getTermsSince(
                pdao.getGrade().get(0) + "-" + (pdao.getGrade().get(0) + 1) + "_1"
        );
        List<TermInfo> term_list = tdao.selectAll();
        for (TermInfo term : term_list) {
            if (terms.contains(term.term)) continue;
            tdao.deleteTerm(term.term);
        }
        for (String term : terms) {

            res = WAN.goToClass_ClassInfo(c, cookie, term);
            if (res.code != 0) {
                Log.e(NAME, "fail");
                return false;
            }
            Merge.goToClass_ClassInfo(res.comment, gdao, cdao);
        }


        res = WAN.graduationScore(c, cookie);
        res_add = WAN.graduationScore2(c,cookie);
        if (res.code != 0 || res_add.code != 0) {
            Log.e(NAME, "fail");
            return false;
        }
        Merge.graduationScore(res.comment,res_add.comment,gsdao);

        res = WAN.grades(c, cookie);
        if (res.code != 0) {
            Log.e(NAME, "fail");
            return false;
        }
        Merge.grades(res.comment, grdao);

        res = WAN.examInfo(c, cookie);
        if (res.code != 0){
            Log.e(NAME, "fail");
            return false;
        }
        Merge.examInfo(res.comment, edao);


        res = WAN.hour(c, cookie);
        if (res.code != 0) {
            Log.e(NAME, "fail");
            return false;
        }
        Merge.hour(c, res.comment, editor);

        Log.e(NAME, "success");
        return true;
    }


    //clear
    public void login_thread_1(View view) {
        //after click button login , it will go to login_thread

        lock();
        clearIMAndFocus();

        sid = ((TextView) findViewById(R.id.sid_input)).getText().toString();
        vpn_pwd = ((TextView) findViewById(R.id.passwd_input)).getText().toString();


        new Thread(new Runnable() {
            @Override
            public void run() {
                //get cookie
                cookie = Login_vpn.vpn_login(Login_vpn.this, sid, vpn_pwd);

                String tip;
                //fail
                if (cookie == null) {
                    //reason
                    tip = "请检查用户名、密码和网络是否正确。";

                }

                //success
                else {
                    tip = "VPN登录成功，正在跳转界面。";
                    Log.e("stop", cookie);
                }


                runOnUiThread(() -> {
                    Toast.makeText(Login_vpn.this, tip, Toast.LENGTH_SHORT).show();
                    if (tip.equals("请检查用户名、密码和网络是否正确。")) {
                        ((EditText) findViewById(R.id.passwd_input)).setText("");
                        setFocusToEditText((EditText) findViewById(R.id.passwd_input));
                    } else {
                        system_login(sid);
                    }
                    unlock(true);
                });
            }
        }).start();
    }



    public void login_thread_2(View view){
        lock();
        clearIMAndFocus();

        aaw_pwd = ((TextView) findViewById(R.id.passwd_input)).getText().toString();
        sys_pwd = ((TextView) findViewById(R.id.aaw_pwd_input)).getText().toString();


        new Thread(()->{

            HttpConnectionAndCode login_res = login(Login_vpn.this, sid, sys_pwd, ck, cookie, cookie_builder);

            if(login_res.code != 0){

                //-6
                if (login_res.comment != null && login_res.comment.contains("验证码")) {
                    do {
                        HttpConnectionAndCode res = WAN.checkcode(Login_vpn.this, cookie);
                        if (res.obj != null) {
                            ck = OCR.getTextFromBitmap(Login_vpn.this, (Bitmap) res.obj, MyApp.ocr_lang_code);
                            cookie_builder.append(res.cookie);
                        }
                        login_res = login(Login_vpn.this, sid, sys_pwd, ck, cookie, cookie_builder);
                    }while ( login_res.comment != null && login_res.comment.contains("验证码") );
                }

                //test bug
                //login_res.code = -5;


                if(login_res.comment != null && login_res.comment.contains("密码")) {
                    tip = getResources().getString(R.string.login_fail_pwd_title);

                }else if(login_res.comment != null && login_res.comment.contains("网络")){
                    tip = getResources().getString(R.string.snackbar_login_fail_vpn);

                }else if(login_res.comment != null && login_res.comment.contains("成功")){
                    tip = "学分系统验证成功";
                }


                runOnUiThread((Runnable)()->{
                    Snackbar.make(view,tip,BaseTransientBottomBar.LENGTH_SHORT).show();

                    //
                    if( tip.equals(getResources().getString(R.string.login_fail_pwd_title)) ){
                        ((EditText)findViewById(R.id.sys_pwd_input)).setText("");
                        setFocusToEditText((EditText)findViewById(R.id.sys_pwd_input));
                        unlock(true);
                        return;
                    }else if(tip.equals(getResources().getString(R.string.snackbar_login_fail_vpn))){
                        //请检查网络连接
                        return;
                    }
                });

            }

            HttpConnectionAndCode outside_login_res = outside_login_test(Login_vpn.this, sid, aaw_pwd);

            if (outside_login_res.code != 0){
                runOnUiThread((Runnable) () -> {

                    Snackbar.make(view, getResources().getString(R.string.lan_snackbar_outside_test_login_fail), BaseTransientBottomBar.LENGTH_SHORT).show();
                    ((EditText)findViewById(R.id.aaw_pwd_input)).setText("");
                    setFocusToEditText((EditText)findViewById(R.id.aaw_pwd_input));
                    unlock(true);

                });

                return;
            }



            if( login_res.code == 0 ) {
                /** get shared preference and its editor */
                final SharedPreferences shared_pref = MyApp.getCurrentSharedPreference();
                final SharedPreferences.Editor editor = MyApp.getCurrentSharedPreferenceEditor();

                if (!Login_vpn.this.toString().equals(MyApp.getRunning_activity_pointer().toString())){

                    runOnUiThread(()->Toast.makeText(Login_vpn.this, "登录取消", Toast.LENGTH_SHORT).show());
                    return;
                }

                //
                /** insert/replace new user into database */
                udao.insert(new User(sid, aaw_pwd, sys_pwd, vpn_pwd));


                /** deactivate all user in database */
                udao.disableAllUser();
                /** set {@link MyApp#running_login_thread} to true */
                MyApp.setRunning_login_thread(true);
                /** clear shared preference */
                editor.clear();
                /** commit shared preference */
                editor.commit();
                /** show tip snack-bar, change title */
                runOnUiThread(() -> {
                    Snackbar.make(view, getResources().getString(R.string.lan_snackbar_data_updating), BaseTransientBottomBar.LENGTH_LONG).show();
                    getSupportActionBar().setTitle(getResources().getString(R.string.lan_title_login_updating));
                });

                //
                /** call {@link #deleteOldDataFromDatabase()} */
                deleteOldDataFromDatabase(gdao, cdao, tdao, pdao, gsdao, grdao, edao);


                boolean fetch_merge_res = fetch_merge(Login_vpn.this, cookie, pdao, tdao, gdao, cdao, gsdao, grdao, edao, editor);


                /** commit shared preference */
                editor.commit();

                if (fetch_merge_res) {

                    /** locate now, print the locate-result to log */
                    Log.e(
                            " " + "locate now",
                            Clock.locateNow(
                                    Clock.nowTimeStamp(), tdao, shared_pref, MyApp.times,
                                    DateTimeFormatter.ofPattern(getResources().getString(R.string.server_hours_time_format)),
                                    getResources().getString(R.string.pref_hour_start_suffix),
                                    getResources().getString(R.string.pref_hour_end_suffix),
                                    getResources().getString(R.string.pref_hour_des_suffix)
                            ) + ""
                    );


                    udao.activateUser(sid);

                    MyApp.setRunning_login_thread(false);

                    runOnUiThread(() -> {
                        unlock(false);
                        Toast.makeText(Login_vpn.this, getResources().getString(R.string.lan_toast_update_success), Toast.LENGTH_SHORT).show();
                        getSupportActionBar().setTitle(getResources().getString(R.string.lan_title_login_updated));
                        startActivity(new Intent(Login_vpn.this, MainActivity.class));
                    });

                } else {

                    /** set {@link MyApp#running_login_thread} to false */
                    MyApp.setRunning_login_thread(false);
                    /** {@link MyApp#running_main} */
                    if (MyApp.getRunning_activity().equals(MyApp.RunningActivity.MAIN) && MyApp.getRunning_main() != null) {
                        runOnUiThread(() -> {
                            /** show tip toast */
                            Toast.makeText(Login_vpn.this, getResources().getString(R.string.lan_toast_update_fail), Toast.LENGTH_SHORT).show();
                            /** call {@link MainActivity#refresh()} */
                            MyApp.getRunning_main().refresh();
                        });
                    } else {
                        runOnUiThread(() -> {
                            /** call {@link #unlock(boolean)} with true */
                            unlock(true);
                            /** show tip snack-bar, change title */
                            Snackbar.make(view, getResources().getString(R.string.lan_toast_update_fail), BaseTransientBottomBar.LENGTH_LONG).show();
                            getSupportActionBar().setTitle(getResources().getString(R.string.lan_title_login_updated_fail));
                        });
                    }
                }
            }
        }).start();
    }
}


