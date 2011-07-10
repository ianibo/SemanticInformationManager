<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="simmain"/>

    <link type="text/css" rel="stylesheet" href="http://yui.yahooapis.com/3.3.0/build/cssfonts/fonts-min.css" />
    <script src="http://yui.yahooapis.com/3.3.0/build/yui/yui-min.js" charset="utf-8"></script>

    <script src="${resource(dir:'js',file:'SemanticEditForm.js')}" charset="utf-8"></script>
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
    <div id="searchactioncontainer" class="yui3-g">
      <div id="searchlhs" class="yui3-u-1-2">
        <div id="searchform" class="graypanel">
          <ul id="accesspoints">
            <li>Text:<input type="text" name="textvalue"/></li>
          </ul>
          <input type="button" value="Search!"/>
        </div>
        <div id="searchresults">
This is the results area
        </div>
      </div>
      <div id="searchrhs" class="yui3-u-1-2">
This is the record display area
      </div>
    </div>
    <g:render template="/search/search" plugin="sip" model="['book':'book','author':'author']" />
  </body>
</html>

