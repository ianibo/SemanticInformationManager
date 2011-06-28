package com.k_int.sim

class NewSPARQLSearchController {

  def index = { 
    def result = [:]
    result.user = authenticatedUser
    result
  }
}
