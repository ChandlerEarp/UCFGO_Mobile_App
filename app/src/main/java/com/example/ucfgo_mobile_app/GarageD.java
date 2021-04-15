package com.example.ucfgo_mobile_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ucfgo_mobile_app.Retrofit.IMyService;
import com.example.ucfgo_mobile_app.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static java.lang.Thread.sleep;

public class GarageD extends AppCompatActivity {

    IMyService iMyService;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    float occupancy = 0;
    float totalOccupancy = 0;
    ProgressBar barD;
    TextView percentD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_d);
        setTitle("Garage D");

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        String garageLetter = "D";
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
                        occupancy = calculatePercentage(s)*100;
                        barD = findViewById(R.id.progressBarD);
                        barD.setProgress((int)occupancy);
                        percentD = findViewById(R.id.percentageD);
                        if(barD !=null)
                            percentD.setText("" + barD.getProgress() + "%");
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