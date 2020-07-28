package ruemelin.de.bgm

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import com.google.gson.Gson
import java.io.*

class Helper {

    val config_file = "config.json"
    val config_folder = "Config"
    lateinit var context:Context

    constructor(ctx: Context) {
        this.context=ctx
    }

    public fun getMediaPath(media:String?):String{
        val moviePath: String = getAppSpecificMovieDir(context)?.getAbsolutePath() + "/" + media
        Log.i("Kotlin", "Helper: Trying to load movie from " + moviePath)
        return moviePath
    }

    fun getLogoBitmap(company: String?): Bitmap? {
        val imagePath: String = getAppSpecificPictureDir(context)?.getAbsolutePath() + "/" + "logo_" + company + ".png"
        Log.i("Kotlin", "Helper: Trying to load " + "logo_" + company + " from " + imagePath)
        try {
            return BitmapFactory.decodeFile(imagePath)
        } catch (e:IllegalStateException){
            return null
        }
    }

    private fun getAppSpecificPictureDir(context: Context): File? {
        // Get the pictures directory that's inside the app-specific directory on
        // external storage.
        //subfolder?
        //val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName)
        val file = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        //Log.e("Kotlin", "Helper: Picture file location is: " + file)
        if (file?.mkdirs()!!) {
            Log.i("Kotlin", "Helper: Picture file directory has just been created: " + file)
        }
        else {
            Log.i("Kotlin", "Helper: Picture file directory already exists at: " + file)
        }
        return file
    }

    fun getAppSpecificMovieDir(context: Context): File? {
        val file = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)

        if (file?.mkdirs()!!) {
            Log.i("Kotlin", "Helper: Movie file directory has just been created: " + file)
        }
        else {
            Log.i("Kotlin", "Helper: Movie file directory already exists at: " + file)
        }
        return file
    }

    public fun writeConfig(context:Context, json:String){
        val path = context.getExternalFilesDir(null)
        val configDirectory = File(path, config_folder)
        if (!configDirectory.exists()) {
            configDirectory.mkdirs()
        }
        val file = File(configDirectory, config_file)

        FileOutputStream(file).use { it.write(json?.toByteArray())}
    }

    public fun loadConfig():MyConfig{
        lateinit var myConfig: MyConfig

        // val pathInt = ctx.getFilesDir()
        val path = context.getExternalFilesDir(null)
        val configDirectory = File(path, config_folder)
        if (!configDirectory.exists()) {
            configDirectory.mkdirs()
        }
        val file = File(configDirectory, config_file)


        //write
        /*FileOutputStream(file).use {
            it.write("First record goes here. ".toByteArray())
        }
        file.appendText("And second record goes here")

        //read
        val inputAsString = FileInputStream(file).bufferedReader().use { it.readText() }
        Log.i("Kotlin", "Helper: just wrote " + inputAsString + " to file " + file.path);
*/

        // try to load config.json from local file dir
        var fis: FileInputStream
        try {
            Log.i("Kotlin", "Helper: try to open config file from " + file.name)
            fis = FileInputStream(file)
            Log.i("Kotlin", "Helper: in try, file seems to exist, no exception")

        } catch (e: IOException) {
            Log.i(
                "Kotlin",
                "Helper: in catch, config.json does not exist locally, will be created with content from assets: " + e.toString()
            )

            // read config data from asset file
            var inputString: String? = ""
            try {
                inputString = context.assets.open(config_file).bufferedReader().use { it.readText() }
                //Log.i("Kotlin", "Read from asset folder: " +inputString)
            } catch (ioException: IOException) {
                Log.i("Kotlin","Helper: Something went wrong when reading config.json from asset folder")
            }
            // and write to the newly create file
            FileOutputStream(file).use { it.write(inputString?.toByteArray()) }
            fis = FileInputStream(file)

            Log.i("Kotlin", "Helper: config file should have been created: " + file.name)
        }

        // now read from fileinputstream and fill myConfig
        try {
            val inputAsString = fis.bufferedReader().use { it.readText() }
            Log.i("Kotlin", "Helper: read from stored file config.json: " + inputAsString)
            val gson = Gson()
            myConfig = gson.fromJson(inputAsString, MyConfig::class.java)
        } catch (e: FileNotFoundException) {
            Log.i("Kotlin", "Helper: catch FileNotFoundException: " + e.toString())
        }

        return myConfig
    }

}