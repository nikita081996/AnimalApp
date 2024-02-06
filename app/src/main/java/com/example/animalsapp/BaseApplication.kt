package com.example.animalsapp

import android.app.Application
import android.content.Context

class BaseApplication : Application() {

    init {
        mContext = this
    }
    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

    companion object {
        private var mContext: BaseApplication? = null

        fun getContext(): Context? {
            return mContext?.applicationContext
        }
    }


}