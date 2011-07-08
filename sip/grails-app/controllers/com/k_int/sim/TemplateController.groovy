package com.k_int.sim

import grails.converters.*
import com.k_int.sip.domain.*

class TemplateController {

    def index = { 

      println "template::index - Get template ${params.template} params=${params}"
      def template_json = ''

      if ( authenticatedUser != null ) {
        println "Request made by authenticated user: ${authenticatedUser}"
      }
      else {
        println "Anonymous request"
      }

      SIPEditTemplate sit = SIPEditTemplate.get(params.id)

      if ( sit != null ) {
        println "Locate template ${params.id}"
        // Call the abstract getJSON method on the template. For dynamic edit templates, the json will be
        // generated in real time, for static ones, simply pulled from the DB.
        template_json = sit.jsonDefn()
      }
      else {
        println "Unable to locate template ${params.id}"
      }

      // println "template: ${template_json}"
      // render response as JSON;
      render(text:template_json, contentType:'application/json')
    }
}
