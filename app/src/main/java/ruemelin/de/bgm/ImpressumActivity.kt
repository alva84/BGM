package ruemelin.de.bgm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import java.io.*


class ImpressumActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener  {

    private lateinit var mBtmView: BottomNavigationView
    private lateinit var myConfig: MyConfig
    val config = "config.json"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_impressum)

        //make use of bottom navigation bar
        mBtmView = findViewById(R.id.nav_view);
        mBtmView.selectedItemId = R.id.navigation_impressum;
        mBtmView.setOnNavigationItemSelectedListener(this);
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        Log.i("Kotlin", "called ImpressumActivity > onNaviagtionItemSelected. Selected item was "+ p0.itemId);

        var i : Intent? = null;

        if (p0.itemId == R.id.navigation_home){
            i = Intent(this, MainActivity::class.java);
        }
        else if (p0.itemId == R.id.navigation_team){
            i = Intent(this, TeamActivity::class.java);
        }
        else if (p0.itemId == R.id.navigation_impressum){
            //i = Intent(this, ImpressumActivity::class.java);
        }

        if (i != null) {
            startActivity(i);
            return true;
        }
        else {
            return false;
        }
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
        } catch (e: FileNotFoundException){
            Log.i("Kotlin", "catch FileNotFoundException: " + e.toString())
        }
    }

    fun switchCompany(view: View) {

        loadConfig()

        //change value locally and in file
        if (myConfig.currentCompany == "flughafen"){
            myConfig.currentCompany = "fraunhofer"
        }
        else if (myConfig.currentCompany == "fraunhofer"){
            myConfig.currentCompany = "flughafen"
        }
        val gson = Gson()
        var jsonString = gson.toJson(myConfig)
        var newFile: File = File(this.filesDir.absolutePath, config)

        FileOutputStream(newFile).use { it.write(jsonString?.toByteArray())}

        // re-create UI
        // setupUI()
    }
}