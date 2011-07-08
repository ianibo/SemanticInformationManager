package com.k_int.sip.domain

class SIPRepository {

    String name
    String uri

    static constraints = {
    }

    String generateDynamicTemplate(SIPContext ctx) {
      println "SIPRepository::generateDynamicTemplate"
    }

    def processUpdate(model) {
      println "SIPRepository::processUpdate"
    }
}
