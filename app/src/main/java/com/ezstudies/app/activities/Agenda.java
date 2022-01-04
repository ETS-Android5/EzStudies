package com.ezstudies.app.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.ezstudies.app.Database;
import com.ezstudies.app.R;
import com.ezstudies.app.services.AlarmSetter;
import com.ezstudies.app.services.Login;
import com.ezstudies.app.services.RouteCalculator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity that displays an agenda
 */
public class Agenda extends FragmentActivity {
    /**
     * ID of notification channel
     */
    private static final String CHANNEL_ID_AGENDA = "EzStudies_Agenda";
    /**
     * JavaScript Interface for processing scripts from websites
     */
    private JavaScriptInterface jsi;
    /**
     * Shared preferences
     */
    private SharedPreferences sharedPreferences;
    /**
     * Shared preferences editor
     */
    private SharedPreferences.Editor editor;
    /**
     * Loading dialog
     */
    private ProgressDialog progressDialog;
    /**
     * ViewPager, slide between fragments
     */
    private ViewPager2 viewPager;
    /**
     * Adapter for ViewPager
     */
    private FragmentStateAdapterAgenda adapter;
    /**
     * Broadcast receiver for RouteCalculator
     */
    private RouteReceiver routeReceiver;
    /**
     * Broadcast receiver for Login
     */
    private LoginReceiver loginReceiver;
    /**
     * Number of pending notifications
     */
    private static int nbNotifPendingAgenda;

    /**
     * Initiate the activity
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda_layout);
        createNotificationChannelAgenda();
        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        viewPager = findViewById(R.id.agenda_viewpager);
        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentStateAdapterAgenda(fm, getLifecycle());
        viewPager.setAdapter(adapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                ImageView dot0 = findViewById(R.id.agenda_dot0);
                ImageView dot1 = findViewById(R.id.agenda_dot1);
                ImageView dot2 = findViewById(R.id.agenda_dot2);
                ImageView dot3 = findViewById(R.id.agenda_dot3);
                ImageView dot4 = findViewById(R.id.agenda_dot4);
                ImageView dot5 = findViewById(R.id.agenda_dot5);
                switch (position){
                    case 0:
                        dot0.setImageDrawable(getDrawable(R.drawable.dot_selected));
                        dot1.setImageDrawable(getDrawable(R.drawable.dot));
                        dot2.setImageDrawable(getDrawable(R.drawable.dot));
                        dot3.setImageDrawable(getDrawable(R.drawable.dot));
                        dot4.setImageDrawable(getDrawable(R.drawable.dot));
                        dot5.setImageDrawable(getDrawable(R.drawable.dot));
                        break;
                    case 1:
                        dot0.setImageDrawable(getDrawable(R.drawable.dot));
                        dot1.setImageDrawable(getDrawable(R.drawable.dot_selected));
                        dot2.setImageDrawable(getDrawable(R.drawable.dot));
                        dot3.setImageDrawable(getDrawable(R.drawable.dot));
                        dot4.setImageDrawable(getDrawable(R.drawable.dot));
                        dot5.setImageDrawable(getDrawable(R.drawable.dot));
                        break;
                    case 2:
                        dot0.setImageDrawable(getDrawable(R.drawable.dot));
                        dot1.setImageDrawable(getDrawable(R.drawable.dot));
                        dot2.setImageDrawable(getDrawable(R.drawable.dot_selected));
                        dot3.setImageDrawable(getDrawable(R.drawable.dot));
                        dot4.setImageDrawable(getDrawable(R.drawable.dot));
                        dot5.setImageDrawable(getDrawable(R.drawable.dot));
                        break;
                    case 3:
                        dot0.setImageDrawable(getDrawable(R.drawable.dot));
                        dot1.setImageDrawable(getDrawable(R.drawable.dot));
                        dot2.setImageDrawable(getDrawable(R.drawable.dot));
                        dot3.setImageDrawable(getDrawable(R.drawable.dot_selected));
                        dot4.setImageDrawable(getDrawable(R.drawable.dot));
                        dot5.setImageDrawable(getDrawable(R.drawable.dot));
                        break;
                    case 4:
                        dot0.setImageDrawable(getDrawable(R.drawable.dot));
                        dot1.setImageDrawable(getDrawable(R.drawable.dot));
                        dot2.setImageDrawable(getDrawable(R.drawable.dot));
                        dot3.setImageDrawable(getDrawable(R.drawable.dot));
                        dot4.setImageDrawable(getDrawable(R.drawable.dot_selected));
                        dot5.setImageDrawable(getDrawable(R.drawable.dot));
                        break;
                    case 5:
                        dot0.setImageDrawable(getDrawable(R.drawable.dot));
                        dot1.setImageDrawable(getDrawable(R.drawable.dot));
                        dot2.setImageDrawable(getDrawable(R.drawable.dot));
                        dot3.setImageDrawable(getDrawable(R.drawable.dot));
                        dot4.setImageDrawable(getDrawable(R.drawable.dot));
                        dot5.setImageDrawable(getDrawable(R.drawable.dot_selected));
                        break;
                }
            }
        });
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int weekDay = cal.get(Calendar.DAY_OF_WEEK)-2;
        Log.d("weekday", String.valueOf(weekDay));
        if(weekDay != 7){
            viewPager.setCurrentItem(getIntent().getIntExtra("weekday", weekDay));
        }
        routeReceiver = new RouteReceiver();
        loginReceiver = new LoginReceiver();
        nbNotifPendingAgenda = 0;
    }

    /**
     * Create pages of ViewPager
     */
    private class FragmentStateAdapterAgenda extends FragmentStateAdapter {
        /**
         * Constructor
         * @param fragmentManager FragmentManager
         * @param lifecycle Lifecycle
         */
        public FragmentStateAdapterAgenda(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        /**
         * Create fragment
         * @param page Page
         * @return Fragment
         */
        @NonNull
        @Override
        public Fragment createFragment(int page) {
            switch (page) {
                case 0 :
                    return new AgendaFragment(1, Agenda.this);
                case 1 :
                    return new AgendaFragment(2, Agenda.this);
                case 2 :
                    return new AgendaFragment(3, Agenda.this);
                case 3 :
                    return new AgendaFragment(4, Agenda.this);
                case 4 :
                    return new AgendaFragment(5, Agenda.this);
                case 5 :
                    return new AgendaFragment(6, Agenda.this);
                default:
                    return new AgendaFragment(1, Agenda.this);
            }
        }

        /**
         * Get number of pages
         * @return Number of pages
         */
        @Override
        public int getItemCount() {
            return 6;
        }
    }

