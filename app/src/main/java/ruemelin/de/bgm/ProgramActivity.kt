package ruemelin.de.bgm

import android.app.Activity
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.util.TypedValue
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.google.gson.Gson
import java.io.IOException

class ProgramActivity  : AppCompatActivity() {

    private val config = "config.json"
    private var myConfig: MyConfig? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_program)

        val programName = intent.getStringExtra(EXTRA_MESSAGE)

        loadConfig(config);

        setupUI(programName);
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

    fun setupUI(programName: String) {
        var screen_width = Resources.getSystem().getDisplayMetrics().widthPixels
        var screen_height = Resources.getSystem().getDisplayMetrics().heightPixels

        val video:VideoView = findViewById(R.id.videoView)
        var textTitle: TextView = findViewById(R.id.textTitle)
        var textDesc: TextView = findViewById(R.id.textDesc);
        var textFact: TextView = findViewById(R.id.textFacts)

        var program: Program? = null;
        myConfig?.programs?.forEach { p ->
            if (p.getProgramName() == programName) {
                program = p
                Log.i("Kotlin","Found match between " + p.getProgramName() + " and " + programName);
            }//if
        }//forEach

        if (program==null){
            Log.i("Kotlin", "ProgramActivity: found no match for " + programName);

        }

        // set texts
        textTitle.text = program?.getProgramName()
        textDesc.text = program?.getProgramDesc()
        textFact.text = program?.getProgramFact()

        // set video
        // get resource id for video, based on config file, and set video file accordingly
        // -> leads e.g. to R.raw.logo_flughafen
        val resVideo:Int = this.getResources().getIdentifier(program?.media, "raw", this.getPackageName());
        video.setVideoPath("android.resource://" + getPackageName() + "/" + resVideo);
        val mediaController = MediaController(this)
        //mediaController.isShowing
        video.setMediaController(mediaController);
        video.requestFocus();
        video.start();


        if(this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val params_Video = video.getLayoutParams()
            Log.i("Kotlin", "ProgramActivity: aktuell: " + params_Video.width + ", soll: " + screen_width  *2/3)

            params_Video.width = screen_width  *2/3
            video.setLayoutParams(params_Video)

            val params_TextTitle = textTitle.getLayoutParams()
            params_TextTitle.width = screen_width / 3
            textTitle.setLayoutParams(params_TextTitle)
            textTitle.setPadding((screen_width/resources.getDimension(R.dimen.gap_medium)).toInt(),(screen_width/resources.getDimension(R.dimen.gap_medium)).toInt(),0,(screen_width/resources.getDimension(R.dimen.gap_big)).toInt())


            val params_TextDesc = textDesc.getLayoutParams()
            params_TextDesc.width = screen_width / 3
            textDesc.setLayoutParams(params_TextDesc)
            textDesc.setPadding((screen_width/resources.getDimension(R.dimen.gap_medium)).toInt(),0,0,(screen_width/resources.getDimension(R.dimen.gap_medium)).toInt())

            val params_TextFacts = textFact.getLayoutParams()
            params_TextFacts.width = screen_width / 3
            textFact.setLayoutParams(params_TextFacts)
            textFact.setPadding((screen_width/resources.getDimension(R.dimen.gap_medium)).toInt(),0,0,0)

        }

        textTitle?.setTextSize(TypedValue.COMPLEX_UNIT_SP, (screen_width/resources.getDimension(R.dimen.font_big)).toFloat())
        Log.i("Kotlin", "Title font size: " + (screen_width/resources.getDimension(R.dimen.font_big)).toFloat() + ", font_big: " + R.dimen.font_big + ", screen_width/R.dimen.font_big: " +screen_width/R.dimen.font_big)
        textDesc?.setTextSize(TypedValue.COMPLEX_UNIT_SP, (screen_width/resources.getDimension(R.dimen.font_small)).toFloat())
        textFact?.setTextSize(TypedValue.COMPLEX_UNIT_SP, (screen_width/resources.getDimension(R.dimen.font_small)).toFloat())

    }//fun


}
