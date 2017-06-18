package info.metadude.android.battery.api

import com.squareup.moshi.Moshi
import info.metadude.android.battery.map.adapters.EnergyLevelAdapter
import okhttp3.OkHttpClient
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object Api {

    fun provideScootersService(baseUrl: String,
                               okHttpClient: OkHttpClient): ScootersService =
            createRetrofit(baseUrl, okHttpClient)
                    .create(ScootersService::class.java)

    private fun createRetrofit(baseUrl: String,
                               okHttpClient: OkHttpClient): retrofit2.Retrofit {
        return retrofit2.Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi()))
                .client(okHttpClient)
                .build()
    }

    private fun moshi() = Moshi.Builder()
            .add(EnergyLevelAdapter())
            .build()

}
