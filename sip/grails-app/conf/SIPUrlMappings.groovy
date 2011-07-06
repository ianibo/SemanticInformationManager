class SIPUrlMappings {
    static mappings = {
      // By default make the root app go to the dashboard action
      "/" (controller:"home", action:"dashboard")
      "/resource/create" (controller:"resource", action:"create")
      "/template/$id" (controller:"template", action:"index")
    }
}
