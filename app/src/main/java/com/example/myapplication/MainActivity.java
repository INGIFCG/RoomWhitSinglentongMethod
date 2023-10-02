package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.db.Contact;
import com.example.myapplication.db.ContactDatabase;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView title_name,title_lastname,title_age,title_email;
    EditText name,lastname,age,email;
    Button save_btn,clear_btn,get_all_contact_btn;
    ContactDatabase contacDB;
    List<Contact> contactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.name);
        lastname = findViewById(R.id.lastname);
        age = findViewById(R.id.age);
        email = findViewById(R.id.email);
        save_btn = findViewById(R.id.save_btn);
        clear_btn = findViewById(R.id.clear_btn);
        get_all_contact_btn= findViewById(R.id.get_all_contact_btn);

        save_btn.setOnClickListener(this);
        clear_btn.setOnClickListener(this);
        get_all_contact_btn.setOnClickListener(this);

        RoomDatabase.Callback myCallBack =new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                System.out.println("Se creo la base de dastos ");
            }

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
                System.out.println("HOLA SE ABRIO LA BASE DE DATOS");
            }
        };

        contacDB= Room.databaseBuilder(getApplicationContext(), ContactDatabase.class,
                "ContactDB").addCallback(myCallBack)
                .build();

    }
    @Override
    public void onClick(View view) {
        if(view.getId()== R.id.save_btn){
            String getname = name.getText().toString();
            String getLastname = lastname.getText().toString();
            String getAge = age.getText().toString();
            String getEmail = email.getText().toString();
            Contact c1 = new Contact(getname,getLastname,getAge,getEmail);

            addContactInBackGraund(c1);// funcion para guardar los contactos

        }
        else if(view.getId()== R.id.clear_btn){
            name.setText("");
            lastname.setText("");
            age.setText("");
            email.setText("");
            Toast.makeText(this, "Clear", Toast.LENGTH_SHORT).show();
        }else if(view.getId()== R.id.get_all_contact_btn){
            getContactListInBackgraund();
        }

    }  public void addContactInBackGraund(Contact contact){

        ExecutorService executor =  Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //backgraund task

                contacDB.getPersonaDao().insertContact(contact);//agrega a la persona a la base de datos

                //on finish task
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "The data have save successfully ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    public void getContactListInBackgraund(){

        ExecutorService executor =  Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //backgraund task
                contactList = contacDB.getPersonaDao().selectAllContact();

                //on finish task
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        StringBuilder stringBuilder = new StringBuilder();
                        for(Contact contc: contactList){
                            stringBuilder.append(contc.getId()+":"+contc.getName());
                            stringBuilder.append("\n");
                        }
                        String finaldata = stringBuilder.toString();
                        Toast.makeText(MainActivity.this, finaldata, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}