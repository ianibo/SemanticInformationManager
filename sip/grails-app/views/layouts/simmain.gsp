<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'reset.css')}">
        <link rel="stylesheet" href="${resource(dir:'css',file:'simmain.css')}">
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
		<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js" type="text/javascript"></script>
		<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.14/themes/base/jquery-ui.css" type="text/css" media="all" />
		<link rel="stylesheet" href="http://static.jquery.com/ui/css/demo-docs-theme/ui.theme.css" type="text/css" media="all" />
		<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.14/jquery-ui.min.js" type="text/javascript"></script>
        <g:layoutHead />
    </head>

    <body>
      <div id="simheader">
        <div class="graypanel" style="width:100%">
          <div style="float:right;">Welcome back ${user.username}</div>
          <div>Semantic Information Portal</div>
        </div>
      </div>

      <div id="simmainlayout">

        <div id="simnav" class="graypanel">

	<ul>
	  <g:each in="${workspace.availableRepositories}" var="repo">
	  <li>
	  	<h3>${repo.name}</h3>
	  	<ul>
	  		<li>
		    <g:each in="${repo.contexts}" var="c">
		      <h4>${c.name}</h4>
		      <ul class="plainlist">
		        <g:each in="${c.searchtemplates}" var="sst">
		          <li><g:link controller="search" action="showtemplate" params="${['repo':repo.id,'template':sst.id]}">${sst.name}</g:link></li>
		        </g:each>
		        <g:each in="${c.edittemplates}" var="set">
		          <li><g:link action="create" controller="resource" params="${['repo':repo.id,'template':set.id]}">New Record(${set.name})</g:link></li>
		        </g:each>
		      </ul>
		    </g:each>
		   	</li>
		</ul>
	  </g:each>

        </div>

        <div id="simmain">
          <g:layoutBody />
        </div>
      </div>

      <div id="simfooter">
      </div>
		
		<script>

		$(document).ready(function()
		{	
			var CONTEXT_PATH = '<%= request.getContextPath()%>';
			var url = $(location).attr('href');

			//process url
			url = url.substring(url.indexOf(CONTEXT_PATH));

			//toggle visibility of location on the nav bar
			$('a[href="' + url +'"]').parent().parent().prev().addClass('active').next().toggle() //toggle 
									 .parent().parent().prev().addClass('active').next().toggle(); //toggle
			
						
			$('ul h4, ul h3').click(function()
			{
				//$('h4.active').removeClass('active').next().toggle();
				$(this).next().toggle();	
				$(this).toggleClass('active');
			});					
		});
		
		</script>
    </body>
</html>
