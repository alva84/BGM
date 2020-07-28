package de.bloomergym.bgm

import android.content.Intent
import android.content.res.Resources
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class TeamActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener  {

    private lateinit var mBtmView: BottomNavigationView
    var list1: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team)

        //make use of bottom navigation bar
        mBtmView = findViewById(R.id.nav_view)
        mBtmView.selectedItemId = R.id.navigation_team
        mBtmView.setOnNavigationItemSelectedListener(this)

        setupUI()
    }

    fun setupUI(){
        val title:TextView = findViewById(R.id.textView_Team)
        val sub:TextView = findViewById(R.id.textView_TeamSub)

        val l1:LinearLayout = findViewById(R.id.linearLayout1)
        val l2:LinearLayout = findViewById(R.id.linearLayout2)
        val l3:LinearLayout = findViewById(R.id.linearLayout3)
        val l4:LinearLayout = findViewById(R.id.linearLayout4)

        val name1:TextView = findViewById(R.id.textView1_1)
        val function1:TextView = findViewById(R.id.textView1_2)
        val image1:ImageView = findViewById(R.id.imageView1)
        val listView1: ListView = findViewById<View>(R.id.listView1) as ListView

        val name2:TextView = findViewById(R.id.textView2_1)
        val function2:TextView = findViewById(R.id.textView2_2)
        val image2:ImageView = findViewById(R.id.imageView2)
        val listView2: ListView = findViewById<View>(R.id.listView2) as ListView

        val name3:TextView = findViewById(R.id.textView3_1)
        val function3:TextView = findViewById(R.id.textView3_2)
        val image3:ImageView = findViewById(R.id.imageView3)
        val listView3: ListView = findViewById<View>(R.id.listView3) as ListView

        val name4:TextView = findViewById(R.id.textView4_1)
        val function4:TextView = findViewById(R.id.textView4_2)
        val image4:ImageView = findViewById(R.id.imageView4)
        val listView4: ListView = findViewById<View>(R.id.listView4) as ListView

        var screen_width = Resources.getSystem().displayMetrics.widthPixels

        val robotoLight = Typeface.createFromAsset(assets, "Roboto-Light.ttf")
        val robotoLightItalic = Typeface.createFromAsset(assets, "Roboto-LightItalic.ttf")

        // Größen insgesamt
        val params = l1.layoutParams
        params.width = screen_width  /4
        l1.layoutParams = params
        l2.layoutParams = params
        l3.layoutParams = params
        l4.layoutParams = params
        l1.setPadding((screen_width/resources.getDimension(R.dimen.gap_medium)).toInt(),0,
            (screen_width/resources.getDimension(R.dimen.gap_medium)).toInt(),0)
        l2.setPadding((screen_width/resources.getDimension(R.dimen.gap_medium)).toInt(),0,
            (screen_width/resources.getDimension(R.dimen.gap_medium)).toInt(),0)
        l3.setPadding((screen_width/resources.getDimension(R.dimen.gap_medium)).toInt(),0,
            (screen_width/resources.getDimension(R.dimen.gap_medium)).toInt(),0)
        l4.setPadding((screen_width/resources.getDimension(R.dimen.gap_medium)).toInt(),0,
            (screen_width/resources.getDimension(R.dimen.gap_medium)).toInt(),0)

        // Bilder
        val imageParams = image1.layoutParams
        imageParams.width = screen_width/5
        imageParams.height = screen_width/5
        image1.layoutParams = imageParams
        image2.layoutParams = imageParams
        image3.layoutParams = imageParams
        image4.layoutParams = imageParams

        image1.setPadding(0, (screen_width/resources.getDimension(R.dimen.gap_small)).toInt(),
            0, (screen_width/resources.getDimension(R.dimen.gap_small)).toInt())
        image2.setPadding(0, (screen_width/resources.getDimension(R.dimen.gap_small)).toInt(),
            0, (screen_width/resources.getDimension(R.dimen.gap_small)).toInt())
        image3.setPadding(0, (screen_width/resources.getDimension(R.dimen.gap_small)).toInt(),
            0, (screen_width/resources.getDimension(R.dimen.gap_small)).toInt())
        image4.setPadding(0, (screen_width/resources.getDimension(R.dimen.gap_small)).toInt(),
            0, (screen_width/resources.getDimension(R.dimen.gap_small)).toInt())

        // Fonts
        title.typeface = robotoLight
        sub.typeface = robotoLightItalic

        name1.typeface = robotoLight
        function1.typeface = robotoLightItalic

        name2.typeface = robotoLight
        function2.typeface = robotoLightItalic

        name3.typeface = robotoLight
        function3.typeface = robotoLightItalic

        name4.typeface = robotoLight
        function4.typeface = robotoLightItalic

        // Beschreibungen
        val list1 = resources.getStringArray(R.array.team_kathrin)
        val list2 = resources.getStringArray(R.array.team_tamara)
        val list3 = resources.getStringArray(R.array.team_jil)
        val list4 = resources.getStringArray(R.array.team_alisa)

        var adapter = TeamListAdapter(this, R.layout.list_item_team2, list1, resources.getDimension(R.dimen.font_size_small2).toFloat(),
            screen_width/4,screen_width/resources.getDimension(R.dimen.gap_small).toInt())
        listView1.adapter = adapter

        adapter = TeamListAdapter(this, R.layout.list_item_team2, list2, resources.getDimension(R.dimen.font_size_small2).toFloat(),
            screen_width/4,screen_width/resources.getDimension(R.dimen.gap_small).toInt())
        listView2.adapter = adapter

        adapter = TeamListAdapter(this, R.layout.list_item_team2, list3, resources.getDimension(R.dimen.font_size_small2).toFloat(),
            screen_width/4,screen_width/resources.getDimension(R.dimen.gap_small).toInt())
        listView3.adapter = adapter

        adapter = TeamListAdapter(this, R.layout.list_item_team2, list4, resources.getDimension(R.dimen.font_size_small2).toFloat(),
            screen_width/4,screen_width/resources.getDimension(R.dimen.gap_small).toInt())
        listView4.adapter = adapter

    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        Log.i("Kotlin", "called KontaktActivity > onNaviagtionItemSelected. Selected item was "+ p0.itemId)

        var i : Intent? = null

        if (p0.itemId == R.id.navigation_home){
            i = Intent(this, MainActivity::class.java)
        }
        else if (p0.itemId == R.id.navigation_team){
            //i = Intent(this, KontaktActivity::class.java);
        }
        else if (p0.itemId == R.id.navigation_impressum){
            i = Intent(this, ImpressumActivity::class.java)
        }

        if (i != null) {
            startActivity(i)
            return true
        }
        else {
            return false
        }
    }
}