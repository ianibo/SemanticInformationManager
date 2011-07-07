package com.k_int.sip.domain

class GormSIPRepository extends SIPRepository {

    def grailsApplication

    static constraints = {
    }

    // Generate a dynamic template for the identified context.
    String generateDynamicTemplate(SIPContext ctx) {
      println "GormSIPRepository::generateDynamicTemplate - Target domain class is ${ctx.defaultType}"
      println "GrailsApplication: ${grailsApplication}"
      def target_class_info = grailsApplication.getArtefact("Domain",ctx.defaultType);
      if ( target_class_info != null ) {
        println "Located domain class"
      }
      else {
        println "Unable to locate domain class"
      }
      return '{"template":"t"}'
    }

}
