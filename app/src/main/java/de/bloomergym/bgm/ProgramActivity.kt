package de.bloomergym.bgm

import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Typeface
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.FileNotFoundException


class ProgramActivity  : AppCompatActivity() {

    private lateinit var myConfig: MyConfig
    private lateinit var helper:Helper
    private lateinit var video:VideoView
    private lateinit var playButton:ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_program)

        val programName = intent.getStringExtra(EXTRA_MESSAGE)

        helper = Helper(applicationContext)
        myConfig = helper.loadConfig()

        setupUI(programName)
    } //fun

    private fun setupUI(programName: String) {
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        //val screenHeight = Resources.getSystem().displayMetrics.heightPixels

        video = findViewById(R.id.videoView)
        playButton = findViewById(R.id.play_button)

        val textTitle: TextView = findViewById(R.id.textTitle)
        val textDesc: TextView = findViewById(R.id.textDesc)
        val textFact: TextView = findViewById(R.id.textFacts)

        val robotoLight = Typeface.createFromAsset(assets, "Roboto-Light.ttf")
        val robotoLightItalic = Typeface.createFromAsset(assets, "Roboto-LightItalic.ttf")

        var program: Program? = null
        myConfig.programs?.forEach { p ->
            if (p.getProgramName() == programName) {
                program = p
                Log.i("Kotlin", "Found match between " + p.getProgramName() + " and " + programName)
            }//if
        }//forEach

        if (program==null){ Log.i("Kotlin", "ProgramActivity: found no match for $programName"); }

        // set texts
        textTitle.text = program?.getProgramName()
        textDesc.text = program?.getProgramDesc()
        textFact.text = program?.getProgramFact()

        //style texts
        textTitle.setTextSize(
            TypedValue.COMPLEX_UNIT_SP,
            (screenWidth / resources.getDimension(R.dimen.font_big))
        )
        textDesc.setTextSize(
            TypedValue.COMPLEX_UNIT_SP,
            (screenWidth / resources.getDimension(R.dimen.font_medium))
        )
        textFact.setTextSize(
            TypedValue.COMPLEX_UNIT_SP,
            (screenWidth / resources.getDimension(R.dimen.font_medium))
        )

        textTitle.typeface = robotoLight
        textTitle.isAllCaps = true
        textDesc.typeface = robotoLight
        textFact.typeface = robotoLightItalic

        val videoWidth = 0.8

        // set video
        this.setupVideo(program?.media, screenWidth * videoWidth)

        //resize everything
        if(this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            val paramsTextTitle = textTitle.layoutParams
            paramsTextTitle.width = (screenWidth * videoWidth).toInt()
            textTitle.layoutParams = paramsTextTitle
            textTitle.setPadding(
                (screenWidth / resources.getDimension(R.dimen.gap_small)).toInt(),
                (screenWidth / resources.getDimension(R.dimen.gap_small)).toInt(),
                0,
                (screenWidth / resources.getDimension(R.dimen.gap_small)).toInt()
            )


            val paramsTextdesc = textDesc.layoutParams

            paramsTextdesc.width = (screenWidth * (1 - videoWidth)).toInt()
            textDesc.layoutParams = paramsTextdesc
            textDesc.setPadding(
                (screenWidth / resources.getDimension(R.dimen.gap_very_small)).toInt(),
                (screenWidth / resources.getDimension(R.dimen.gap_small)).toInt(),
                (screenWidth / resources.getDimension(R.dimen.gap_very_small)).toInt(),
                (screenWidth / resources.getDimension(R.dimen.gap_medium)).toInt()
            )

            val paramsTextfacts = textFact.layoutParams
            paramsTextfacts.width = (screenWidth * (1 - videoWidth)).toInt()
            textFact.layoutParams = paramsTextfacts
            textFact.setPadding(
                (screenWidth / resources.getDimension(R.dimen.gap_very_small)).toInt(),
                0,
                (screenWidth / resources.getDimension(R.dimen.gap_very_small)).toInt(),
                (screenWidth / resources.getDimension(R.dimen.gap_very_small)).toInt()
            )
        }


    }//fun

    private fun setupVideo(media: String?, width:Double) {
        //load video
        try {
            video.setVideoPath(helper.getMediaPath(media))
        } catch (e: FileNotFoundException)
        { // todo: delete else case, access via resources only for testing
            // get resource id for video, based on config file, and set video file accordingly
            // -> leads e.g. to R.raw.logo_flughafen
            val resVideo:Int = this.resources.getIdentifier(
                media,
                "raw",
                this.packageName
            )
            video.setVideoPath("android.resource://$packageName/$resVideo")
        }

        video.requestFocus()
        //video.start() -> start with button
        video.seekTo(10)
        video.setOnCompletionListener {
            resetVideo()
        }

        //resize
        if(this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val paramsVideo = video.layoutParams
            paramsVideo.width = (width).toInt()
            video.layoutParams = paramsVideo
            video.setPadding(//l t r b
                0, 0, 0, 0
            )
        }//if
    }//fun

    private fun startVideo(){
        video.start()
        playButton.visibility = View.GONE

        // add controls
        val mediaController = MediaController(this)
        video.setMediaController(mediaController)
        mediaController.setMediaPlayer(video)
    }
    private fun resetVideo(){

        video.seekTo(10)
        playButton.visibility = View.VISIBLE
    }

    private fun pressPlay(view: View) {
        Log.i("Kotlin", "ProgramActivity: called pressPlay")

        if (video.isPlaying) {
            Log.i("Kotlin", "ProgramActivity: if - should never be called")

            video.stopPlayback()
        } else {
            Log.i("Kotlin", "ProgramActivity: else")
            startVideo()
        }
    }//fun
}
