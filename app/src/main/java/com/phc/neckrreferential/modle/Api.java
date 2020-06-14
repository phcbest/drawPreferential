package com.phc.neckrreferential.modle;

import com.phc.neckrreferential.modle.domain.Categories;

import retrofit2.Call;
import retrofit2.http.GET;

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


}
