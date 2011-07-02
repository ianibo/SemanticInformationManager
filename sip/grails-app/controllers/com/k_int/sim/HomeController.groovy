package com.k_int.sim

class HomeController {

  def index = { }

  def dashboard = {
    def result = [:]
    result.user = authenticatedUser

    // println "Manual render..."
    // render(view: "dashboard", plugin: "SIPPlugin", model:result)
    result
  }

}
