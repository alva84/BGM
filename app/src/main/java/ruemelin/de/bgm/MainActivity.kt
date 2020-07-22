package ruemelin.de.bgm


import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.system.ErrnoException
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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
                R.id.navigation_home, R.id.navigation_kontakt, R.id.navigation_impressum))
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
        val layoutKooperation:LinearLayout = findViewById(R.id.layoutKooperation)
        val textKooperation: TextView= findViewById(R.id.text_Kooperation)
        val bloomergymLogo:ImageView = findViewById(R.id.logoBloomergym)
        val aokLogo:ImageView = findViewById(R.id.logoAOK)
        val programmLogo:ImageView = findViewById(R.id.logoProgramm)

        // Start working with config data
        myConfig.companies?.forEach { c -> if(c.getCompanyId()==myConfig.currentCompany)
        { this.currentCompanyTest=c
            Log.i("Kotlin","Found match between "+ c.getCompanyId() + " and " +myConfig.currentCompany + "- looking for logo named logo_" + myConfig.currentCompany+".png now")
        }
        else { //Log.i("Kotlin", c.getCompanyId() + " does not match " +myConfig.currentCompany)
        }}
        //Log.i("Kotlin", "Todo: set logo to the one of " + (currentCompanyTest?.printOut() ?: currentCompanyTest))

        // get resource id based on config file and set the logo accordingly, leads e.g. to R.drawable.logo_flughafen
        val resLogo:Int = this.getResources().getIdentifier("logo_" + myConfig.currentCompany, "drawable", this.getPackageName())
        val drawable: Drawable? = ResourcesCompat.getDrawable(resources, resLogo, null)
        companyLogo.setImageDrawable(drawable)


        val screen_width = Resources.getSystem().getDisplayMetrics().widthPixels
        val screen_height = Resources.getSystem().getDisplayMetrics().heightPixels

        Log.i("Kotlin", "Working with screen size " + screen_width +" x " +screen_height)


        if (currentCompanyTest?.AOK == true){
            Log.i("Kotlin", "AOK logo included: " + (currentCompanyTest?.AOK ?: currentCompanyTest))

            // resize company logo to 1/6 of screen width
            val params_CompanyLogo = companyLogo.getLayoutParams()
            params_CompanyLogo.width = screen_width/7
            params_CompanyLogo.height = screen_height/4
            companyLogo.setLayoutParams(params_CompanyLogo)

            Log.i("Kotlin", "AOK logo width (should be /7 of screen width): " + params_CompanyLogo.width)


            /*val marginParams_CompanyLogo = params_CompanyLogo as MarginLayoutParams
            marginParams_CompanyLogo.setMargins(screen_width/10,0,screen_width/10,0);
            layoutKooperation?.setLayoutParams(marginParams_CompanyLogo);*/

            // resize middle part
            val params_Layout = layoutKooperation.getLayoutParams()
            params_Layout.width = screen_width*6/10 // 15/4 = 3,75, *4/9 = 0,44, *4/8 = 0,5, *6/10 = 0,6
            layoutKooperation.setLayoutParams(params_Layout)

            val params_BloomergymLogo = bloomergymLogo.getLayoutParams()
            params_BloomergymLogo.width = screen_width/3 // 7/2 = 3,5
            //params_BloomergymLogo?.height = screen_width/(20)
            bloomergymLogo.setLayoutParams(params_BloomergymLogo)

            textKooperation.setTextSize((screen_width/resources.getDimension(R.dimen.font_big)).toFloat())

            // show AOK logo, and resize
            logoAOK.isVisible=true;
            val params_AOKLogo = aokLogo.getLayoutParams()
            params_AOKLogo.width = screen_width*2/9
            aokLogo.setLayoutParams(params_AOKLogo)



        } else{

            // resize company logo to 1/6 of screen width
            val params_CompanyLogo = companyLogo.getLayoutParams()
            params_CompanyLogo.width = screen_width/5
            //params_CompanyLogo.height = screen_height/4
            companyLogo.setLayoutParams(params_CompanyLogo)


            // resize middle part
            val params_Layout = layoutKooperation.getLayoutParams()
            params_Layout.width = screen_width*4/9 // 15/4 = 3,75, *4/9 = 0,44, *4/8 = 0,5,
            layoutKooperation.setLayoutParams(params_Layout)

            val params_BloomergymLogo = bloomergymLogo.getLayoutParams()
            params_BloomergymLogo.width = screen_width/3 // 7/2 = 3,5
            //params_BloomergymLogo?.height = screen_width/(20)
            bloomergymLogo.setLayoutParams(params_BloomergymLogo)

            textKooperation.setTextSize((screen_width/resources.getDimension(R.dimen.font_big)).toFloat())


            // hide AOK logo
            logoAOK?.isVisible=false
            Log.i("Kotlin", "no AOK logo required, setup UI accordingly")

        }

        val params_ProgrammLogo = programmLogo.getLayoutParams()
        params_ProgrammLogo.height = screen_height*2/5
        programmLogo.setLayoutParams(params_ProgrammLogo)

        programmLogo.setPadding(0,0,0,screen_height/9)


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
        } else if (p0.itemId == R.id.navigation_kontakt) {
            i = Intent(this, KontaktActivity::class.java)
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