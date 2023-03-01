package com.example.befikar

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setUpViews()
        callHelpline1()
        callHelpline2()
        callMainTask1()

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = db.collection("user").document(userId)

        ref.get().addOnSuccessListener {
            if(it!=null){
                val name = it.data?.get("name")?.toString()
                val email = it.data?.get("email")?.toString()

                val userName : TextView = findViewById(R.id.userName)
                val userEmail : TextView = findViewById(R.id.userMail)
                userName.text = name
                userEmail.text = email

            }
        }
            .addOnFailureListener {
                Toast.makeText(this,"Failed to fetch data",Toast.LENGTH_SHORT).show()
            }


    }

    private fun setUpViews(){
        setUpDrawerLayout()
    }

    private fun setUpDrawerLayout(){
        //you can directly use the id without using R.id.id_name
        setSupportActionBar(appBar)
        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener {

            it.isChecked = true
            drawerLayout.closeDrawer(GravityCompat.START)
            when(it.itemId){

                R.id.home->{
                    val intent = Intent(applicationContext,Home::class.java)
                    startActivity(intent)
                }
                R.id.operation ->  replaceFragment(OperationFragment())
                R.id.aboutus -> replaceFragment(AboutFragment())
                R.id.logout -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this,LoginActivity::class.java)
                    startActivity(intent)
                }
                R.id.feedback -> replaceFragment(FeedbackFragment())
                R.id.contactus -> replaceFragment(ContactFragment())
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun callHelpline1(){

        val cardView1 : CardView = findViewById(R.id.helplineCard1)
        cardView1.setOnClickListener{

            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:7827-170-170")
            startActivity(intent)
        }
    }

    private fun callHelpline2(){

        val cardView2 : CardView = findViewById(R.id.helplineCard2)
        cardView2.setOnClickListener{

            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:112")
            startActivity(intent)
        }
    }

    private fun replaceFragment(fragment: Fragment){

        val rootView : ConstraintLayout = findViewById(R.id.rootView)
        rootView.removeAllViews()
        val fragmentManager : FragmentManager = supportFragmentManager
        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
    }

    private fun callMainTask1(){

        val mainTask1 : CardView = findViewById(R.id.mainTask1)
        mainTask1.setOnClickListener {
            val intent = Intent(applicationContext,MainTask1::class.java)
            startActivity(intent)
        }
    }
}