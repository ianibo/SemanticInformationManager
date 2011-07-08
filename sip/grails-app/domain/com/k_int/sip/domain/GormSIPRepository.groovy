package com.k_int.sip.domain

import grails.converters.*

class GormSIPRepository extends SIPRepository {

    def grailsApplication

    static constraints = {
    }

    // Generate a dynamic template for the identified context.
    String generateDynamicTemplate(SIPContext ctx) {

      def new_layout = [:]
      new_layout.element_id='tab1'
      new_layout.tab_name='TabNameForClass'
      new_layout.properties = []

      def target_class_info = grailsApplication.getArtefact("Domain",ctx.defaultType);
      if ( target_class_info != null ) {

        target_class_info.getPersistentProperties().each { pprop ->
          // println "${pprop}..."
          if ( pprop.association ) {
            // So far, only handle scalar types
          }
          else {
            new_layout.properties.add([label:pprop.name,property_uri:pprop.name,cardinality:'1',type:pprop.typePropertyName])
          }
        }
      }
      else {
        println "Unable to locate domain class"
      }

      def converter = new_layout as JSON;
      def json_string = converter.toString()

      return json_string
    }

}
