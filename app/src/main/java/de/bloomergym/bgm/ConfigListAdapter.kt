package de.bloomergym.bgm


import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox

class ConfigListAdapter : ArrayAdapter<String> {

    private lateinit var c:Context
    private lateinit var inflater: LayoutInflater
    private lateinit var programs: Array<String?>
    private var programsAll: List<Program>?
    private var programsSelected: List<String>?
    private var height =0
    private var width = 0
    private var textSize:Float = 0F
    private var padding = 0

    constructor(
        ctx: Context, i:Int, a: Array<String?>, textsize: Float, width:Int, height: Int,
        padding:Int, pa: List<Program>?, ps: List<String>?
    ) : super(ctx,i,a){
        this.c=ctx
        this.inflater = LayoutInflater.from(ctx)
        this.programs=a
        this.programsAll=pa
        this.programsSelected=ps
        this.textSize=textsize
        this.width =width
        this.padding=padding
        this.height=height
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView: View = inflater.inflate(R.layout.list_item_config, parent, false)
        val cb: CheckBox = listItemView.findViewById(R.id.checkBox)
        if (programsAll != null) {
            val p = programsAll!!.get(position)
            if (p != null) {cb.text = p.getProgramName()}

            programsSelected?.forEach { ps ->
                if (p.getProgramName() == ps){
                    cb.setChecked(true)
                }
            }
        }

        val params = cb.layoutParams
        params.width = width
        params.height = height

        cb.layoutParams = params
        cb.setPadding(0, padding, 0, padding)

        cb.textSize = textSize

        val robotoLight = Typeface.createFromAsset(context.assets, "Roboto-Light.ttf")
        cb.typeface = robotoLight


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