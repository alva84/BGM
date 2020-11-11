package de.bloomergym.bgm

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class TeamListAdapter : ArrayAdapter<String> {

    private lateinit var c:Context
    private lateinit var inflater: LayoutInflater
    private lateinit var info_items: Array<String?>
    private var height =0
    private var width = 0
    private var textSize:Float = 0F
    private var padding = 0

    constructor(ctx: Context, i:Int, p: Array<String?>, textsize: Float, width:Int, padding:Int) : super(ctx,i,p){ //height: Int
        this.c=ctx
        this.inflater = LayoutInflater.from(ctx)
        this.info_items=p
        this.textSize=textsize
        this.width =width
        this.padding=padding
        /*this.height=height*/
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView: View= inflater.inflate(R.layout.list_item_team2, parent, false)
        val t: TextView = listItemView.findViewById(R.id.team_info)
        t.text = info_items.get(position)

        val params = t.layoutParams
        params.width = width-2*padding
        t.layoutParams = params
        t.setPadding(0, padding, 0, padding)

        /*
        params.height=height
        */

        t.textSize = textSize

        val robotoLight = Typeface.createFromAsset(context.assets, "Roboto-Light.ttf")
        t.typeface = robotoLight

        return listItemView
    }

    override fun getItem(position: Int): String? {
        //TODO("test")
        return info_items.get(position)!!
    }

    override fun getItemId(position: Int): Long {
        //TODO("test")
        return position.toLong()
    }

    override fun getCount(): Int {
        //TODO("test")
        val size = info_items.size
        if(size != null) {
            return size}
        else return 0
    }

}