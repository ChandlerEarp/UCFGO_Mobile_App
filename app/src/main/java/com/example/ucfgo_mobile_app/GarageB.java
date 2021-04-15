package com.example.ucfgo_mobile_app;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ucfgo_mobile_app.Retrofit.IMyService;
import com.example.ucfgo_mobile_app.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class GarageB extends AppCompatActivity {
    IMyService iMyService;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    float occupancy = 0;
    float totalOccupancy = 0;
    ProgressBar barB;
    TextView percentB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_b);
        setTitle("Garage B");

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        String garageLetter = "B";
        compositeDisposable.add(iMyService.garageOccupancy(garageLetter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println(s);
                        totalOccupancy = calculatePercentage(s);
                    }
                })
        );
        compositeDisposable.add(iMyService.garageTrue(garageLetter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        occupancy = (calculatePercentage(s)/totalOccupancy)*100;
                        barB = findViewById(R.id.progressBarB);
                        barB.setProgress((int)occupancy);
                        percentB = findViewById(R.id.percentageB);
                        if(barB !=null)
                            percentB.setText("" + barB.getProgress() + "%");
                    }
                }));

    }



    private int calculatePercentage(String s){
        int count = 0;
        //Calculate length of string
        for(int i = 0; i < s.length(); i++){
            Character temp = s.charAt(i);
            if(temp == '{'){
                count++;
            }
        }
        System.out.println(count);
        return count;
    }
}
