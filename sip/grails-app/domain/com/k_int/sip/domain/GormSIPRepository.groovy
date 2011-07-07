package com.k_int.sip.domain

class GormSIPRepository extends SIPRepository {

    static constraints = {
    }

    // Generate a dynamic template for the identified context.
    String generateDynamicTemplate(SIPContext ctx) {
      println "GormSIPRepository::generateDynamicTemplate"
      return '{"template":"t"}'
    }

}
