package com.example.contactlist

import android.app.Application
import com.example.contactlist.model.ContactDatabase

class App : Application() {

    lateinit var db: ContactDatabase

    override fun onCreate() {
        super.onCreate()
        db = ContactDatabase.getDatabase(this)
    }
}