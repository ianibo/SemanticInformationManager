package com.k_int.sip.domain

class SIPEditTemplate {

    String name
    String uri
    SIPContext owner

    static belongsTo = { owner : SIPContext }

    static constraints = {
    }
}