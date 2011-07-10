class SIPUrlMappings {
    static mappings = {
      // By default make the root app go to the dashboard action
      "/" (controller:"home", action:"dashboard")
      "/resource/create" (controller:"resource", action:"create")
      "/resource/update" (controller:"resource", action:"update")
      "/template/resource/$id" (controller:"template", action:"resourcetemplate")
      "/template/search/$id" (controller:"template", action:"searchtemplate")
      "/search" (controller:"search", action:"showtemplate")
    }
}