    /**
     * Import agenda from Celcat
     */
    public void import_celcat() {
        progressDialog = ProgressDialog.show(this, getString(R.string.connecting), getString(R.string.loading), true);
        String name = sharedPreferences.getString("name", null);
        String password = sharedPreferences.getString("password", null);
        Intent intent = new Intent(this, Login.class);
        intent.putExtra("name", name);
        intent.putExtra("password", password);
        intent.putExtra("target", "AgendaLogin");
        startService(intent);
        registerReceiver(loginReceiver, new IntentFilter("AgendaLogin"));
    }

    /**
     * Parse HTML source from Celcat
     */
    public void parseCelcat(){
        Database database = new Database(this);
        database.clearAgenda();

        String source = jsi.getSource();
        Log.d("html source", source);
        Document document = Jsoup.parse(source, "UTF-8");
        Elements days = document.getElementsByClass("fc-list-heading");
        for (Element e : days) { //for each days
            String date = e.attr("data-date");
            String dateElements[] = date.split("-");
            while(e.nextElementSibling() != null && e.nextElementSibling().className().equals("fc-list-item")){ //for each courses in that day
                e = e.nextElementSibling();
                Element eHours = e.getElementsByClass("fc-list-item-time fc-widget-content").get(0);
                String hour = eHours.text();
                String startHour = hour.substring(0, hour.indexOf(" - "));
                String endHour = hour.substring(hour.indexOf(" - ")+3);
                String startHourSplit[] = startHour.split(":");
                String endHourSplit[] = endHour.split(":");
                String startingHour = startHourSplit[0];
                String startingMinute = startHourSplit[1];
                String endingHour = endHourSplit[0];
                String endingMinute = endHourSplit[1];
                Element eCourse = e.getElementsByClass("fc-list-item-title fc-widget-content").get(0);
                String course = eCourse.toString();
                course = course.substring(course.indexOf("</a>")+4, course.indexOf("</td>"));
                String courseInfo[] = course.split(" <br> ");
                String title = courseInfo[0] + " - " + courseInfo[1];
                String description = "";
                for(int i = 2 ; i<courseInfo.length ; i++){
                    description += courseInfo[i] + " / ";
                }
                description = description.substring(0, description.length()-3);
                String newDate = dateElements[2] + "/" + dateElements[1] + "/" + dateElements[0];
                database.addAgenda(newDate, title, startingHour + ":" + startingMinute, endingHour + ":" + endingMinute, description);
            }
        }
        Log.d("database agenda", database.toStringAgenda());
        database.close();

        progressDialog.cancel();
        Toast.makeText(this, getString(R.string.celcat_success), Toast.LENGTH_SHORT).show();
        restart();
    }

