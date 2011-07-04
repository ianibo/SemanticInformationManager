class SIPUrlMappings {
    static mappings = {
      // By default make the root app go to the dashboard action
      "/" (controller:"home", action:"dashboard")
      "/resource/${uri}" (controller:"resource", action:"edit")
    }
}
