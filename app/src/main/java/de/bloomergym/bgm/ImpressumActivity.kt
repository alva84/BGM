package de.bloomergym.bgm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson

class ImpressumActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener  {

    private lateinit var mBtmView: BottomNavigationView
    private lateinit var myConfig: MyConfig
    private lateinit var helper:Helper
    private lateinit var linearLayout:LinearLayout;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_impressum)

        //make use of bottom navigation bar
        mBtmView = findViewById(R.id.nav_view)
        mBtmView.selectedItemId = R.id.navigation_impressum
        mBtmView.setOnNavigationItemSelectedListener(this)
        helper = Helper(applicationContext)
        linearLayout =  findViewById(R.id.linear_layout_impressum)

        setupUI()
    }

    fun setupUI(){

        helper.setTextViewRobotoLight(assets, linearLayout, R.id.header_impressum)
        helper.setTextSizeBig(applicationContext, linearLayout, R.id.header_impressum)
        //setTextSizeBig(R.id.header_impressum)

        helper.setTextViewRobotoLight(assets, linearLayout, R.id.impressum1)
        helper.setTextSizeMedium(applicationContext, linearLayout, R.id.impressum1)

        helper.setTextViewRobotoLight(assets, linearLayout, R.id.impressum1_1)
        helper.setTextSizeSmall(applicationContext, linearLayout, R.id.impressum1_1)

        helper.setTextViewRobotoLight(assets, linearLayout, R.id.impressum2)
        helper.setTextSizeMedium(applicationContext, linearLayout, R.id.impressum2)

        helper.setTextViewRobotoLight(assets, linearLayout, R.id.impressum2_1)
        helper.setTextSizeSmall(applicationContext, linearLayout, R.id.impressum2_1)

        helper.setTextViewRobotoLight(assets, linearLayout, R.id.impressum3)
        helper.setTextSizeMedium(applicationContext, linearLayout, R.id.impressum3)

        helper.setTextViewRobotoLight(assets, linearLayout, R.id.impressum3_1)
        helper.setTextSizeSmall(applicationContext, linearLayout, R.id.impressum3_1)

        helper.setTextViewRobotoLight(assets, linearLayout, R.id.impressum4)
        helper.setTextSizeMedium(applicationContext, linearLayout, R.id.impressum4)

        helper.setTextViewRobotoLight(assets, linearLayout, R.id.impressum4_1)
        helper.setTextSizeSmall(applicationContext, linearLayout, R.id.impressum4_1)

        helper.setTextViewRobotoLight(assets, linearLayout, R.id.impressum5)
        helper.setTextSizeMedium(applicationContext, linearLayout, R.id.impressum5)

        helper.setTextViewRobotoLight(assets, linearLayout, R.id.impressum5_1)
        helper.setTextSizeSmall(applicationContext, linearLayout, R.id.impressum5_1)

        helper.setTextViewRobotoLight(assets, linearLayout, R.id.impressum6)
        helper.setTextSizeMedium(applicationContext, linearLayout, R.id.impressum6)

        helper.setTextViewRobotoLight(assets, linearLayout, R.id.impressum6_1)
        helper.setTextSizeSmall(applicationContext, linearLayout, R.id.impressum6_1)

        helper.setTextViewRobotoLight(assets, linearLayout, R.id.impressum7)
        helper.setTextSizeMedium(applicationContext, linearLayout, R.id.impressum7)

        helper.setTextViewRobotoLight(assets, linearLayout, R.id.impressum7_1)
        helper.setTextSizeSmall(applicationContext, linearLayout, R.id.impressum7_1)
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        Log.i("Kotlin", "called ImpressumActivity > onNaviagtionItemSelected. Selected item was "+ p0.itemId)

        var i : Intent? = null

        if (p0.itemId == R.id.navigation_home){
            i = Intent(this, MainActivity::class.java)
        }
        else if (p0.itemId == R.id.navigation_team){
            i = Intent(this, TeamActivity::class.java)
        }
        else if (p0.itemId == R.id.navigation_impressum){
            //i = Intent(this, ImpressumActivity::class.java);
        }

        if (i != null) {
            startActivity(i)
            return true
        }
        else {
            return false
        }
    }

    fun goToConfig(v: View) {
        val i = Intent(this, ConfigActivity::class.java)
        startActivity(i)
    }

    fun toggleCompany(v: View) {

        myConfig = helper.loadConfig()
        //change value locally and in file
        if (myConfig.currentCompany == "flughafen"){
            myConfig.currentCompany = "fraunhofer"
        }
        else if (myConfig.currentCompany == "fraunhofer"){
            myConfig.currentCompany = "flughafen"
        }
        val gson = Gson()
        val jsonString = gson.toJson(myConfig)

        helper.writeConfig(applicationContext,jsonString)

        // re-create UI
        // setupUI()
    }
}