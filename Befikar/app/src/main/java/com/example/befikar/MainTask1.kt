package com.example.befikar

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.*
import com.google.protobuf.MapEntryLite
import kotlinx.android.synthetic.main.activity_main_task1.*
import java.io.File
import java.io.FileOutputStream

class MainTask1 : AppCompatActivity() {

    lateinit var listView : ListView
    lateinit var addContactsBtn : Button
    lateinit var temporary : HashMap<String,String>
    lateinit var contactList : ArrayList<HashMap<String,String>>
    lateinit var adapter : SimpleAdapter


   /* val path : File = applicationContext.filesDir
    private val writer = FileOutputStream(File(path,"emergencyContacts.txt"))*/


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_task1)

        listView = findViewById(R.id.listView)
        addContactsBtn = findViewById(R.id.addContacts)
        temporary = HashMap()
        contactList = ArrayList()
        adapter = SimpleAdapter(this,contactList,R.layout.list_item,
        arrayOf("First Line","Second Line"),intArrayOf(R.id.contactName,R.id.contactNumber))


        /*setSupportActionBar(appBarMainTask1)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)*/

        addContacts.setOnClickListener{
            var i = Intent(Intent.ACTION_PICK)
            i.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            startActivityForResult(i, 111)
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 111 && resultCode == Activity.RESULT_OK ){

            var contacturi : Uri = data?.data ?: return
            var cols : Array<String> = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER,
                                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            var rs = contentResolver.query(contacturi,cols,null,null,null)
            if(rs?.moveToFirst()!!){
                /*Toast.makeText(this,rs.getString(0),Toast.LENGTH_SHORT).show()
                Toast.makeText(this,rs.getString(1),Toast.LENGTH_SHORT).show()*/
                temporary.put(rs.getString(1),rs.getString(0))

                 val itr  = temporary.iterator()
                while(itr.hasNext()){

                    val resultMap : HashMap<String,String> = HashMap()
                    val pair : Map.Entry<String,String> = itr.next()

                    resultMap.put("First Line", pair.key)
                    resultMap.put("Second Line", pair.value)
                    if(contactList.size < 5){
                        contactList.add(resultMap)
                        //storeInFile(pair)
                    }else{
                        //writer.close()
                        Toast.makeText(this,"Cannot add more then 5 emergency contacts",Toast.LENGTH_SHORT).show()
                    }

                }
                temporary.clear()
                listView.adapter = adapter

            }
        }
    }

    /*override fun onDestroy() {
        val path : File = applicationContext.filesDir
        try{
            val writer = FileOutputStream(File(path,"emergencyContacts.txt"))
            val iterator  = contactList.iterator()
            while(iterator.hasNext()){
                val tempMap : HashMap<String,String> = iterator.next()

            }
            writer.close()

        }catch(exp : Exception){
            exp.printStackTrace()
        }
        super.onDestroy()
    }*/

    /*private fun storeInFile(pair : Map.Entry<String,String>){
        val item = pair.key + ":" + pair.value
        writer.write(item.toByteArray())
    }

    fun loadContent(){
        val readFrom : File = File(path,"emergencyContacts.txt")
        val content = ByteArray(readFrom)
    }*/

    /*override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true

        return super.onSupportNavigateUp()
    }*/
}