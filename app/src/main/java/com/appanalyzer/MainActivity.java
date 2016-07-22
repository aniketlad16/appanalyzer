package com.appanalyzer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.util.MD5;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    Context context;
    public static Context context2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        context2=context;

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main , menu);

        return true;
    }

    public void scanFunction(View view)
    {
        new APKProcessor((MainActivity) context).execute("Exe");
    }


}





class APKProcessor extends AsyncTask<String, Void, String> {
    ProgressDialog PD;

    public APKProcessor() {

    }

    int notFoundCounter = 0;
    Context context;
    File mFileToHash;
    String tempDate = "Date:";
    static List<App> apps;

    public APKProcessor(MainActivity mainActivityObj) {
        context = mainActivityObj;
    }

    @Override
    protected String doInBackground(String... params) {


         apps = new ArrayList<>();

        /* Get app list */
        final PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages =  pm.getInstalledApplications(PackageManager.GET_META_DATA);
        int i=0;
        for (ApplicationInfo packageInfo : packages) {
            String name;


            // Skips the system application (packages)

            ApplicationInfo applicationInfo = packages.get(i);
            if ( (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1)
            {
                i++;
                continue;
            }

            /* Use package name if app label is empty */

            if ((name = String.valueOf(pm.getApplicationLabel(packageInfo))).isEmpty()) {
                name = packageInfo.packageName;
            }
            i++;
            Drawable icon = pm.getApplicationIcon(packageInfo);
            String apkPath = packageInfo.sourceDir;
            String packaName= packageInfo.packageName;
            MD5 md5Obj = new MD5();
            mFileToHash = new File(apkPath);
            String md5AppInfo = md5Obj.checkMD5(mFileToHash);
            long apkSize = new File(packageInfo.sourceDir).length();

            apps.add(new App(name, icon, apkPath, apkSize, packaName, md5AppInfo));
        }

        /* Sort apps alphabetically */
        Collections.sort(apps, new Comparator<App>() {
            @Override
            public int compare(App app1, App app2) {
                return app1.getName().toLowerCase().compareTo(app2.getName().toLowerCase());
            }
        });
        // temp = internetDataConnection(compName);


        return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {

        Intent intent = new Intent(context, AppListActivity.class);
        PD.dismiss();
        context.startActivity(intent);

    }


    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        PD = new ProgressDialog(context);
        PD.setTitle("Please Wait..");
        PD.setMessage("Loading...");
        PD.setCancelable(false);
        PD.show();


    }

    @Override
    protected void onProgressUpdate(Void... values) {}


    public String[] internetDataConnection(String compName) throws IOException {
        URL urlObj = new URL("http://finance.google.com/finance/info?client=ig&q=NASDAQ:"+compName);

        HttpURLConnection conData = (HttpURLConnection) urlObj.openConnection();

        if(conData.getResponseCode() == 400) {
            System.out.println("++++++++============ Response code: " + conData.getResponseCode());
            notFoundCounter++;
            Handler handler =  new Handler(context.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(context, "No Data Found.Enter Correct Code.", Toast.LENGTH_SHORT).show();
                }
            });

            String res[]= new String[4];
            tempDate = "No Data ";
            res[0]= "No Data";
            res[1]= "No Data";
            res[2]= "No Data";
            res[3]= "No Data";

            return res;
        }
        BufferedReader inStream = new BufferedReader(new InputStreamReader(conData.getInputStream()));
        String inputDataLine;
        String temp = "";


        //System.out.println("**************Data got: ");

        while ((inputDataLine = inStream.readLine()) != null){
            //System.out.println(inputLine);
            temp = temp + inputDataLine;
        }

        inStream.close();
        //System.out.println("**************Finished********** ");



        String patternString = ",";
        Pattern pattern = Pattern.compile(patternString);

        String token[] = pattern.split(temp);


        String patternStringTemp = ":";
        Pattern patternTemp = Pattern.compile(patternStringTemp);

        String tokenTemp[] = patternTemp.split(token[3]);
        String[] comp =  patternTemp.split(token[1]);
        String[] dateOn1 =  patternTemp.split(token[9]);
        String[] dateOn2 = patternTemp.split(token[10]);
        String dateOn = dateOn2[1] +":"+dateOn1[1];
        String[] change =  patternTemp.split(token[11]);

        String resArr[] = new String[4];
        resArr[0] = comp[1];
        resArr[1] = change[1];
        resArr[2] = dateOn;
        resArr[3] = tokenTemp[1];

        System.out.println("++++++++============ Company : "+resArr[0]);


        System.out.println("++++++++============ Change : "+resArr[1]);

        System.out.println("++++++++============ Date : "+resArr[2]);


        System.out.println("++++++++============ Value : "+resArr[3]);
        return resArr;

    }

    ProgressBar bar;

    public void setProgressBar(ProgressBar bar) {
        this.bar = bar;
    }

}



