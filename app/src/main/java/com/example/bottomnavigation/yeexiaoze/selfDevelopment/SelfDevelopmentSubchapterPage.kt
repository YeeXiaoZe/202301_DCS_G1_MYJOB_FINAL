package com.example.bottomnavigation.yeexiaoze.selfDevelopment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomnavigation.yeexiaoze.database.selfDevelopment.SubchapterModel
import com.example.bottomnavigation.databinding.SelfDevelopmentSubchapterPageBinding
import java.io.Serializable

@Suppress("DEPRECATION")
class SelfDevelopmentSubchapterPage : AppCompatActivity() {
    //Set binding variables
    private lateinit var binding: SelfDevelopmentSubchapterPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SelfDevelopmentSubchapterPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Retrieve the subchapter records
        val subchapterList = intent.getSerializableExtra("subchapterList")
        var position = 0

        if (subchapterList != null) {

            //Log.i("Main Activity", "$subchapterList")
            setContent(subchapterList, position)
        }

        binding.backButton.setOnClickListener()
        {
            position--
            setContent(subchapterList, position)
        }

        binding.forwardButton.setOnClickListener()
        {
            position++
            setContent(subchapterList, position)
        }
    }

    private fun setContent(serializable: Serializable?, position: Int) {
        val subchapterList = serializable as? ArrayList<SubchapterModel>

        //Decide to whether proceed to content setting
        if (!changePage(subchapterList, position))
        {
            return
        }

        binding.selfDevTitle.text = subchapterList?.get(position)?.title
        binding.selfDevContent.text = subchapterList?.get(position)?.content
        binding.selfDevLink.text = subchapterList?.get(position)?.link

        binding.selfDevLink.setOnClickListener{
            if (!subchapterList?.get(position)?.link.isNullOrEmpty()) {
                addLink(subchapterList?.get(position)?.link)
            }
        }
    }

    private fun changePage(subchapterList: ArrayList<SubchapterModel>?, position: Int): Boolean
    {

        if (position == -1)
        {
            //Go back to previous activity
            onBackPressed()

            return false
        }
        else if (position == subchapterList?.size)
        {
            //Go back to previous activity
            onBackPressed()

            return false
        }

        return true
    }

    private fun addLink(link: String?)
    {
        //Intent for web search
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }
}