package de.bloomergym.bgm

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.get
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_config.*
import java.lang.Exception

class ConfigActivity : AppCompatActivity() {

    private lateinit var myConfig: MyConfig
    private lateinit var helper: Helper
    lateinit var currentCompany:Company

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        helper = Helper(applicationContext)
        myConfig = helper.loadConfig();
        setupUI()
    }

    private fun setupUI() {
        val company_radioGroup: RadioGroup = findViewById(R.id.radioGroup_config)
        val program_listView: ListView = findViewById(R.id.listView_config)
        val screen_width = Resources.getSystem().displayMetrics.widthPixels

        // access programs from myconfig
        val programs = myConfig.programs

        // access companies from myconfig
        val companies = myConfig.companies
        // todo do not default initialize with first
        currentCompany = companies!![0]

        // show companies as radio button list, active one is activated
        if (companies != null) {
            companies.forEach { c ->
                val r: RadioButton = RadioButton(applicationContext)
                r.setText(c.getCompanyId())
                r.id = View.generateViewId()
                company_radioGroup.addView(r)
                if (c.getCompanyId()==myConfig.currentCompany){
                    currentCompany = c
                    company_radioGroup.check(r.id)
                }
            }
        }

        company_radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            // This will get the radiobutton that has changed in its check state

            val checkedRadioButton =
                group.findViewById<View>(checkedId) as RadioButton
            // This puts the value (true/false) into the variable
            val isChecked = checkedRadioButton.isChecked
            // If the radiobutton that has changed in check state is now checked...
            Log.i("Kotlin","ConfigActivity: " + checkedRadioButton.text
                    + " has been checked, change checkboxes and currentComp in json")

            // write change to json
            myConfig.currentCompany= checkedRadioButton.text.toString()
            updateJson();

            currentCompany = getCurrentCompany(checkedRadioButton.text as String)
            setupPrograms(myConfig.programs)
        })

        if (programs != null) {
            setupPrograms(programs)
        }


    } // end of setupUI

    fun updateJson(){
        val gson = Gson()
        val jsonString = gson.toJson(myConfig)
        helper.writeConfig(applicationContext,jsonString)

    }

    fun setupPrograms(programs:List<Program>?){

        Log.i("Kotlin","ConfigActivity: setupPrograms ")

        var list = emptyArray<String?>()
        if (programs != null){
            val size: Int = programs.size
            list = arrayOfNulls<String>(size)

            for (i in 0..size - 1) {
                val p = programs.get(i)
                list[i] = p.getProgramName()
            }
        }

        var adapter = ConfigListAdapter(this,
            R.layout.list_item_config,
            list,
            20.toFloat(),
            400,
            90,
            0,
            programs, //all available programs
            currentCompany.chosen_programs
        )
        listView_config.adapter = adapter



    }

    fun handleCbClick(view: View) {
        var cb: CheckBox? = view as CheckBox?
        if (cb!!.isChecked()) {
            // checked
            Log.i("Kotlin", "ConfigActivity: "+cb.text +" was checked")
            // change chosen program list in myconfig for current comp
            myConfig.addChosenProgram(cb.text as String);
            updateJson();
            Log.i("Kotlin", "ConfigActivity: todo")
        }
        else
        {
            // not checked
            Log.i("Kotlin", "ConfigActivity: "+cb.text +" was unchecked")
            myConfig.removeChosenProgram(cb.text as String);
            updateJson();
        }
    }

    fun getCurrentCompany(name:String):Company{//}, companies:List<Company>) : Company {

        val companies = myConfig.companies

        // todo default = 0?
        var comp:Company = companies!![0]

        // show companies as radio button list, active one is activated
        if (companies != null) {
            companies.forEach { c ->
                if (c.getCompanyId()==myConfig.currentCompany){
                    comp = c
                }
            }
        }
        return comp
    }


    fun addProgram(v: View) {

    }
}