    /**
     * Import ICS file from storage
     */
    public void importICS(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/calendar");
        ActivityResultLauncher.launch(intent);
    }

    /**
     * Parse content of ICS file
     * @param content ICS file content
     */
    public void parseICS(String content){
        Database database = new Database(this);
        database.clearAgenda();
        try {
            BufferedReader bufferedReader = new BufferedReader(new StringReader(content));
            String line;
            String date = null;
            String title = null;
            String startingAt = null;
            String endingAt = null;
            String description = null;
            String list[];
            while((line = bufferedReader.readLine()) != null) { //read every lines
                list = line.split(":");
                switch (list[0]){ //detect type of data of the line
                    case "BEGIN": //start
                        if(list[1].equals("VEVENT")) {
                            date = null;
                            title = null;
                            startingAt = null;
                            endingAt = null;
                            description = null;
                        }
                        break;
                    case "DTSTART": //start time
                        startingAt = list[1].substring(9, 11) + ":" + list[1].substring(11, 13);
                        date = list[1].substring(6, 8) + "/" + list[1].substring(4, 6) + "/" + list[1].substring(0, 4);
                        break;
                    case "DTEND": //end time
                        endingAt = list[1].substring(9, 11) + ":" + list[1].substring(11, 13);
                        break;
                    case "DESCRIPTION": //description
                        description = list[1];
                        break;
                    case "SUMMARY": //title
                        title = list[1];
                        break;
                    case "END": //end
                        if(list[1].equals("VEVENT")) {
                            database.addAgenda(date, title, startingAt, endingAt, description);
                        }
                        break;
                }
            }
            bufferedReader.close();
            Log.d("database agenda", database.toStringAgenda());
        } catch (IOException e) {
            e.printStackTrace();
        }
        database.close();

        Toast.makeText(this, getString(R.string.ics_success), Toast.LENGTH_SHORT).show();
        restart();
    }

