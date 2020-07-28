package de.bloomergym.bgm

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button


//open public class CustomAdapter() : ArrayAdapter<String>(context:Context, ) {
class OverviewListAdapter : ArrayAdapter<String> {

    private lateinit var c:Context
    private lateinit var inflater: LayoutInflater
    private lateinit var programs: Array<String?>
    private var height =0
    private var width = 0
    private var textSize:Float = 0F
    private var padding = 0

    constructor(ctx: Context, i:Int, p: Array<String?>, height: Int, width:Int, textsize:Float, padding:Int) : super(ctx,i,p){
        this.c=ctx
        this.inflater = LayoutInflater.from(ctx)
        this.programs=p
         this.height=height
        this.width =width
        this.textSize=textsize
        this.padding=padding
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //Log.i("Kotlin", "CustomAdapter -> getView() with position = " + position + ", convertview = " + convertView +", parent = " + parent)
        var listItemView: View= inflater.inflate(R.layout.list_item_overview, parent, false)
        val b: Button = listItemView.findViewById(R.id.program_button)
        b.text = programs.get(position)

        val params = b.layoutParams
        params.width = width
        params.height=height

        b.layoutParams = params
        b.setPadding(padding, padding, padding, padding)

        b.textSize = textSize

        val robotoLight = Typeface.createFromAsset(context.assets, "Roboto-Light.ttf")
        b.typeface = robotoLight

        return listItemView
    }

    override fun getItem(position: Int): String? {
        //TODO("test")
        return programs.get(position)!!
    }

    override fun getItemId(position: Int): Long {
        //TODO("test")
        return position.toLong()
    }

    override fun getCount(): Int {
        //TODO("test")
        val size = programs.size
        if(size != null) {
            return size}
        else return 0
    }

}