package Model

import com.google.android.libraries.places.api.model.OpeningHours
import com.google.android.libraries.places.api.model.PlusCode

class Results {
    var business_status:String?=null
    var name:String?=null
    var geometry:Geometry?=null
    var icon:String?=null
    var plus_code:PlusCode?=null
    var rating:Double?=null
    var openingHours:OpeningHours?=null
    var photos:Array<Photos>?=null
    var place_id:String?=null
    var reference:String?=null
    var types:Array<String>?=null
    var scope:String?=null
    var vicinity:String?=null
}