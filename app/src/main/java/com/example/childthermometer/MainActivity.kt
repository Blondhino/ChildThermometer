package com.example.childthermometer

import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        button_reset.setOnClickListener { v.cancel()
        warningWindow.visibility=View.GONE
            settings.visibility=View.VISIBLE
        }
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("temperature")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value =
                    dataSnapshot.getValue(Float::class.java)!!
                Log.d("temperature",value.toString())
                temptText2.text= "$value °C"
                if(value>36.99){
                    alarmUser(v)
                    warningWindow.visibility=View.VISIBLE
                    tempText.text= "$value °C"
                    settings.visibility=View.GONE
                }
                else{
                    warningWindow.visibility=View.GONE
                    settings.visibility=View.VISIBLE
                    v.cancel()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }


    private fun alarmUser(v: Vibrator) {
        val pattern = longArrayOf(0, 500, 100)
        v.vibrate(pattern, 0)
    }
}