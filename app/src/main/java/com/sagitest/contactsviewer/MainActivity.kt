package com.sagitest.contactsviewer

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

//for reference: https://youtu.be/Sxf04knGhJE

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    var cols = listOf<String>(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone._ID
    ).toTypedArray()

    lateinit var rs: Cursor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, Array(1) {Manifest.permission.READ_CONTACTS}, 111)
        } else {readContact()}

        listView1.setOnItemClickListener(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){readContact()}
    }

    private fun readContact() {
        val from = listOf<String>(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        ).toTypedArray()

        val to = intArrayOf(android.R.id.text1, android.R.id.text2)

        //the last argument is for sorting. for sort by name, use ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        //for sort by number, use ContactsContract.CommonDataKinds.Phone.NUMBER
        rs = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            cols, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)!!

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
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)!!
                adapterCustom.changeCursor(rs)
                return false
            }
        })
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        rs.moveToPosition(position)
        var cliper = "Name: " + rs.getString(0) +  "\n" + "Phone No. : " + rs.getString(1)

        val clb : ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip : ClipData = ClipData.newPlainText("EditText", cliper)
        clb.setPrimaryClip(clip)

        Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show()
    }
}




