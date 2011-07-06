package com.k_int.sim

import grails.converters.*

class TemplateController {

    def index = { 

      println "template::index"

      def response = [
        'one':'two'
      ]

      render response as JSON;
    }
}
