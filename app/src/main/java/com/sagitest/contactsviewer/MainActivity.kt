package com.sagitest.contactsviewer

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

//for reference: https://youtu.be/Sxf04knGhJE

class MainActivity : AppCompatActivity() {

    var cols = listOf<String>(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone._ID
    ).toTypedArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, Array(1) {Manifest.permission.READ_CONTACTS}, 111)
        } else {readContact()}
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){readContact()}
    }

    //the last argument is for sorting. for sort by name, use ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
    //for sort by number, use ContactsContract.CommonDataKinds.Phone.NUMBER
    private fun readContact() {
        val from = listOf<String>(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        ).toTypedArray()

        val to = intArrayOf(android.R.id.text1, android.R.id.text2)

        var rs = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            cols, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

        //to check if ur function can read a contact
        //if(rs?.moveToNext()!!) {Toast.makeText(applicationContext, rs.getString(0), Toast.LENGTH_LONG).show() }

        val adapterCustom = SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, rs, from, to, 0)
        listView1.adapter = adapterCustom

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                 rs = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    cols,
                    "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ?",
                    Array(1){"%$p0%"},
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                adapterCustom.changeCursor(rs)
                return false
            }
        })
    }
}








/*
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity"
    android:background="#121212">

    <Button
        android:id="@+id/btn_rc"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Read Contact"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintHorizontal_bias="0.454"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.023" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rcv"
        android:background="#D6D6D6"
        android:layout_width="match_parent"
        android:layout_height="600dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.0"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.725" />

</androidx.constraintlayout.widget.ConstraintLayout>
 */








/*
package com.sagitest.contactsviewer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    class ContactAdapter(items : List<String>, ctx : Context) : RecyclerView.Adapter<CountryAdapter.ViewHolder>(){
        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }

        override fun onBindViewHolder(holder: CountryAdapter.ViewHolder, position: Int, ) {
            super.onBindViewHolder(holder, position, payloads)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CountryAdapter.ViewHolder {
            TODO("Not yet implemented")
        }

        class ViewHolder(v: View) : RecyclerView.ViewHolder(v){

        }
    }
}
 */