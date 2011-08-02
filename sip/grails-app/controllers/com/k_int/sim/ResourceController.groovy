package com.k_int.sim

import grails.converters.*
import com.k_int.sip.domain.*

class ResourceController {

  def workspaceService

  /**
   *  Edit is passed a repo id and a resource URI (Which must be resolvable in the context of the repo)
   */
  def edit = { 
    log.debug("edit - params=${params}")

    def result = [:]
    result.user = authenticatedUser
    result.workspace = workspaceService.listAccessibleComponents(authenticatedUser);
    result.repo = SIPRepository.get(params.repo)

    // If the user specified an object to edit
    if ( params.uri != null ) {
      // def object_to_edit = result.repo.resolveURI(params.uri);

      // Did the user specify a template to use? if so, pass that to the javascript, else look up what the default template for objects of this class is
      if ( params.template != null ) {
        result.template_id = params.template;
      }
      else {
        log.debug("No explicit template given, identify appropriate default for uri: ${params.uri}");
        result.template_id = result.repo.repository().getDefaultTemplateFor(params.uri, authenticatedUser)
      }
    }

    result
  }

  def create = { 
    log.debug("create - params=${params}")
    def result = [:]
    result.user = authenticatedUser
    result.workspace = workspaceService.listAccessibleComponents(authenticatedUser);
    def template_info = SIPEditTemplate.get(params.template)
    result.template_id = params.template
    result.default_type = template_info.owner.defaultType
    render(view:'edit',model:result)
  }

  def update = { 
    log.debug("update - params=${params}")

    def model = JSON.parse(params.model)
    println "${model}"

    // Find out which repository this object is targetted at
    def repository = SIPRepository.get(model.target_repository_id)

    if ( repository != null ) {
      repository.repository().processUpdate(model)
    }

    def result = [ result:'OK' ]

    render result as JSON
  }


}
