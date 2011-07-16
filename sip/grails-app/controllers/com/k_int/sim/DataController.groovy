package com.k_int.sim

import grails.converters.*
import com.k_int.sip.domain.*;

class DataController {

  def index = { }

  def list = {
    def result = []

    println "Listing for repo: ${params.repo} type: ${params.typeUri} displayProps:${params.displayProps}"

    if ( params.repo != null ) {

      def props = null

      if ( ( params.displayProps != null ) && ( params.displayProps.length() > 0 ) ) {
        props = params.displayProps.split(',')
      }
     
      // Action needs to be driven by base repo - Each repo needs to understand how to handle references to it's own type.
      def repo = SIPRepository.get(params.repo)
      if ( repo != null ) {
        // Located repository, now ask the repository to deal with the list request
        result = repo.list(params.typeUri, props)
      }
    }

    render result as JSON
  }
}
