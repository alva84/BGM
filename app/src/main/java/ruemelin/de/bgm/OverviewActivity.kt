package ruemelin.de.bgm

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import java.io.*


class OverviewActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener  {

    private lateinit var mBtmView: BottomNavigationView

    private val config = "config.json"
    private var myConfig: MyConfig? = null
    private var currentCompany: Company? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        // Get the Intent that started this activity and extract the string
        var config_file = intent.getStringExtra(EXTRA_MESSAGE)
        if (config_file == null) config_file = config

        //Todo: load from local file - also in programactivity
        loadConfig()
        setupUI()

        //make use of bottom navigation bar
        mBtmView = findViewById(R.id.nav_view)
        mBtmView.setOnNavigationItemSelectedListener(this)

        mBtmView.menu.setGroupCheckable(0, true, false)
        for (i in 0 until mBtmView.menu.size()) {
            mBtmView.menu.getItem(i).isChecked = false
        }
        mBtmView.menu.setGroupCheckable(0, true, true)
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

    fun setupUI() {
        var screen_width = Resources.getSystem().getDisplayMetrics().widthPixels
        var screen_height = Resources.getSystem().getDisplayMetrics().heightPixels

        var gridView: GridView = findViewById(R.id.gridView);
        // TODO ggf. noch numColumns anpassen, default ist 3

        // Start working with config data
        myConfig?.companies?.forEach { c ->
            if (c.getCompanyId() == myConfig?.currentCompany) {
                this.currentCompany = c
                Log.i(
                    "Kotlin", "Found match between " + c.getCompanyId() +
                            " and " + myConfig?.currentCompany +
                            "- looking for logo named logo_" + myConfig!!.currentCompany + ".png now"
                );
            }

        }
        if (this.currentCompany != null){
            Log.i(
                "Kotlin",
                "Number of chosen programs to integrate: " + currentCompany?.chosen_programs?.size
            )

            val size: Int? = currentCompany?.chosen_programs?.size;

            if (size != null) {
                //val chosenPrograms = arrayOf("a", "b", "c")

                // get a string array of program names
                val chosenPrograms = arrayOfNulls<String>(size)

                for (i in 0..size - 1) {
                    val p = currentCompany?.chosen_programs?.get(i)
                    chosenPrograms[i] = p;
                }

//            currentCompany?.chosen_programs?.forEach { p -> createButton(p)}//Log.i("Kotlin", "Todo: create button" )
                //val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, chosenPrograms)

                Log.i("Kotlin", "overviewactivity: try to create custom adapter with string-array chosenprograms (holds " + chosenPrograms.size + " items)");

                //val adapter = ArrayAdapter<String>(this, R.layout.overview_item, chosenPrograms)
                val adapter = CustomAdapter(this, R.layout.overview_item2, chosenPrograms, screen_height/3, screen_width/3, (screen_width/resources.getDimension(R.dimen.font_big)).toFloat(), screen_width/28)
                Log.i("Kotlin", "overviewactivity: try to map custom adapter to gridview");

                gridView.adapter = adapter

                Log.i("Kotlin", "overviewactivity: try to add onitemclicklistener to gridview");

                // add listeners to all items
                gridView.onItemClickListener =
                    AdapterView.OnItemClickListener { parent, view, position, id ->
                        // Do something in response to the click
                        Log.i("Kotlin", "overviewactivity: is this called?");
                    }

                // configure items
                /*
                for (i in 0..size - 1) {
                    val item: TextView = gridView.getChildAt(i) as TextView
                    item.setTextSize(
                        TypedValue.COMPLEX_UNIT_SP,
                        (screen_width / resources.getDimension(R.dimen.font_medium)).toFloat()
                    )

                    val params_Item = item.getLayoutParams()
                    params_Item.height = screen_width / 10
                    item.setLayoutParams(params_Item)
                    //item.setPadding((screen_width/resources.getDimension(R.dimen.gap_medium)).toInt(),(screen_width/resources.getDimension(R.dimen.gap_medium)).toInt(),0,(screen_width/resources.getDimension(R.dimen.gap_big)).toInt())

                }//for
                */


            }//if
            Log.i("Kotlin", "check: " + gridView.childCount)
            Log.i("Kotlin", "check: " + gridView.adapter.getItem(0))

            // TODO: find list where all buttons are in with height param

            // check the max height of buttons
            /*
            var maxHeight = 0;
            for (item in gridView?.children!!) {
                // body of loop
                val params = item?.getLayoutParams()
                Log.i("Kotlin", item.toString() + ": width = " + params?.width!!)
                if (params?.width!! > maxHeight){
                    maxHeight = params?.width!!;
                }
                Log.i("Kotlin", "maxHeight = " + maxHeight)
            }
            // then set all buttons to the same max height
            for (item in gridView?.children!!) {
                // body of loop
                val params = item?.getLayoutParams()
                params?.width = maxHeight
                item?.setLayoutParams(params);
            }*/

        }//if
        }//fun

    fun switchToProgram(view: View){
        Log.i("Kotlin", "called switchToProgram");

        var btn: Button? = view as Button?

        val message = btn?.text;

        Log.i("Kotlin", "called switchToProgram for button labelled " + message);

        val i = Intent(this, ProgramActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(i)
    }

        override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        Log.i("Kotlin", "called OverviewActivity > onNaviagtionItemSelected. Selected item was "+ p0.itemId);

        var i : Intent? = null;

        if (p0.itemId == R.id.navigation_home){
            i = Intent(this, MainActivity::class.java);
        }
        else if (p0.itemId == R.id.navigation_team){
            i = Intent(this, TeamActivity::class.java);
        }
        else if (p0.itemId == R.id.navigation_impressum){
            i = Intent(this, ImpressumActivity::class.java);
        }

        if (i != null) {
            startActivity(i);
            return true;
        }
        else {
            return false;
        }
    }
}