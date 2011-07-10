package com.k_int.sim

class SearchController {

  def workspaceService

  def showtemplate = { 
    def result = [:]
    result.user = authenticatedUser
    result.workspace = workspaceService.listAccessibleComponents(authenticatedUser);
    println "SearchController::template ${params.id}"
    render(view:'index',model:result)
  }
}
