class SIPUrlMappings {
    static mappings = {
      // By default make the root app go to the dashboard action
      "/" (controller:"home", action:"dashboard")
      "/resource/create" (controller:"resource", action:"create")
      "/resource/update" (controller:"resource", action:"update")
      "/template/$id" (controller:"template", action:"index")
      "/search/template/$id" (controller:"search", action:"showtemplate")
    }
}
