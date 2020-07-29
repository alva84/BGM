package de.bloomergym.bgm

import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var myConfig: MyConfig
    var currentCompanyTest:Company?=null
    private lateinit var mBtmView: BottomNavigationView
    private lateinit var helper:Helper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Remove title bar
        if (supportActionBar != null)
            supportActionBar?.hide()

        setContentView(R.layout.activity_main)

        // set up navigation bar
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_team, R.id.navigation_impressum))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        helper = Helper(applicationContext)
        myConfig = helper.loadConfig()
        setupUI()

        //make use of bottom navigation bar
        mBtmView = findViewById(R.id.nav_view)
        mBtmView.setOnNavigationItemSelectedListener(this)
    }


    fun setupUI(){
        val companyLogo:ImageView = findViewById(R.id.logoCompany)
        val aokLogo:ImageView = findViewById(R.id.logoAOK)
        //val layoutKooperation:LinearLayout = findViewById(R.id.layoutKooperation)
        val rechargeLogo:ImageView = findViewById(R.id.logoRecharge)
        val rechargeText:ImageView = findViewById(R.id.textRecharge)

        // Start working with config data
        myConfig.companies?.forEach { c -> if(c.getCompanyId()==myConfig.currentCompany)
        { this.currentCompanyTest=c }
        else {} //Log.i("Kotlin", c.getCompanyId() + " does not match " +myConfig.currentCompany)
        }

        // get resource id based on config file and set the logo accordingly, leads e.g. to R.drawable.logo_flughafen
        /*val resCompanyLogo:Int = this.getResources().getIdentifier("logo_" + myConfig.currentCompany, "drawable", this.getPackageName())
        val drawableCompanyLogo: Drawable? = ResourcesCompat.getDrawable(resources, resCompanyLogo, null)
        companyLogo.setImageDrawable(drawableCompanyLogo)
        */

        //TODO: handle that image is not there
        val companyLogoBitmap: Bitmap? = helper.getLogoBitmap(myConfig.currentCompany)
        companyLogo.setImageBitmap(companyLogoBitmap)

        var aokLogoBitmap: Bitmap? = null
        var drawableAOKLogo : Drawable? = null
        if (helper.getLogoBitmap("aok_by") != null){
             aokLogoBitmap = helper.getLogoBitmap("aok_by")
            aokLogo.setImageBitmap(aokLogoBitmap)
        } else {
            val resAOKLogo:Int = this.resources.getIdentifier("logo_aok_by", "drawable", this.packageName)
            drawableAOKLogo = ResourcesCompat.getDrawable(resources, resAOKLogo, null)
            aokLogo.setImageDrawable(drawableAOKLogo)
        }

        val screen_width = Resources.getSystem().displayMetrics.widthPixels
        val screen_height = Resources.getSystem().displayMetrics.heightPixels

        if (currentCompanyTest?.AOK == true){
            // show AOK logo
            logoAOK.isVisible=true
        } else{
            // hide AOK logo
            logoAOK?.isVisible=false
        }

        // resize company logo to 1/8 of screen height
        val params_CompanyLogo = companyLogo.layoutParams
        params_CompanyLogo.height = screen_height/8
        /*if (drawableCompanyLogo != null) {
            params_CompanyLogo.width = drawableCompanyLogo.intrinsicWidth*screen_height/8/drawableCompanyLogo.intrinsicHeight
        }*/
        if (companyLogoBitmap != null) {
            params_CompanyLogo.width = companyLogoBitmap.width*screen_height/8/companyLogoBitmap.height
        }
        companyLogo.layoutParams = params_CompanyLogo

        // resize AOK logo
        val params_AOKLogo = aokLogo.layoutParams
        params_AOKLogo.height = screen_height/8
        if (aokLogoBitmap != null) {
            params_AOKLogo.width = aokLogoBitmap.width*screen_height/8/aokLogoBitmap.height
        } else {
            if (drawableAOKLogo != null){
                params_AOKLogo.width = drawableAOKLogo.intrinsicWidth*screen_height/8/drawableAOKLogo.intrinsicHeight
            }
        }
        aokLogo.layoutParams = params_AOKLogo

        // resize recharge logo
        val params_rechargeLogo = rechargeLogo.layoutParams
        params_rechargeLogo.height = screen_height/7*3
        rechargeLogo.layoutParams = params_rechargeLogo

        // resize recharge text
        val params_rechargeText = rechargeText.layoutParams
        params_rechargeText.height = screen_height/8
        rechargeText.layoutParams = params_rechargeText
    }

    fun switchToOverview(view: View) {
        Log.i("Kotlin", "called switchToOverview")
        val message = "dummy"
        val i = Intent(this, OverviewActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(i)
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        var i: Intent? = null

        if (p0.itemId == R.id.navigation_home) {
            //i = Intent(this, MainActivity::class.java);
        } else if (p0.itemId == R.id.navigation_team) {
            i = Intent(this, TeamActivity::class.java)
        } else if (p0.itemId == R.id.navigation_impressum) {
            i = Intent(this, ImpressumActivity::class.java)
        }

        if (i != null) {
            startActivity(i)
            return true
        } else {
            return false
        }
    } // end of onNavigationItemSelected

}