    /**
     * Handle when an activity finishes
     */
    ActivityResultLauncher<Intent> ActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                /**
                 * On file selected
                 * @param result Result
                 */
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) { //file selected
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(result.getData().getData());
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                            String line;
                            String content = "";
                            while ((line = bufferedReader.readLine()) != null) {
                                content = content + line + "\n";
                            }
                            Log.d("ics content", content);
                            bufferedReader.close();
                            inputStream.close();
                            parseICS(content);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    /**
     * Export agenda from database to ICS file
     * @param view View
     */
    public void exportICS(View view) {
        Database database = new Database(this);
        String ics = database.toICS();
        database.close();
        try{
            Calendar now = Calendar.getInstance();
            String date = now.get(Calendar.DAY_OF_MONTH) + "" + (now.get(Calendar.MONTH)+1) + "" + now.get(Calendar.YEAR) + "" + now.get(Calendar.HOUR_OF_DAY) + "" + now.get(Calendar.MINUTE) + "" + now.get(Calendar.SECOND);
            String name = "celcat_" + date + ".ics";
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + name); //downloads folder
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(ics);
            fileWriter.close();
            Toast.makeText(this, getString(R.string.export_success) + name, Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refresh agenda, accordingly to the chosen mode
     * @param view View
     */
    public void refresh(View view){
        int import_mode = sharedPreferences.getInt("import_mode", -1);
        switch (import_mode){
            case 0: //celcat
                import_celcat();
                break;
            case 1: //ics
                importICS();
                break;
            case -1:
                break;
        }
    }

    /**
     * Restart activity to reload every pages of ViewPager
     */
    public void restart(){
        finish();
        startActivity(getIntent());
        setNotificationsAgenda(this);
        Boolean alarm = sharedPreferences.getBoolean("alarm", false);
        if(alarm){
            setAlarms();
        }
    }

    /**
     * Set alarms for every days of the week
     */
    public void setAlarms(){
        int mode = sharedPreferences.getInt("travel_mode", 0);
        if(mode != 2){ //not transit
            String homeLat = sharedPreferences.getString("home_latitude", null);
            String homeLong = sharedPreferences.getString("home_longitude", null);
            String schoolLat = sharedPreferences.getString("school_latitude", null);
            String schoolLong = sharedPreferences.getString("school_longitude", null);

            Intent intent = new Intent(this, RouteCalculator.class);
            intent.putExtra("mode", mode);
            intent.putExtra("homeLat", homeLat);
            intent.putExtra("homeLong", homeLong);
            intent.putExtra("schoolLat", schoolLat);
            intent.putExtra("schoolLong", schoolLong);
            intent.putExtra("target", "AgendaRoute");
            startService(intent);
            registerReceiver(routeReceiver, new IntentFilter("AgendaRoute"));
        }
        else{ //transit
            Database database = new Database(this);
            ArrayList<String[]> firsts = database.getFirsts();
            database.close();
            int prep_time = sharedPreferences.getInt("prep_time", -1);
            int travel_time = sharedPreferences.getInt("travel_time", -1);
            for(String [] infos : firsts){ //calculate alarm for each days
                int hour = Integer.parseInt(infos[1].split(":")[0]);
                int minute = Integer.parseInt(infos[1].split(":")[1]);
                int total = travel_time + prep_time;
                int diffHour = total/60;
                int diffMin = total - diffHour*60;
                hour -= diffHour;
                minute -= diffMin;
                if(minute<0){
                    minute += 60;
                    hour--;
                }
                infos[1] = hour + ":" + minute;
            }
            Intent intent = new Intent(this, AlarmSetter.class);
            intent.putExtra("list", firsts);
            startService(intent);
        }
    }

    /**
     * Broadcast receiver for route
     */
    private class RouteReceiver extends BroadcastReceiver{
        /**
         * On receive
         * @param context Context
         * @param intent Intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(routeReceiver);
            Database database = new Database(context);
            ArrayList<String[]> firsts = database.getFirsts();
            database.close();
            int duration = intent.getIntExtra("duration", -1);
            editor.putInt("duration", duration);
            editor.apply();
            int prep_time = sharedPreferences.getInt("prep_time", -1);
            for(String [] infos : firsts){
                int hour = Integer.parseInt(infos[1].split(":")[0]);
                int minute = Integer.parseInt(infos[1].split(":")[1]);
                int total = duration/60 + prep_time;
                int diffHour = total/60;
                int diffMin = total - diffHour*60;
                hour -= diffHour;
                minute -= diffMin;
                if(minute<0){
                    minute += 60;
                    hour--;
                }
                infos[1] = hour + ":" + minute;
                Log.d("alarm hour", infos[1]);
            }
            Intent intent1 = new Intent(context, AlarmSetter.class);
            intent1.putExtra("list", firsts);
            startService(intent1);
        }
    }

    /**
     * Broadcast receiver for Login
     */
    private class LoginReceiver extends BroadcastReceiver{
        /**
         * On receive
         * @param context Context
         * @param intent Intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(loginReceiver);
            String url = intent.getStringExtra("url");
            Boolean success = intent.getBooleanExtra("success", false);
            if(!success){
                progressDialog.cancel();
                Toast.makeText(context, getString(R.string.login_fail_network), Toast.LENGTH_SHORT).show();
                return;
            }
            WebView webview = new WebView(context);
            webview.setWebViewClient(new MyWebView(Agenda.this));
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setLoadWithOverviewMode(true);
            jsi = new JavaScriptInterface();
            webview.addJavascriptInterface(jsi, "HTMLOUT");
            HashMap<String, String> cookies = (HashMap<String, String>) intent.getSerializableExtra("cookies");
            for(Map.Entry<String, String> pair: cookies.entrySet()){
                String cookie = pair.getKey() + "=" + pair.getValue() + "; path=/";
                CookieManager.getInstance().setCookie(url, cookie);
            }
            webview.loadUrl(url);
        }
    }

    /**
     * JavaScript Interface
     */
    private class JavaScriptInterface {
        /**
         * HTML source
         */
        private String source;

        /**
         * Handle HTML source
         * @param html HTML source
         */
        @JavascriptInterface
        public void processHTML(String html){
            source = html;
        }

        /**
         * Get HTML source
         * @return HTML source
         */
        public String getSource() {
            return source;
        }
    }

    /**
     * WebView
     */
    private class MyWebView extends WebViewClient {
        /**
         * Agenda
         */
        private Agenda agenda;
        /**
         * Status of parsing
         */
        private Boolean parsing = false;

        /**
         * Constructor
         * @param agenda Agenda
         */
        public MyWebView(Agenda agenda){
            this.agenda = agenda;
        }

        /**
         * On page finished
         * @param view View
         * @param url URL
         */
        public void onPageFinished(WebView view, String url) {
            Log.d("url", url);
            view.loadUrl("javascript:HTMLOUT.processHTML(document.documentElement.outerHTML);"); //extract html source using javascript interface
            if (url.contains("fid0") && url.contains("listWeek") && !parsing){
                Log.d("html parser", "parsing");
                parsing = true;
                String source = "";
                int i = 0;
                while (i <= 10 && !source.contains("fc-list-table")){ // cycle max 10times for 1sec and check if the javascript content is ready to be parsed
                    Log.d("iteration parser", String.valueOf(i));
                    Log.d("fc-list-table", String.valueOf(source.contains("fc-list-table")));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    source = jsi.getSource();
                    i++;
                }
                agenda.parseCelcat();
            }
        }
    }

    /**
     * Broadcast receiver for notifications
     */
    public static class NotificationReceiver extends BroadcastReceiver {
        /**
         * On receive
         * @param context Context
         * @param intent Intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("receiver", "received ! id = " + intent.getIntExtra("nb", 1));
            Intent agenda = new Intent(context, Agenda.class);
            agenda.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, agenda, PendingIntent.FLAG_IMMUTABLE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_AGENDA)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(intent.getStringExtra("text")))
                    .setContentTitle(intent.getStringExtra("title"))
                    .setContentText(intent.getStringExtra("text"))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(intent.getIntExtra("nb", -1), builder.build());
        }
    }

    /**
     * Schedule a notification
     * @param context Context
     * @param time Time
     * @param title Title
     * @param text Text
     */
    public static void scheduleNotificationAgenda(Context context, long time, String title, String text) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("text", text);
        intent.putExtra("nb", nbNotifPendingAgenda);
        PendingIntent pending = PendingIntent.getBroadcast(context, nbNotifPendingAgenda, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        // Schedule notification
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setExact(AlarmManager.RTC_WAKEUP, time, pending);
        Log.d("agenda notification", "created notification");
        nbNotifPendingAgenda ++;
    }

    /**
     * Cancel a notification
     * @param context Context
     */
    public static void cancelNotificationsAgenda(Context context) {
        while(nbNotifPendingAgenda >= 0){
            Intent intent = new Intent(context, NotificationReceiver.class);
            intent.putExtra("title", "title");
            intent.putExtra("text", "text");
            PendingIntent pending = PendingIntent.getBroadcast(context, nbNotifPendingAgenda, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            // Cancel notification
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            manager.cancel(pending);
            nbNotifPendingAgenda --;
        }
        nbNotifPendingAgenda = 0;
    }

    /**
     * Create a notification channel
     */
    private void createNotificationChannelAgenda() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name) + " " + getString(R.string.agenda);
            String description = getString(R.string.reminders);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_AGENDA, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Set notifications
     * @param context Context
     */
    public static void setNotificationsAgenda(Context context){
        Database database = new Database(context);
        cancelNotificationsAgenda(context);
        Calendar now = Calendar.getInstance();
        ArrayList<ArrayList<String>> courses = database.toTabAgenda();
        database.close();
        Log.d("courses", courses.toString());
        for (ArrayList<String> row : courses){
            Calendar calendar = Calendar.getInstance();
            String[] date = row.get(0).split("/");
            String[] hour = row.get(2).split(":");
            int minute = Integer.parseInt(hour[1]) - 15;
            int heure = Integer.parseInt(hour[0]);
            if (minute < 0){
                heure --;
                minute = 60 + minute;
            }
            calendar.set(Integer.parseInt(date[2]), Integer.parseInt(date[1])-1, Integer.parseInt(date[0]), heure, minute);
            long time = calendar.getTimeInMillis();
            if(now.getTimeInMillis() < time){
                String text = row.get(2) + " - " + row.get(3) + "\n" + row.get(4);
                scheduleNotificationAgenda(context, time, row.get(1), text.replace(" / ", "\n"));
                Log.d("new notification", row.get(0) + " at " + heure + ":" + minute);
            }
        }
    }
}
