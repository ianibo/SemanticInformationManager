package com.k_int.sim

import grails.converters.*

class ResourceController {

  def workspaceService

  def edit = { 
    def result = [:]
    result.user = authenticatedUser
    result.workspace = workspaceService.listAccessibleComponents(authenticatedUser);

    result
  }

  def create = { 
    def result = [:]
    result.user = authenticatedUser
    result.workspace = workspaceService.listAccessibleComponents(authenticatedUser);

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

    def result = [ result:'OK' ]

    //render(view:'edit',model:result)
    render result as JSON
  }

}
