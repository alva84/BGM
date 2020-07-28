package de.bloomergym.bgm

import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Typeface
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.util.TypedValue
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import java.io.FileNotFoundException

class ProgramActivity  : AppCompatActivity() {

    private lateinit var myConfig: MyConfig
    private lateinit var helper:Helper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_program)

        val programName = intent.getStringExtra(EXTRA_MESSAGE)

        helper = Helper(applicationContext)
        myConfig = helper.loadConfig()

        setupUI(programName)
    }

    fun setupUI(programName: String) {
        var screen_width = Resources.getSystem().displayMetrics.widthPixels
        var screen_height = Resources.getSystem().displayMetrics.heightPixels

        val video:VideoView = findViewById(R.id.videoView)
        var textTitle: TextView = findViewById(R.id.textTitle)
        var textDesc: TextView = findViewById(R.id.textDesc)
        var textFact: TextView = findViewById(R.id.textFacts)

        val robotoLight = Typeface.createFromAsset(assets, "Roboto-Light.ttf")
        val robotoLightItalic = Typeface.createFromAsset(assets, "Roboto-LightItalic.ttf")

        var program: Program? = null
        myConfig.programs?.forEach { p ->
            if (p.getProgramName() == programName) {
                program = p
                Log.i("Kotlin","Found match between " + p.getProgramName() + " and " + programName)
            }//if
        }//forEach

        if (program==null){ Log.i("Kotlin", "ProgramActivity: found no match for " + programName); }

        // set texts
        textTitle.text = program?.getProgramName()
        textDesc.text = program?.getProgramDesc()
        textFact.text = program?.getProgramFact()

        // set video
        try {
            video.setVideoPath(helper.getMediaPath(program?.media))
        } catch (e: FileNotFoundException)
        { // todo: delete else case, access via resources only for testing
                // get resource id for video, based on config file, and set video file accordingly
                // -> leads e.g. to R.raw.logo_flughafen
                val resVideo:Int = this.resources.getIdentifier(program?.media, "raw", this.packageName)
            video.setVideoPath("android.resource://" + packageName + "/" + resVideo)
        }
        val mediaController = MediaController(this)
        //mediaController.isShowing
        video.setMediaController(mediaController)
        video.requestFocus()
        video.start()

        //resize everything
        if(this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val params_Video = video.layoutParams
            Log.i("Kotlin", "ProgramActivity: Video aktuell: " + params_Video.width +
                    ", soll: " + screen_width  *2/3)

            params_Video.width = screen_width  *2/3
            video.layoutParams = params_Video
            video.setPadding(
                (screen_width/resources.getDimension(R.dimen.gap_small)).toInt(),
                (screen_width/resources.getDimension(R.dimen.gap_small)).toInt(),
                (screen_width/resources.getDimension(R.dimen.gap_small)).toInt(),
                0)

            val params_TextTitle = textTitle.layoutParams

            Log.i("Kotlin", "ProgramActivity: Title aktuell: " + params_TextTitle.width +
                    ", soll: " + screen_width  /3)

            params_TextTitle.width = screen_width / 3
            textTitle.layoutParams = params_TextTitle
            //textTitle.setPadding((screen_width/resources.getDimension(R.dimen.gap_medium)).toInt(),(screen_width/resources.getDimension(R.dimen.gap_medium)).toInt(),0,(screen_width/resources.getDimension(R.dimen.gap_big)).toInt())

            val params_TextDesc = textDesc.layoutParams
            params_TextDesc.width = screen_width / 3
            textDesc.layoutParams = params_TextDesc
            //textDesc.setPadding((screen_width/resources.getDimension(R.dimen.gap_medium)).toInt(),0,0,(screen_width/resources.getDimension(R.dimen.gap_medium)).toInt())

            val params_TextFacts = textFact.layoutParams
            params_TextFacts.width = screen_width / 3
            textFact.layoutParams = params_TextFacts
            //textFact.setPadding((screen_width/resources.getDimension(R.dimen.gap_medium)).toInt(),0,0,0)
        }

        //style texts
        textTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, (screen_width/resources.getDimension(R.dimen.font_big)).toFloat())
        textDesc.setTextSize(TypedValue.COMPLEX_UNIT_SP, (screen_width/resources.getDimension(R.dimen.font_small)).toFloat())
        textFact.setTextSize(TypedValue.COMPLEX_UNIT_SP, (screen_width/resources.getDimension(R.dimen.font_small)).toFloat())

        textTitle.typeface = robotoLight
        textTitle.isAllCaps = true
        textDesc.typeface = robotoLight
        textFact.typeface = robotoLightItalic

    }//fun


}
