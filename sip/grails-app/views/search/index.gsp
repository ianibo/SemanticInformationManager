<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="simmain"/>

    <link type="text/css" rel="stylesheet" href="http://yui.yahooapis.com/3.3.0/build/cssfonts/fonts-min.css" />
    <script src="http://yui.yahooapis.com/3.3.0/build/yui/yui-min.js" charset="utf-8"></script>

    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js" type="text/javascript"></script>
    <link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.14/themes/base/jquery-ui.css" type="text/css" media="all" />
    <link rel="stylesheet" href="http://static.jquery.com/ui/css/demo-docs-theme/ui.theme.css" type="text/css" media="all" />
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.14/jquery-ui.min.js" type="text/javascript"></script>
    <script src="http://jquery-ui.googlecode.com/svn/tags/latest/external/jquery.bgiframe-2.1.2.js" type="text/javascript"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.14/i18n/jquery-ui-i18n.min.js" type="text/javascript"></script>
  </head>
  <body>
    Search view
    The search view consists of a LHS which is a search form and a search results block and a RHS which is a record detail block.
      <div id="searchlhs">
        <div id="searchform" class="graypanel">
          <form method="get">
            <input type="hidden" name="template" value="${params.template}"/>
            <input type="hidden" name="repo" value="${params.repo}"/>
            <ul id="accesspoints">
              <g:each in="${search_form_model.access_points}" var="prop">
              <li>${prop}:<input type="text" name="${prop}"/></li>
              </g:each>
            </ul>
            <input type="submit" value="Search!!!"/>
          </form>
        </div>
        <div id="searchresults">
This is the results area
          <table>
            <thead>
              <tr>
                <g:each in="${search_form_model.search_columns}" var="prop">
                  <td>${prop}</td>
                </g:each>
              </tr>
            </thead>
            <tbody>
            </tbody>
          </table>
        </div>
      </div>
    <!-- g:render template="/search/search" plugin="sip" model="['book':'book','author':'author']" -->
  </body>
</html>

