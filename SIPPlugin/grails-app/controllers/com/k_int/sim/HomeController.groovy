package com.k_int.sim

class HomeController {

  def index = { }

  def dashboard = {
    def result = [:]
    result.user = authenticatedUser
    result
  }

}
