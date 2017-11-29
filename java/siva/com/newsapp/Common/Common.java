package siva.com.newsapp.Common;

import android.util.Log;

import siva.com.newsapp.Interface.IconBetterIdeaService;
import siva.com.newsapp.Interface.NewsService;
import siva.com.newsapp.Model.IconBetterIdea;
import siva.com.newsapp.Remote.IconBetterIdeaClient;
import siva.com.newsapp.Remote.RetrofitClient;

/**
 * Created by MANIKANDAN on 16-11-2017.
 */

public class Common {

    private static final String BASE_URL="https://newsapi.org/";
    public static final String API_KEY="**your api key**";

    public static NewsService getNewsService(){
        return RetrofitClient.getClient(BASE_URL).create(NewsService.class);

    }

    public static IconBetterIdeaService getIconService(){
        return IconBetterIdeaClient.getClient().create(IconBetterIdeaService.class);

    }

    public static String getAPIUrl(String source,String sortBy,String apiKEY){
        StringBuilder apiUrl=new StringBuilder("https://newsapi.org/v1/articles?source=");
        String sample=apiUrl.append(source)
                .append("&sortBy=")
                .append(sortBy)
                .append("&apiKey=")
                .append(apiKEY)
                .toString();
        Log.e("ERADFADFADFAE",sample);
        return sample;
    }

}
