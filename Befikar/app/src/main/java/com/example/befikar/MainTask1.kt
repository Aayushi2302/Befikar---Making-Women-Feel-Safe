package com.example.befikar

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.icu.text.Bidi.invertMap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.*
import androidx.core.view.size
import com.google.android.gms.common.util.MapUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.protobuf.MapEntryLite
import kotlinx.android.synthetic.main.activity_main_task1.*
import kotlinx.android.synthetic.main.list_item.*
import java.io.File
import java.io.FileOutputStream

class MainTask1 : AppCompatActivity() {

    lateinit var listView : ListView
    lateinit var addContactsBtn : Button
    lateinit var temporary : HashMap<String,String>
    lateinit var contactList : ArrayList<HashMap<String,String>>
    lateinit var tmpList : ArrayList<HashMap<String,String>>
    lateinit var adapter : SimpleAdapter
    lateinit var sampleAdadpter : SimpleAdapter




    private var db = Firebase.firestore
    val userId = FirebaseAuth.getInstance().currentUser!!.uid


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_task1)

        listView = findViewById(R.id.listView)
        addContactsBtn = findViewById(R.id.addContacts)
        temporary = HashMap()
        contactList = ArrayList()
        tmpList = ArrayList()
        adapter = SimpleAdapter(this,contactList,R.layout.list_item,
        arrayOf("Name","Number"),intArrayOf(R.id.contactName,R.id.contactNumber))




        /*setSupportActionBar(appBarMainTask1)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)*/

        loadEmergencyContacts()

        addContacts.setOnClickListener{
            if(tmpList.size < 3){
                var i = Intent(Intent.ACTION_PICK)
                i.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
                startActivityForResult(i, 111)
            }
            else{
                Toast.makeText(this,"Cannot add more than 3 emergency contacts",Toast.LENGTH_SHORT).show()
            }

        }

     

    }


    private fun loadEmergencyContacts() {
        val rootRef : FirebaseFirestore = FirebaseFirestore.getInstance()
        val applicationRef : CollectionReference = rootRef.collection("emergency")

        val applicationIdRef : DocumentReference = applicationRef.document(userId)
        applicationIdRef.get().addOnCompleteListener {task ->
            if (task.isSuccessful)
            {
                val document: DocumentSnapshot = task.result

                if(document.exists())
                {

                    tmpList = document.get("emergency contacts") as ArrayList<HashMap<String, String>>
                    contactList = ArrayList()
                    sampleAdadpter = SimpleAdapter(this,contactList,R.layout.list_item,arrayOf("Number","Name"),
                        intArrayOf(R.id.contactNumber,R.id.contactName))


                    for(element in tmpList){
                        val resultMap : HashMap<String,String> = HashMap()
                        for(ele in element){
                            resultMap.put(ele.key,ele.value)
                        }
                        contactList.add(resultMap)
                    }
                    listView.adapter = sampleAdadpter
                }
            }
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

                temporary.put(rs.getString(1),rs.getString(0))

                 val itr  = temporary.iterator()
                while(itr.hasNext()){

                    val resultMap : HashMap<String,String> = HashMap()
                    val pair : Map.Entry<String,String> = itr.next()

                    resultMap.put("Name", pair.key)
                    resultMap.put("Number", pair.value)

                    contactList.add(resultMap)
                    tmpList.add(resultMap)
                    storeEmergencyContacts()

                }
                temporary.clear()
                listView.adapter = adapter

            }
        }
    }

    private fun storeEmergencyContacts(){

        val userMap = hashMapOf(
            "emergency contacts" to contactList
        )
        db.collection("emergency").document(userId).set(userMap)
            .addOnSuccessListener {
                Toast.makeText(this,"Successfully data added!!",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this,"Failed to store data!!",Toast.LENGTH_SHORT).show()
            }
    }




    /*override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true

        return super.onSupportNavigateUp()
    }*/


}