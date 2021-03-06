package com.k_int.sip.domain

class DynamicSIPEditTemplate extends SIPEditTemplate {

    static constraints = {
    }

    // static transients = [ 'json' ] 
    public String jsonDefn() {
      println "DynamicSIPEditTemplate::jsonDefn()"
      // This template is owned by a context, which is in turn owned by a repository
      // Templates are dependent upon the capabilities of the repository, therefore, we delegate 
      // this call to the repository, to create an appropriate template for the class of repository
      return owner.owner.repository().generateDynamicEditTemplate(owner);
    }
}
