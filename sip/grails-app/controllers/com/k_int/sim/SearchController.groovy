package com.k_int.sim

import grails.converters.*
import com.k_int.sip.domain.*
import groovy.util.Eval;

class SearchController {

  def workspaceService

  def showtemplate = { 

    log.debug("Staring SearchController::showTemplate")

    def result = [:]
    result.user = authenticatedUser
    result.workspace = workspaceService.listAccessibleComponents(authenticatedUser);

    def sst = SIPSearchTemplate.get(params.template)

    
    if ( sst != null ) {
      log.debug("Located search template, parse JSON")

      // Search isn't like edit.. here we take the search form definition json object and parse it into a groovy structure
      // We then build the search page inside the gsp and send html
      result.search_form_model = JSON.parse(sst.jsonDefn());

      // get hold of actual class we are basing our search on
      def target_class = grailsApplication.getArtefact("Domain",sst.owner.defaultType);

      if ( target_class != null ) {

        println "Got instance of ${target_class.name}"
        def c = target_class.getClazz().createCriteria()

        log.debug("Created criteria against target classs: ${c}")

        // def recset = c.list(max: 5, offset: 10) {
        def recset = c.list(max: 20) {
          and {
            result.search_form_model.access_points.each { ap ->
              if ( ( params[ap.propname] != null ) && ( params[ap.propname].length() > 0 ) ) {
                if ( ap.proptype=='string' ) {
                  like(ap.propname,params[ap.propname])
                }
                else if ( ap.proptype=='long' ) {
                  eq(ap.propname,new Long(Long.parseLong(params[ap.propname])))
                }
                else {
                  eq(ap.propname,"${params[ap.propname]}")
                }
              }
            }
          }
        }

       log.debug("recset.totalCount = ${recset.totalCount}")

        result.results = []

        recset.each { r ->
          // println "${r.class.name}"
          
          def results_row = [:]
          results_row.v = []

          def resource_class_name = r.class.name;
          def i = resource_class_name.indexOf('_$$_javassist')
          if ( i > -1 )
            resource_class_name = resource_class_name[0..i-1]

          results_row.uri = "uri:gorm:${resource_class_name}:${r.id}"
          result.results.add(results_row)
          result.search_form_model.search_columns.each { sc  ->
            results_row.v.add([v:Eval.x(r, 'x.' + sc.property),selaction:sc.selaction])
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
      log.error("Unable to locate template with id ${params.template}")
    }

    render(view:'index',model:result)
    log.debug("completed SearchController::showTemplate")
  }
}
