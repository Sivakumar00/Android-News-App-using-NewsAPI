package siva.com.newsapp.Interface;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import siva.com.newsapp.Model.IconBetterIdea;

/**
 * Created by MANIKANDAN on 16-11-2017.
 */

public interface IconBetterIdeaService {
    @GET
    Call<IconBetterIdea> getIconUrl(@Url String url);

}
