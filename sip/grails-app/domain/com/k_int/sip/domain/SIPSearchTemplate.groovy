package com.k_int.sip.domain

class SIPSearchTemplate {

    String shortcode
    String name
    String uri
    SIPContext owner

    static belongsTo = { owner : SIPContext }

    static constraints = {
    }
}
