package com.k_int.sim

import grails.converters.*
import com.k_int.sip.domain.*

class ResourceController {

  def workspaceService

  def edit = { 
    def result = [:]
    result.user = authenticatedUser
    result.workspace = workspaceService.listAccessibleComponents(authenticatedUser);
    result.repo = SIPRepository.get(params.repo)

    

    result
  }

  def create = { 
    def result = [:]
    result.user = authenticatedUser
    result.workspace = workspaceService.listAccessibleComponents(authenticatedUser);
    def template_info = SIPEditTemplate.get(params.template)
    result.default_type = template_info.owner.defaultType
    render(view:'edit',model:result)
  }

  def update = { 
    println "update - params=${params}"

    //def result = [:]
    //result.user = authenticatedUser
    //result.workspace = workspaceService.listAccessibleComponents(authenticatedUser);

   // println "Parsing model"
    def model = JSON.parse(params.model)
    println "${model}"

    //println "Target repository is ${model.target_repository_id}"
    //model.graph.each { res ->
    //  println "Processing resource: ${res.key}"
    //  println "__metamodel = ${res.value['__metamodel']}"
    //  res.value.each { prop ->
    //  println "Processing property ${prop.key} = ${prop.value}"
    //  }
    //}

    def repository = SIPRepository.get(model.target_repository_id)

    if ( repository != null ) {
      repository.processUpdate(model)
    }

    // Find out which repository this object is targetted at
    def result = [ result:'OK' ]

    render result as JSON
  }

}
