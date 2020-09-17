package com.telephone.coursetable.Notice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.telephone.coursetable.Clock.Clock;
import com.telephone.coursetable.Database.GoToClass;
import com.telephone.coursetable.Database.GoToClassDao;
import com.telephone.coursetable.Library.LibraryActivity;
import com.telephone.coursetable.MainActivity;
import com.telephone.coursetable.MyApp;
import com.telephone.coursetable.R;

import java.util.List;

public class Notice_Test extends AppCompatActivity {

    //创建渠道
//    private String createNotificationChannel(String channelID, String channelNAME, int level) {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            NotificationChannel channel = new NotificationChannel(channelID, channelNAME, level);
//            manager.createNotificationChannel(channel);
//            return channelID;
//        } else {
//            return null;
//        }
//    }

    Intent intent = new Intent(Notice_Test.this, LibraryActivity.class);

    PendingIntent pi = PendingIntent.getActivity(Notice_Test.this,0,intent,0);
    NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    Notification notification = new NotificationCompat.Builder(Notice_Test.this)
            .setContentTitle("这是测试通知标题")  //设置标题
            .setContentText("这是测试通知内容") //设置内容
            .setWhen(System.currentTimeMillis())  //设置时间
            .setSmallIcon(R.mipmap.ic_launcher)  //设置小图标  只能使用alpha图层的图片进行设置
            .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))   //设置大图标
            .setContentIntent(pi)
            //.setAutoCancel(true)
            .build();


    private GoToClassDao gdao = null;
    private List<GoToClass> gtc_list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_test);

        gdao = MyApp.getCurrentAppDB().goToClassDao();

        gtc_list = gdao.selectAll();

        String c = gtc_list.get(0).courseno;

        //得到课程的时间
        String time = gtc_list.get(0).time;

        //得到现在的时间
        long ntime = Clock.nowTimeStamp();

        
    }
}