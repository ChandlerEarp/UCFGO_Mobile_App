package com.example.ucfgo_mobile_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import com.example.ucfgo_mobile_app.Retrofit.IMyService;
import com.example.ucfgo_mobile_app.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    TextView text_create_account;
    MaterialEditText edit_login_email, edit_login_password;
    Button button_login;
    String email, name;



    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    @Override
    protected void onStop(){
        compositeDisposable.clear();
        super.onStop();
    }

    public void launchHome(){
        //Open the Settings Page
        Intent i = new Intent(this, Home.class);
        i.putExtra("email", email);
        i.putExtra("name", name);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        //Init view
        edit_login_email = (MaterialEditText)findViewById(R.id.email);
        edit_login_password = (MaterialEditText)findViewById(R.id.password);

        button_login = (Button) findViewById(R.id.loginButton);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(edit_login_email.getText().toString(),
                        edit_login_password.getText().toString());
                email = (edit_login_email.getText().toString());
                grabUserInfo(email);
            }
        });
        text_create_account = (TextView) findViewById(R.id.SignupButton);
        text_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View register_layout = LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.register_layout, null);
                new MaterialStyledDialog.Builder(MainActivity.this)
                        .setTitle("REGISTRATION")
                        .setDescription("Please fill out all fields")
                        .setCustomView(register_layout)
                        .setNegativeText("CANCEL")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveText("REGISTER")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                MaterialEditText register_email = (MaterialEditText)register_layout.findViewById(R.id.email);
                                MaterialEditText register_password = (MaterialEditText)register_layout.findViewById(R.id.password);
                                MaterialEditText register_name = (MaterialEditText)register_layout.findViewById(R.id.name);

                                if(TextUtils.isEmpty(register_email.getText().toString())){
                                    Toast.makeText(MainActivity.this,"Email cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if(TextUtils.isEmpty(register_password.getText().toString())){
                                    Toast.makeText(MainActivity.this,"Password cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if(TextUtils.isEmpty(register_name.getText().toString())){
                                    Toast.makeText(MainActivity.this,"Name cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                registerUser(register_email.getText().toString(),
                                        register_password.getText().toString(),
                                        register_name.getText().toString());

                            }
                        }).show();
            }
        });

    }

    private void registerUser(String email, String password, String name) {
        compositeDisposable.add(iMyService.registerUser(email,name,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(MainActivity.this, ""+response, Toast.LENGTH_SHORT).show();

                    }
                }));

    }

    private void loginUser(String email, String password) {
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Email cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Password cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }
        compositeDisposable.add(iMyService.loginUser(email,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(MainActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                        launchHome();
                    }
                }));

    }
    public void grabUserInfo(String email){
        String name = null;
        System.out.println("Email: " + email);
        compositeDisposable.add(iMyService.grabName(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        System.out.println("Success " + response);
                        setName(response);
                    }
                }));

    }
    public void setName(String response){
        name = response;
        System.out.println(getName());
    }
    public String getName(){
        return name;
    }
//    public String getName()
//    {
//        String arr[] = name.split("", 2);
//        return arr[0];
//    }
}