package com.k_int.sip.domain

class SIPEditTemplate {

    String shortcode
    String name
    String uri
    SIPContext owner

    static belongsTo = { owner : SIPContext }

    static constraints = {
    }

 
    //static transients = [ 'json' ] 
    //public abstract String getJson();

}
