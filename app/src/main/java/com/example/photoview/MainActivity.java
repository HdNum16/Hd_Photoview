package com.example.photoview;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    MaterialSearchView searchView;
    MenuItem item_favorite;
    String default_image_url = "https://pixabay.com/api/?key=20691192-b92dda33039876d2c2158f9e7&q=pink+flower&image_type=photo&pretty=true";
    String first_half_url = "https://pixabay.com/api/?key=20691192-b92dda33039876d2c2158f9e7&q=";
    String second_half_url = "&image_type=photo&pretty=true";
    String image_url = default_image_url;
    Boolean searched = false;

    RecyclerView rvUsers;
    DatabaseHandler dbHandler;
    Request request;
    OkHttpClient client;
    Moshi moshi;
    Type usersType;
    JsonAdapter<List<imageInfo>> jsonAdapter;

    void search_image(String image_url) {
        request = new Request.Builder()
                .url(image_url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Error", "Network Error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String json = response.body().string();
                try {
                    JSONObject jo = new JSONObject(json);
                    json = jo.getString("hits");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final List<imageInfo> users = jsonAdapter.fromJson(json);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rvUsers.setAdapter(new imageInfoAdapter(users, MainActivity.this));
                    }
                });
            }
        });
        searched = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvUsers = (RecyclerView) findViewById(R.id.rv_users);
//        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setLayoutManager(new GridLayoutManager(this, 3));
        dbHandler = new DatabaseHandler(MainActivity.this);
        rvUsers.setAdapter(new imageInfoAdapter(dbHandler.getAllImages(), MainActivity.this));

        client = new OkHttpClient();

        moshi = new Moshi.Builder().build();
        usersType = Types.newParameterizedType(List.class, imageInfo.class);
        jsonAdapter = moshi.adapter(usersType);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                image_url = first_half_url + query.replace(' ', '+') + second_half_url;
                search_image(image_url);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!searched) {
            rvUsers.setAdapter(new imageInfoAdapter(dbHandler.getAllImages(), MainActivity.this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem item_search = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item_search);

        item_favorite = menu.findItem(R.id.action_favorite);
        item_favorite.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, FavoriteImageActivity.class);
                startActivity(intent);
                return true;
            }
        });
        return true;
    }
}
