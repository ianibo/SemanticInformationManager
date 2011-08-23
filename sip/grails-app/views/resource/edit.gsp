<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="simmain"/>

    <script src="${resource(dir:'js',file:'SemanticEditForm.js')}" charset="utf-8"></script> 
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js" type="text/javascript"></script>
    <link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.14/themes/base/jquery-ui.css" type="text/css" media="all" /> 
    <link rel="stylesheet" href="http://static.jquery.com/ui/css/demo-docs-theme/ui.theme.css" type="text/css" media="all" /> 
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.14/jquery-ui.min.js" type="text/javascript"></script> 
    <script src="http://jquery-ui.googlecode.com/svn/tags/latest/external/jquery.bgiframe-2.1.2.js" type="text/javascript"></script> 
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.14/i18n/jquery-ui-i18n.min.js" type="text/javascript"></script> 
  </head>
  <body>
   Template loaded from <a href="${createLink(controller:'template',action:'resourcetemplate',id:template_id)}">${createLink(controller:'template',action:'index',id:template_id)}</a><br/>
   Graph loaded from <a href="${createLink(controller:'home',action:'dashboard')}data/graph?repo=${params.repo}&amp;uri=${params.uri}">${createLink(controller:'home',action:'dashboard')}data/graph?repo=${params.repo}&amp;uri=${params.uri}</a>
   
    <div id="SIMEditForm">
      <ul>
      </ul>
    </div>
    <script language="JavaScript">
      makeSIMEditor("#SIMEditForm",
                    "${createLink(controller:'template',action:'resourcetemplate',id:template_id)}",
                    "${createLink(controller:'home',action:'dashboard')}",
                    "${params.repo}",
                    <g:if test="${params.uri != null}">"${params.uri}"</g:if><g:else>null</g:else>,
                    ["${default_type}"]);
    </script>
    <input type="button" value="Submit" onClick="javascript:sendFormData('${createLink(controller:'resource',action:'update')}')"/>
  </body>
</html>
