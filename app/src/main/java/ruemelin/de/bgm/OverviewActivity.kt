package ruemelin.de.bgm

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import java.io.IOException


class OverviewActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener  {

    private lateinit var mBtmView: BottomNavigationView
    private var buttonId = 0

    private val config = "config.json"
    private var myConfig: MyConfig? = null
    private var currentCompany: Company? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)


        // Get the Intent that started this activity and extract the string
        val config_file = intent.getStringExtra(EXTRA_MESSAGE)

        // Capture the layout's TextView and set the string as its text
        /*val textView = findViewById<TextView>(R.id.textView).apply {
            text = config_file
        }*/

        loadConfig(config_file);
        setupUI();


        //make use of bottom navigation bar
        mBtmView = findViewById(R.id.nav_view);
        mBtmView.setOnNavigationItemSelectedListener(this);
        //mBtmView.getMenu().findItem(R.id.action_yoga).setChecked(true);
    }

    fun loadConfig(filename: String) {

        var inputString: String? = ""
        try {
            inputString = applicationContext.assets.open(filename).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

        Log.i("Kotlin", "read from " + filename + ": " + inputString)

        val gson = Gson()
        //val listPersonType = object : TypeToken<List<Person>>() {}.type
        myConfig = gson.fromJson(inputString, MyConfig::class.java)
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
            //myConfig?.companies?.forEach { c -> if(c.name==myConfig?.currentCompany) this.currentCompany=c}
            Log.i(
                "Kotlin",
                "Todo: set logo to the one of " + (currentCompany?.printOut() ?: currentCompany)
            )

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
                val adapter = CustomAdapter(this, R.layout.overview_item2, chosenPrograms, screen_height/3, screen_width/3, (screen_width/resources.getDimension(R.dimen.font_big)).toFloat(), screen_width/25)
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
        else if (p0.itemId == R.id.navigation_kontakt){
            i = Intent(this, KontaktActivity::class.java);
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
}