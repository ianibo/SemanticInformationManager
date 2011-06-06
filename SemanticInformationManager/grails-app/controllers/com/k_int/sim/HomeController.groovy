package com.k_int.sim

class HomeController {

  def index = { 
    def result = [:]
    println "Home"
    // def subject = SecurityUtils.getSubject()
    // println "Hello ${subject.principal} - auth? ${subject.authenticated} - injected: ${authenticatedUser}"
    println "User: ${authenticatedUser.username}"
    result.user = authenticatedUser

    // println "done"
    result
  }

  def dashboard = { 
    def result = [:]
    result.user = authenticatedUser
    result
  }

  def create = {
    def result = [:]
    result.user = authenticatedUser
    result
  }

  def search = {
    def result = [:]
    result.user = authenticatedUser
    result
  }
}
