package com.example.bottomnavigation.phuameikee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.bottomnavigation.PostFragment
import com.example.bottomnavigation.R
import com.example.bottomnavigation.databinding.ActivityMainBinding
import com.example.bottomnavigation.harrychiong.FragmentHome
import com.example.bottomnavigation.kuanhanjet.UserSettings
import com.example.bottomnavigation.yeexiaoze.selfDevelopment.SelfDevelopmentMainPage

class MainActivity : AppCompatActivity() {

    private lateinit var navigationBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        navigationBinding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(navigationBinding.root)

        //Get the username from login activity
        val username = intent.getStringExtra("username")
        insertFragment(FragmentHome())

        navigationBinding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> insertFragment(FragmentHome())
                //go to a normal kotlin class file
                R.id.post -> insertFragment(PostFragment())
                R.id.build -> insertFragment(SelfDevelopmentMainPage())
                //Pass the username to profile activity to display it
                R.id.profile -> insertFragment(UserSettings().apply {
                    arguments = Bundle().apply {
                        putString("username", username)
                    }
                })

                else -> {

                }
            }
            true

        }


    }

    private fun insertFragment(fragment: Fragment) {
        val fragmentMan = supportFragmentManager
        val fragmentTrans = fragmentMan.beginTransaction()
        fragmentTrans.replace(R.id.frame_layout, fragment)
        fragmentTrans.commit()
    }

}