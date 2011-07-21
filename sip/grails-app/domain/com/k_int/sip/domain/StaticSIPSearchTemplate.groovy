package com.k_int.sip.domain

class StaticSIPSearchTemplate extends SIPSearchTemplate {

  String templateText

    static constraints = {
    }

    public String jsonDefn() {
      return templateText
    }

  static mapping = {
    templateText type:'text'
  }
}
