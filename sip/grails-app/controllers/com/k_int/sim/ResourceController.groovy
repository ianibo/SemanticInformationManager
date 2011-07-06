package com.k_int.sim

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

}
