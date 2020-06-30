package ruemelin.de.bgm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button

//open public class CustomAdapter() : ArrayAdapter<String>(context:Context, ) {
public class CustomAdapter() : BaseAdapter() {

    private var inflater: LayoutInflater? = null
    private var programs: Array<String>? = null;

    constructor(ctx: Context, i:Int, p:Array<String>) : this(){
        this.inflater = LayoutInflater.from(ctx)
        this.programs=p
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        TODO("Not yet implemented")
        //convertView = inflater?.inflate( R.layout.overview_item, parent, false );
        if (convertView ==null) {
            convertView = inflater?.inflate(R.layout.overview_item2, parent, false);
            val b: Button? = convertView?.findViewById(R.id.program_button)
            b?.text = "test"
        }
    }

    override fun getItem(position: Int): Any {
        TODO("test")
        return programs?.get(position)!!;
    }

    override fun getItemId(position: Int): Long {
        TODO("test")
        return position.toLong()
    }

    override fun getCount(): Int {
        TODO("test")
        val size = programs?.size
        if(size != null) {
            return size}
        else return 0;
    }

}