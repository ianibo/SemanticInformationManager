package com.k_int.sim

import grails.converters.*

class TemplateController {

    def index = { 

      println "template::index"

      if ( authenticatedUser != null ) {
        println "Request made by authenticated user: ${authenticatedUser}"
      }
      else {
        println "Anonymous request"
      }


      def response = [
        'one':'two'
      ]

      render response as JSON;
    }
}
