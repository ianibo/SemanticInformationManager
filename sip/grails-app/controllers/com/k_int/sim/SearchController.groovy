package com.k_int.sim

import grails.converters.*
import com.k_int.sip.domain.*
import groovy.util.Eval;

class SearchController {

  def workspaceService

  def showtemplate = { 
    def result = [:]
    result.user = authenticatedUser
    result.workspace = workspaceService.listAccessibleComponents(authenticatedUser);

    def sst = SIPSearchTemplate.get(params.template)
    
    if ( sst != null ) {
      println "Located search template, parse JSON"
      // Search isn't like edit.. here we take the search form definition json object and parse it into a groovy structure
      // We then build the search page inside the gsp and send html
      result.search_form_model = JSON.parse(sst.jsonDefn());

      // get hold of actual class we are basing our search on
      def target_class = grailsApplication.getArtefact("Domain",sst.owner.defaultType);

      if ( target_class != null ) {

        println "Got instance of ${target_class.name}"
        def c = target_class.getClazz().createCriteria()

        println "Created criteria against target classs: ${c}"
        def recset = c {
          and {
            result.search_form_model.access_points.each { ap ->
              if ( ( params[ap] != null ) && ( params[ap].length() > 0 ) ) {
                like(ap,"${params[ap]}%")
              }
            }
          }
        }

        result.results = []

        recset.each { r ->
          // println "${r.class.name}"
          
          def results_row = []
          result.results.add(results_row)
          result.search_form_model.search_columns.each { sc  ->
            results_row.add(Eval.x(r, 'x.' + sc))
          }
        }

        //	like("holderFirstName", "Fred%")
        //  maxResults(10)
       	//  order("holderLastName", "desc")

      }
      else {
        log.error("unable to locate default type for identified template ${params.template}")
      }
    }
    else {
      println "Unable to locate template with id ${params.template}"
    }

    render(view:'index',model:result)
  }
}
