package com.k_int.sim

import grails.converters.*
import com.k_int.sip.domain.*

class TemplateController {

    // Import the i8n service.. this is getting too heavy for a domain object, should ship all this out to a service really.
    def messageSource

    def resourcetemplate = { 

      log.debug("template::resourcetemplate - Get template ${params.template} params=${params}")
      def template_json = ''

      if ( authenticatedUser != null ) {
        // println "Request made by authenticated user: ${authenticatedUser}"
      }
      else {
        // println "Anonymous request"
      }

      SIPEditTemplate sit = SIPEditTemplate.get(params.id)

      if ( sit != null ) {
        // println "Locate template ${params.id}"
        // Call the abstract getJSON method on the template. For dynamic edit templates, the json will be
        // generated in real time, for static ones, simply pulled from the DB.
        template_json = sit.jsonDefn()
      }
      else {
        log.error("Unable to locate template ${params.id}")
      }

      def model = JSON.parse(template_json)
      convertLabels(model)
      render model as JSON

      // println "template: ${template_json}"
      // render response as JSON;
      // render(text:template_json, contentType:'application/json')
    }

    def searchtemplate = {
      // println "template::searchtemplate - Get template ${params.template} params=${params}"

      def template_json = ''

      SIPSearchTemplate sst = SIPSearchTemplate.get(params.id)

      if ( sst != null ) {
        // println "Locate template ${params.id}"
        // Call the abstract getJSON method on the template. For dynamic edit templates, the json will be
        // generated in real time, for static ones, simply pulled from the DB.
        template_json = sst.jsonDefn()
      }
      else {
        log.error("Unable to locate template ${params.id}")
      }

      // Parse the JSON string into actual java objects, so we can internationalize
      def model = JSON.parse(template_json)
      convertLabels(model)
      render model as JSON
      // render(text:template_json, contentType:'application/json')
    }

    def convertLabels(json_object) {
      if ( json_object instanceof java.util.Map ) {
        convertLabelsInMap(json_object)
      }
      else if ( json_object instanceof java.util.List ) {
        json_object.each {
          convertLabels(it)
        }
      }
    }

    def convertLabelsInMap(json) {
      // def local_to_render = new java.util.Locale("EN")
      json.each { entry ->
        // log.debug("testing ${entry.key}")
        if ( entry.key == 'label' ) {
          // entry.value = messageSource.resolveCode(entry.value, new java.util.Locale("EN")).format(); //'TabNameForClass'
          entry.value = messageSource.getMessage(entry.value, null, entry.value, new java.util.Locale("EN"))
        }
        if ( ( entry.value instanceof java.util.Map ) || ( entry.value instanceof java.util.List ) )
          convertLabels(entry.value);
      }
    }

    def convertLabelsInList(json) {
    }
}
