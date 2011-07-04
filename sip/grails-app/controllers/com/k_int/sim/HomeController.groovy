package com.k_int.sim


class HomeController {

  def workspaceService
  def index = { }

  def dashboard = {
    def result = [:]
    result.user = authenticatedUser
    result.workspace = workspaceService.listAccessibleComponents(authenticatedUser);

    result
  }

}
