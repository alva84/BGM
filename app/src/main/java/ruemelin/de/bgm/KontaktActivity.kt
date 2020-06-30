package ruemelin.de.bgm

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class KontaktActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener  {

    private lateinit var mBtmView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kontakt)

        //make use of bottom navigation bar
        mBtmView = findViewById(R.id.nav_view);
        mBtmView.selectedItemId = R.id.navigation_kontakt;
        mBtmView.setOnNavigationItemSelectedListener(this);
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        Log.i("Kotlin", "called KontaktActivity > onNaviagtionItemSelected. Selected item was "+ p0.itemId);

        var i : Intent? = null;

        if (p0.itemId == R.id.navigation_home){
            i = Intent(this, MainActivity::class.java);
        }
        else if (p0.itemId == R.id.navigation_kontakt){
            //i = Intent(this, KontaktActivity::class.java);
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