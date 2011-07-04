package com.k_int.sip.domain

/**
 * A context which denotes a particular way of accessing the resources in a repository. Contexts can
 * be used to organise both searches and record creation workflows.
 */
class SIPContext {

    SIPRepository owner

    String contextUri
    String contextName
    String contextType

    static belongsTo [owner:SIPRepository]

    static constraints = {
    }
}
