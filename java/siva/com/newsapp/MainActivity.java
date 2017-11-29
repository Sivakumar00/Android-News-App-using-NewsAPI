package siva.com.newsapp;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import siva.com.newsapp.Adapter.ListSourceAdapter;
import siva.com.newsapp.Common.Common;
import siva.com.newsapp.Interface.NewsService;
import siva.com.newsapp.Model.WebSite;

public class MainActivity extends AppCompatActivity {

    RecyclerView listWebsite;
    RecyclerView.LayoutManager layoutManager;
    NewsService mService;
    ListSourceAdapter adapter;
    SpotsDialog dialog;
    SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init cache
        Paper.init(this);
        //init service
        mService= Common.getNewsService();

        //init View
        swipeLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWebsiteSource(true);
            }
        });
        listWebsite=(RecyclerView)findViewById(R.id.list_source);
        listWebsite.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        listWebsite.setLayoutManager(layoutManager);

        dialog=new SpotsDialog(this);
        if(Function.isNetworkAvailable(getApplicationContext()))
        loadWebsiteSource(false);
        else
            Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();


    }

    private void loadWebsiteSource(boolean isRefreshed) {

        if(!isRefreshed){
            String cache=Paper.book().read("cache");
            if(cache!=null && !cache.isEmpty()){
                WebSite website=new Gson().fromJson(cache,WebSite.class);
                adapter=new ListSourceAdapter(getBaseContext(),website);
                adapter.notifyDataSetChanged();
                listWebsite.setAdapter(adapter);

            }
            else{
                 dialog.show();
                mService.getSources().enqueue(new Callback<WebSite>() {
                    @Override
                    public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                        adapter=new ListSourceAdapter(getBaseContext(),response.body());
                        adapter.notifyDataSetChanged();
                        listWebsite.setAdapter(adapter);

                        //save to cache
                        Paper.book().write("cache",new Gson().toJson(response.body()));

                    }

                    @Override
                    public void onFailure(Call<WebSite> call, Throwable t) {

                    }
                });
            }
        }else {
            dialog.show();
            mService.getSources().enqueue(new Callback<WebSite>() {
                @Override
                public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                    adapter=new ListSourceAdapter(getBaseContext(),response.body());
                    adapter.notifyDataSetChanged();
                    listWebsite.setAdapter(adapter);

                    //save to cache
                    Paper.book().write("cache",new Gson().toJson(response.body()));
                    //dismiss
                    swipeLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<WebSite> call, Throwable t) {

                }
            });
        }
    }
}
