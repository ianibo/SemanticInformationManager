<html>
    <head>
        <title>IEP - Information Enhancement Portal</title>
        <link type="text/css" rel="stylesheet" href="http://yui.yahooapis.com/3.3.0/build/cssfonts/fonts-min.css" />
        <script src="http://yui.yahooapis.com/3.3.0/build/yui/yui-min.js" charset="utf-8"></script>
        <meta name="layout" content="main" />
    </head>
    <body>

      <form>
        <div id="resourceid">
          Resource ID: <input type="text" name="resourceid" value="${new_resource_uri}" />
        </div>

        <div id="addclass">
          This resource is-a :
          <select name="addClass">
            <option value="Widget">Widget</option>
          </select>

          <input type="submit" value="Add"/>
        </div>
      </form>
    </body>
</html>

