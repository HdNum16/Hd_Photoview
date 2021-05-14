package com.example.photoview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteImageActivity extends AppCompatActivity {

    DatabaseHandler dbHandler;
    RecyclerView rvUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_image);

        dbHandler = new DatabaseHandler(FavoriteImageActivity.this);
        rvUsers = (RecyclerView) findViewById(R.id.rv_users);
        rvUsers.setLayoutManager(new GridLayoutManager(this, 3));
        rvUsers.setAdapter(new imageInfoAdapter(dbHandler.getAllImages(), FavoriteImageActivity.this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        rvUsers.setAdapter(new imageInfoAdapter(dbHandler.getAllImages(), FavoriteImageActivity.this));
    }
}
