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


class OverviewActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener  {

    private lateinit var mBtmView: BottomNavigationView
    private var myConfig: MyConfig? = null
    private var currentCompany: Company? = null
    private lateinit var helper:Helper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        // Get the Intent that started this activity and extract the string
        //var intentString = intent.getStringExtra(EXTRA_MESSAGE)

        helper = Helper(applicationContext)
        myConfig = helper.loadConfig()
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
                // get a string array of program names
                val chosenPrograms = arrayOfNulls<String>(size)

                for (i in 0..size - 1) {
                    val p = currentCompany?.chosen_programs?.get(i)
                    chosenPrograms[i] = p;
                }

                Log.i("Kotlin", "overviewactivity: create custom adapter with string-array chosenprograms (holds " + chosenPrograms.size + " items)");

                val adapter = OverviewListAdapter(this, R.layout.list_item_overview, chosenPrograms, screen_height/3, screen_width/3, (screen_width/resources.getDimension(R.dimen.font_overview)).toFloat(), screen_width/28)
                gridView.adapter = adapter

                // add listeners to all items
                gridView.onItemClickListener =
                    AdapterView.OnItemClickListener { parent, view, position, id ->
                        Log.i("Kotlin", "overviewactivity: is this called?");
                    }
            }//if
        }//if
    }//fun

    fun switchToProgram(view: View){

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