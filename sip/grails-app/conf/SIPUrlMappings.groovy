class SIPUrlMappings {
    static mappings = {
      // By default make the root app go to the dashboard action
      "/" (controller:"home", action:"dashboard")
      "/resource/create" (controller:"resource", action:"create")
      "/resource/update" (controller:"resource", action:"update")
      "/resource/edit" (controller:"resource", action:"edit")
      "/template/resource/$id" (controller:"template", action:"resourcetemplate")
      "/template/search/$id" (controller:"template", action:"searchtemplate")
      "/data/list" (controller:"data", action:"list")
      "/data/qry" (controller:"data", action:"qry")
      "/data/graph" (controller:"data", action:"graph")
      "/search" (controller:"search", action:"showtemplate")
    }
}
