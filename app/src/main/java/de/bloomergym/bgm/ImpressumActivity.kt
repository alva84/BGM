package de.bloomergym.bgm

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson


class ImpressumActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener  {

    private lateinit var mBtmView: BottomNavigationView
    private lateinit var myConfig: MyConfig
    private lateinit var helper:Helper
    val config = "config.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_impressum)

        //make use of bottom navigation bar
        mBtmView = findViewById(R.id.nav_view)
        mBtmView.selectedItemId = R.id.navigation_impressum
        mBtmView.setOnNavigationItemSelectedListener(this)
        helper = Helper(applicationContext)

        setupUI()
    }

    fun setTextViewRobotoLight(res:Int){
        val robotoLight = Typeface.createFromAsset(assets, "Roboto-Light.ttf")
        val robotoLightItalic = Typeface.createFromAsset(assets, "Roboto-LightItalic.ttf")
        val textView: TextView = findViewById(res)
        textView.typeface = robotoLight
    }

    fun setTextViewRobotoLightItalic(res:Int){
        val robotoLightItalic = Typeface.createFromAsset(assets, "Roboto-LightItalic.ttf")
        val textView: TextView = findViewById(res)
        textView.typeface = robotoLightItalic
    }

    fun setupUI(){
        setTextViewRobotoLight(R.id.header_impressum)
        setTextViewRobotoLight(R.id.impressum1)
        setTextViewRobotoLight(R.id.impressum2)
        setTextViewRobotoLight(R.id.impressum3)
        setTextViewRobotoLight(R.id.impressum4)
        setTextViewRobotoLight(R.id.impressum5)
        setTextViewRobotoLight(R.id.impressum6)
        setTextViewRobotoLight(R.id.impressum7)
        setTextViewRobotoLight(R.id.impressum8)
        setTextViewRobotoLight(R.id.impressum9)
        setTextViewRobotoLight(R.id.impressum10)
        setTextViewRobotoLight(R.id.impressum11)
        setTextViewRobotoLight(R.id.impressum12)
        setTextViewRobotoLight(R.id.impressum13)
        setTextViewRobotoLight(R.id.impressum14)
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

    fun switchCompany(view: View) {

        myConfig = helper.loadConfig()
        //change value locally and in file
        if (myConfig.currentCompany == "flughafen"){
            myConfig.currentCompany = "fraunhofer"
        }
        else if (myConfig.currentCompany == "fraunhofer"){
            myConfig.currentCompany = "flughafen"
        }
        val gson = Gson()
        var jsonString = gson.toJson(myConfig)


        helper.writeConfig(applicationContext,jsonString)

        // re-create UI
        // setupUI()
    }
}