package com.example.task4.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.task4.database.User;
import com.example.task4.database.UserDatabase;
import com.example.task4.network.ApiService;
import com.example.task4.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class UserWorker extends Worker {

    public UserWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<User>> call = apiService.getUsers();

        try {
            Response<List<User>> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                UserDatabase.getDatabase(getApplicationContext())
                        .userDao()
                        .insertAll(response.body());
                return Result.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.retry();
    }
}