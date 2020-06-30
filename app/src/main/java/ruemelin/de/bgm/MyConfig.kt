package ruemelin.de.bgm

import android.util.Log
import java.lang.NumberFormatException

class MyConfig{
    var currentCompany: String? = null
    var url: String? = null
    var companies: List<Company>? = null
    var programs: List<Program>? = null
    constructor() : super() {}

    constructor(CurrentCompany: String, Url: String,Companies: List<Company>,Programs: List<Program>) : super() {
        this.currentCompany = CurrentCompany
        this.url = Url
        this.companies = Companies
        this.programs = Programs
    }
}


class Company
{
    var id:String = "" //only lowercase
    var name:String = "" // as printed
    var AOK: Boolean?= null
    var chosen_programs: List<String>? = null

    constructor() : super() {}

    constructor(Id: String, Name: String, AOK: Boolean, Programs: List<String>) : super() {
        this.id=Id
        this.name = Name
        this.AOK = AOK
        this.chosen_programs=Programs
    }

    fun getCompanyId():String{
        return this.id
    }

    fun printOut():String{
        //return name + "/" + AOK.toString();
        return this.name + "("+this.id + ")";
    }
}

class Program
{
    var name:String = "";
    var media:String? = null
    var description:String? = null
    var health_fact:String? = null
    var star_average:Double? = null

    constructor() : super() {}

    constructor(Name: String, Media: String, Description:String, Health_Fact:String, Star_Average:String) : super() {
        this.name = Name
        this.media = Media
        this.description=Description
        this.health_fact =Health_Fact
        this.star_average=0.0
        try{
            this.star_average=Star_Average.toDouble()
        } catch(e:NumberFormatException){
            Log.i("Kotlin","Tried to parse program star rating, but conversion from String to Double failed. (program name: " + name + ")");
        }
    }

    fun getProgramName():String?{
        return this.name;
    }
    fun getProgramDesc(): String? {
        return this.description;
    }
    fun getProgramFact():String?{
        return this.health_fact;
    }

    fun printOut():String{
        return this.name;
    }

}
