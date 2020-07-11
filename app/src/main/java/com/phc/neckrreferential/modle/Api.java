package com.phc.neckrreferential.modle;

import com.phc.neckrreferential.modle.domain.Categories;
import com.phc.neckrreferential.modle.domain.HomePagerContent;
import com.phc.neckrreferential.modle.domain.OnSellContent;
import com.phc.neckrreferential.modle.domain.SearchRecommend;
import com.phc.neckrreferential.modle.domain.SearchResult;
import com.phc.neckrreferential.modle.domain.SelectedContent;
import com.phc.neckrreferential.modle.domain.SelectedPageCategory;
import com.phc.neckrreferential.modle.domain.TicketParams;
import com.phc.neckrreferential.modle.domain.TicketResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * 版权：没有版权 看得上就用
 *
 * @author peng
 * 创建日期：2020/6/12 10
 * 描述：与baseUrl配合使用，使用retrofit来访问网络
 */
public interface Api {

    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET
    Call<HomePagerContent> getHomePagerContent(@Url String url);

    @POST("tpwd")
    Call<TicketResult> getTicket(@Body TicketParams ticketParams);

    @GET("recommend/categories")
    Call<SelectedPageCategory> getSelectedPageCategories();

    @GET
    Call<SelectedContent> getSelectedPageContent(@Url String url);

    @GET
    Call<OnSellContent>  getOnSellPageContent(@Url String url);

    @GET("search/recommend")
    Call<SearchRecommend> getRecommendWords();

    @GET("search")
    Call<SearchResult> doSearch (@Query("page")int page,@Query("keyword")String keyword);
}
