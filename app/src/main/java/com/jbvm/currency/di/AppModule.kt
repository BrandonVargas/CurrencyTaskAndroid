package com.jbvm.currency.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jbvm.currency.data.local.AppDatabase
import com.jbvm.currency.data.local.SymbolDao
import com.jbvm.currency.data.remote.CurrencyRemoteDataSource
import com.jbvm.currency.data.remote.Webservice
import com.jbvm.currency.data.repository.CurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson) : Retrofit = Retrofit.Builder()
        .baseUrl("http://data.fixer.io/api/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences = appContext.getSharedPreferences("TIMESTAMP", Context.MODE_PRIVATE)

    @Provides
    fun provideWebService(retrofit: Retrofit): Webservice = retrofit.create(Webservice::class.java)

    @Singleton
    @Provides
    fun provideCurrencyRemoteDataSource(webService: Webservice) = CurrencyRemoteDataSource(webService)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideSymbolDao(db: AppDatabase) = db.symbolDao()

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: CurrencyRemoteDataSource,
                          localDataSource: SymbolDao,
                          sharedPreferences: SharedPreferences
    ) =
        CurrencyRepository(remoteDataSource, localDataSource, sharedPreferences)
}