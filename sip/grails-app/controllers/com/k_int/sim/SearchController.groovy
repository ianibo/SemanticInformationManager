package com.k_int.sim

class SearchController {

  def workspaceService

  def index = { 
    def result = [:]
    result.user = authenticatedUser
    result.workspace = workspaceService.listAccessibleComponents(authenticatedUser);
    result
  }
}
