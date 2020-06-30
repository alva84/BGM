package ruemelin.de.bgm


import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
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
import java.io.IOException


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    val config = "config.json"
    private lateinit var myConfig: MyConfig
    var currentCompanyTest:Company?=null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        // load config.json
        loadConfig()

        // create UI
        setupUI()
    }

    fun loadConfig(){
        var inputString: String?=""
        try {
            inputString = applicationContext.assets.open(config).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) { ioException.printStackTrace() }
        //Log.i("Kotlin", "read from json: " +inputString)

        val gson = Gson()
        //val listPersonType = object : TypeToken<List<Person>>() {}.type
        myConfig = gson.fromJson(inputString, MyConfig::class.java)

        //var persons: List<Post> = gson.fromJson(inputString, listPersonType)
        //persons.forEachIndexed { idx, person -> Log.i("data", "> Item $idx:\n$person") }

        //Initialize the String Builder for debugging
        var stringBuilder = StringBuilder("\nMyConfig Details\n---------------------")
        +Log.i("Kotlin",myConfig.currentCompany)
        stringBuilder.append("\nCurrent company: " + myConfig.currentCompany)
        stringBuilder.append("\nAll companies: ")
        //get the all Tags using for Each loop
        myConfig.companies?.forEach { c -> stringBuilder.append(c.name +" (AOK = "+ c.AOK +")"+ ", ") }
        stringBuilder.append("\nAll programs: ")
        //get the all Tags using for Each loop
        myConfig.programs?.forEach { p -> stringBuilder.append(p.name+ ", ") }

        Log.i("Kotlin", stringBuilder.toString())


    }

    fun setupUI(){
        var companyLogo:ImageView = findViewById(R.id.logoCompany)
        var layoutKooperation:LinearLayout = findViewById(R.id.layoutKooperation);
        var textKooperation: TextView= findViewById(R.id.text_Kooperation)
        var bloomergymLogo:ImageView = findViewById(R.id.logoBloomergym)
        var aokLogo:ImageView = findViewById(R.id.logoAOK)
        var programmLogo:ImageView = findViewById(R.id.logoProgramm)

        // Start working with config data
        myConfig.companies?.forEach { c -> if(c.getCompanyId()==myConfig.currentCompany)
        { this.currentCompanyTest=c
            Log.i("Kotlin","Found match between "+ c.getCompanyId() + " and " +myConfig.currentCompany + "- looking for logo named logo_" + myConfig.currentCompany+".png now");
        }
        else { Log.i("Kotlin", c.getCompanyId() + " does not match " +myConfig.currentCompany);}}
        Log.i("Kotlin", "Todo: set logo to the one of " + (currentCompanyTest?.printOut() ?: currentCompanyTest));

        // get resource id based on config file and set the logo accordingly, leads e.g. to R.drawable.logo_flughafen
        val resLogo:Int = this.getResources().getIdentifier("logo_" + myConfig.currentCompany, "drawable", this.getPackageName());
        val drawable: Drawable? = ResourcesCompat.getDrawable(resources, resLogo, null)
        companyLogo?.setImageDrawable(drawable);


        var screen_width = Resources.getSystem().getDisplayMetrics().widthPixels
        var screen_height = Resources.getSystem().getDisplayMetrics().heightPixels


        Log.i("Kotlin", "Todo: înclude AOK logo or not: " + (currentCompanyTest?.AOK ?: currentCompanyTest));
        if (currentCompanyTest?.AOK == true){

            // resize company logo to 1/6 of screen width
            val params_CompanyLogo = companyLogo?.getLayoutParams()
            params_CompanyLogo?.width = screen_width/7
            companyLogo?.setLayoutParams(params_CompanyLogo)

            /*val marginParams_CompanyLogo = params_CompanyLogo as MarginLayoutParams
            marginParams_CompanyLogo.setMargins(screen_width/10,0,screen_width/10,0);
            layoutKooperation?.setLayoutParams(marginParams_CompanyLogo);*/

            // resize middle part
            var params_Layout = layoutKooperation?.getLayoutParams()
            params_Layout?.width = screen_width*4/9 // 15/4 = 3,75
            layoutKooperation?.setLayoutParams(params_Layout);

            var params_BloomergymLogo = bloomergymLogo?.getLayoutParams()
            params_BloomergymLogo?.width = screen_width/4 // 7/2 = 3,5
            //params_BloomergymLogo?.height = screen_width/(20)
            bloomergymLogo?.setLayoutParams(params_BloomergymLogo);

            textKooperation?.setTextSize((screen_width/resources.getDimension(R.dimen.font_medium)).toFloat())

            // keep AOK logo at default, just resize
            val params_AOKLogo = aokLogo?.getLayoutParams()
            params_AOKLogo?.width = screen_width/5
            aokLogo?.setLayoutParams(params_AOKLogo)

            val params_ProgrammLogo = programmLogo?.getLayoutParams()
            params_ProgrammLogo?.height = screen_height/2
            programmLogo?.setLayoutParams(params_ProgrammLogo)
            programmLogo.setPadding(0,0,0,200)


        } else{
            logoAOK?.isVisible=false;
        }

    }

    fun switchToOverview(view: View) {
        Log.i("Kotlin", "called switchToOverview");
        //val editText = findViewById<EditText>(R.id.editText)
        //val message = editText.text.toString()
        val message = config;
        val i = Intent(this, OverviewActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(i)
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        Log.i(
                "Kotlin",
                "called MainActivity > onNaviagtionItemSelected. Selected item was " + p0.itemId
        );

        var i: Intent? = null;

        if (p0.itemId == R.id.navigation_home) {
            //i = Intent(this, MainActivity::class.java);
        } else if (p0.itemId == R.id.navigation_kontakt) {
            i = Intent(this, KontaktActivity::class.java);
        } else if (p0.itemId == R.id.navigation_impressum) {
            i = Intent(this, ImpressumActivity::class.java);
        }

        if (i != null) {
            startActivity(i);
            return true;
        } else {
            return false;
        }
    } // end of onNavigationItemSelected
}

