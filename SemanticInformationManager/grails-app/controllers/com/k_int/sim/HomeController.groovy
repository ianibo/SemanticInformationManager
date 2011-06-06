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
    result.new_resource_uri = "urn:uri:${java.util.UUID.randomUUID().toString()}"
    println "Generated a new resource: ${result.new_resource_uri}"
    result
  }

  def search = {
    def result = [:]
    result.user = authenticatedUser
    result
  }
}
