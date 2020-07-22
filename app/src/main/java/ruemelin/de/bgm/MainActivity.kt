package ruemelin.de.bgm


import android.content.Intent
import android.content.res.Resources
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
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    val config = "config.json"
    private lateinit var myConfig: MyConfig
    var currentCompanyTest:Company?=null
    private lateinit var mBtmView: BottomNavigationView


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

        loadConfig()
        setupUI()

        //make use of bottom navigation bar
        mBtmView = findViewById(R.id.nav_view)
        mBtmView.setOnNavigationItemSelectedListener(this)
    }

    fun loadConfig(){
        // try to load config.json from local file dir
        val file = File(this.filesDir.absolutePath, config)
        var fis: FileInputStream
        try{
            Log.i("Kotlin", "try to open config file from " + file.name)
            fis = FileInputStream(file)
            Log.i("Kotlin", "try: file seems to exist, no exception")

        } catch (e: IOException){
            Log.i("Kotlin", "catch: config.json does not exist locally, will be created from assets: " +e.toString())

            // read config data from asset file
            var inputString: String?=""
            try {
                inputString = applicationContext.assets.open(config).bufferedReader().use { it.readText() }
                //Log.i("Kotlin", "Read from asset folder: " +inputString)
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                Log.i("Kotlin", "MainActivity: Something went wrong when reading config.json from asset folder")
            }
            // and write to the newly create file
            FileOutputStream(file).use { it.write(inputString?.toByteArray())}
            fis = FileInputStream(file)

            Log.i("Kotlin", "config file should have been created: " + file.name)
        }

        // now read from fileinputstream and fill myConfig
        try {
            val inputAsString = fis.bufferedReader().use { it.readText() }
            Log.i("Kotlin", "read from stored file config.json: " +inputAsString)
            val gson = Gson()
            myConfig = gson.fromJson(inputAsString, MyConfig::class.java)
        } catch (e:FileNotFoundException){
            Log.i("Kotlin", "catch FileNotFoundException: " + e.toString())
        }
    }

    fun setupUI(){
        val companyLogo:ImageView = findViewById(R.id.logoCompany)
        val aokLogo:ImageView = findViewById(R.id.logoAOK)
        //val layoutKooperation:LinearLayout = findViewById(R.id.layoutKooperation)
        val rechargeLogo:ImageView = findViewById(R.id.logoRecharge)
        val rechargeText:ImageView = findViewById(R.id.textRecharge)

        // Start working with config data
        myConfig.companies?.forEach { c -> if(c.getCompanyId()==myConfig.currentCompany)
        { this.currentCompanyTest=c
            Log.i("Kotlin","Found match between "+ c.getCompanyId() + " and " +myConfig.currentCompany + "- looking for logo named logo_" + myConfig.currentCompany+".png now")
        }
        else { //Log.i("Kotlin", c.getCompanyId() + " does not match " +myConfig.currentCompany)
        }}
        //Log.i("Kotlin", "Todo: set logo to the one of " + (currentCompanyTest?.printOut() ?: currentCompanyTest))

        // get resource id based on config file and set the logo accordingly, leads e.g. to R.drawable.logo_flughafen
        val resCompanyLogo:Int = this.getResources().getIdentifier("logo_" + myConfig.currentCompany, "drawable", this.getPackageName())
        val drawableCompanyLogo: Drawable? = ResourcesCompat.getDrawable(resources, resCompanyLogo, null)
        companyLogo.setImageDrawable(drawableCompanyLogo)

        val resAOKLogo:Int = this.getResources().getIdentifier("logo_aok_by", "drawable", this.getPackageName())
        val drawableAOKLogo: Drawable? = ResourcesCompat.getDrawable(resources, resAOKLogo, null)
        aokLogo.setImageDrawable(drawableAOKLogo)


        val screen_width = Resources.getSystem().getDisplayMetrics().widthPixels
        val screen_height = Resources.getSystem().getDisplayMetrics().heightPixels

        Log.i("Kotlin", "Working with screen size " + screen_width +" x " +screen_height)


        if (currentCompanyTest?.AOK == true){
            Log.i("Kotlin", "AOK logo included: " + (currentCompanyTest?.AOK ?: currentCompanyTest))

            // show AOK logo
            logoAOK.isVisible=true;

        } else{
            // hide AOK logo
            logoAOK?.isVisible=false
            Log.i("Kotlin", "no AOK logo required, setup UI accordingly - maybe change margin or padding of company logo to be aligned on right side?")
            /*val marginParams_CompanyLogo = params_CompanyLogo as MarginLayoutParams
            marginParams_CompanyLogo.setMargins(screen_width/10,0,screen_width/10,0);
            layoutKooperation?.setLayoutParams(marginParams_CompanyLogo);*/

            // programmLogo.setPadding(0,0,0,screen_height/9)
        }

        // resize company logo to 1/6 of screen width
        val params_CompanyLogo = companyLogo.getLayoutParams()
        params_CompanyLogo.height = screen_height/8
        if (drawableCompanyLogo != null) {
            params_CompanyLogo.width = drawableCompanyLogo.intrinsicWidth*screen_height/8/drawableCompanyLogo.intrinsicHeight
        }
        companyLogo.setLayoutParams(params_CompanyLogo)

        // resize AOK logo
        val params_AOKLogo = aokLogo.getLayoutParams()
        params_AOKLogo.height = screen_height/8
        if (drawableAOKLogo != null) {
            params_AOKLogo.width = drawableAOKLogo.intrinsicWidth*screen_height/8/drawableAOKLogo.intrinsicHeight
        }
        aokLogo.setLayoutParams(params_AOKLogo)

        // resize bloomergym logo
        val params_rechargeLogo = rechargeLogo.getLayoutParams()
        params_rechargeLogo.height = screen_height/2
        rechargeLogo.setLayoutParams(params_rechargeLogo)

        // resize recharge text

        val params_rechargeText = rechargeText.getLayoutParams()
        params_rechargeText.height = screen_height/8
        rechargeText.setLayoutParams(params_rechargeText)
    }

    fun switchToOverview(view: View) {
        Log.i("Kotlin", "called switchToOverview")
        //val editText = findViewById<EditText>(R.id.editText)
        //val message = editText.text.toString()
        val message = config
        val i = Intent(this, OverviewActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(i)
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        Log.i(
                "Kotlin",
                "called MainActivity > onNaviagtionItemSelected. Selected item was " + p0.itemId
        )

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