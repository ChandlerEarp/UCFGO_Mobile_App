package com.example.ucfgo_mobile_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ucfgo_mobile_app.Retrofit.IMyService;
import com.example.ucfgo_mobile_app.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Home extends AppCompatActivity {

    IMyService iMyService;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        String email= null,name=null,result = null;
        Button button = findViewById(R.id.SettingsButton);
        ImageView qrImage = findViewById(R.id.QRCodeImage);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("email");
            name = extras.getString("name");
            if(name!= null)
                result = name.replaceAll("^[\"']+|[\"']+$", "");
            TextView welcome;
            welcome = findViewById(R.id.WelcomeText);
            System.out.println("Test:" + result);
            welcome.setText("Welcome "+ result);
        }

        qrImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                launchGarage(v);
            }
        } );
    }
    public void launchSettings(View v){
        //Open the Settings Page
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }
    public void launchGarage(View v){
        Intent i = new Intent(this, AvailableSpots.class);
        startActivity(i);
    }

//    public void grabUserInfo(String email){
//        String name = null;
//        compositeDisposable.add(iMyService.grabName(email)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String response) throws Exception {
//                        System.out.println("Success " + response);
//                        setName(""+response);
//                    }
//                }));
//
//    }
//    public void setName(String response){
//        name = response;
//        System.out.println(getName());
//    }
//    public String getName(){
//        return name;
//    }
}