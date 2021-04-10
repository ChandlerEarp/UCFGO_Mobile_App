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

public class GarageA extends AppCompatActivity {

    IMyService iMyService;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);
        setTitle("Garage A");

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        String garageLetter = "A";
        compositeDisposable.add(iMyService.garageOccupancy(garageLetter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println(s);

                    }
                }));

        ProgressBar barA;
        barA = findViewById(R.id.progressBarA);
        TextView percentA;
        percentA = findViewById(R.id.percentageA);
        if(barA !=null)
            percentA.setText("" + barA.getProgress() + "%");
    }
}