
function makeSearchForm(access_points_list_element, 
                       results_div_element, 
                       base_template_uri, 
                       target_repository_id, root_types) {
  var template=loadSearchTemplateFrom(base_template_uri);
  alert("template: "+template);
}

function loadSearchTemplateFrom(template_uri) {
  console.log("Loading search template "+template_uri);
  var template
  $.ajax({
    async:false,
    url: template_uri,
    success: function(data){
      template=data;
    }
  });
  return template;
}

