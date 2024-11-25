package com.example.task4;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task4.database.User;
import com.example.task4.database.UserDatabase;
import com.example.task4.ui.UserAdapter;
import com.example.task4.worker.UserWorker;

import java.util.ArrayList;
import java.util.List;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(new ArrayList<>());
        recyclerView.setAdapter(userAdapter);

        // Получение данных из базы
        UserDatabase.getDatabase(this).userDao().getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                userAdapter.setUserList(users);
            }
        });
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(UserWorker.class).build();
        WorkManager.getInstance(this).enqueue(workRequest);
    }
}