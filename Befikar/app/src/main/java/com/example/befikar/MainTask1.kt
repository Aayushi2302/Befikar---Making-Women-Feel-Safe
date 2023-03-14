package com.example.befikar

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main_task1.*

class MainTask1 : AppCompatActivity() {

    lateinit var listView : ListView
    lateinit var temporary : HashMap<String,String>
    lateinit var contactList : ArrayList<HashMap<String,String>>
    lateinit var tmpList : ArrayList<HashMap<String,String>>
    lateinit var sampleAdadpter : SimpleAdapter

    private var db = Firebase.firestore
    val userId = FirebaseAuth.getInstance().currentUser!!.uid

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_task1)

        listView = findViewById(R.id.listView)
        temporary = HashMap()
        contactList = ArrayList()
        tmpList = ArrayList()

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

        deleteBtn.setOnClickListener{
            if(tmpList.isEmpty()){
                Toast.makeText(this,"No entries to delete",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(deleteText.text?.length == 10)
            {
                val str : String = deleteText.text.toString()
                for(element in tmpList){

                    for(ele in element){

                        if(ele.value == str){
                            val index = tmpList.indexOf(element)
                            val deleteContact = db.collection("emergency").document(userId)
                            deleteContact.update("emergency contacts",FieldValue.arrayRemove(tmpList.get(index)))
                            loadEmergencyContacts()
                            Toast.makeText(this,"Contact Deleted Successfully",Toast.LENGTH_SHORT).show()

                        }
                    }
                }
            }else{
                Toast.makeText(this,"Enter Valid Number",Toast.LENGTH_SHORT).show()
            }
            deleteText.text = null
        }

        setSupportActionBar(appBarMainTask1)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarMainTask1.setNavigationOnClickListener {
                finish()
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

                var str : String = rs.getString(0)
                str = str.replace(" ","")
                if(str[0] == '+'){
                    str = str.substring(3)
                    temporary.set(rs.getString(1),str)
                }
                else if(str.length > 10){
                    Toast.makeText(this,"Enter 10 digits number only",Toast.LENGTH_SHORT).show()
                    return
                }
                for(element in tmpList){
                    for(ele in element){
                        if(ele.value == str){
                            Toast.makeText(this,"This number is already added",Toast.LENGTH_SHORT).show()
                            return
                        }

                    }
                }
                
                //TODO : Add the check for special character in mobile number

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
                listView.adapter = sampleAdadpter

            }
        }
    }

    private fun storeEmergencyContacts(){

        val userMap = hashMapOf(
            "emergency contacts" to contactList
        )
        db.collection("emergency").document(userId).set(userMap)
            .addOnSuccessListener {
                Toast.makeText(this,"Contact added successfully",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this,"Failed to store contact",Toast.LENGTH_SHORT).show()
            }
    }

}