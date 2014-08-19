<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<tags:userarea-template>
<jsp:attribute name="active">Batch Download</jsp:attribute>
<jsp:body>
<div class="container">
	<div class="jumbotron"
		style="padding-left: 30px; padding-right: 30px;">				
	 
		<h2>Image Batch Download</h2>
		<p>
			Enter information in the fields to refine your snapshots. All fields are optional.
		</p>
		<span class="help-block">
			If you are downloading to a server with wget, use --output-document FILENAME.zip to save it to give it a reasonable name. Results are 
			in an uncompressed ZIP archive.
		</span>
		
		<form role="form" id ="query-builder" class="select-data" method="GET" action="<c:url context="/phenofront" value="/massdownload" />" >
			<div class="form-group">
				
				<input type="hidden" value="${experiment}" name="experiment"/>
	 			
	 			
				<label for="barcode">
					Barcode Regex Search (DB.+, D[A]{3,}, .+AA, etc.)
				</label>
				<input type="text" name="barcode" id="barcode"
					class="form-control" placeholder="DBAA"
					title="Find Snapshots where the barcode matches the input string, or Regex. Supports POSIX Extended Regex." />
				
				
				<label for="measurementLabel">
					Measurement Label Regex Search
				</label>
				<input type="text" name="measurementLabel" id="measurementLabel"
					class="form-control" placeholder=""
					title="Find Snapshots where the measurement(experiment) label matches the input string. Supports POSIX Extended Regex." />
				
				
				<label for="startTime">
					Snapshots Start Time:
				</label>	
				<input type="text" name="startTime" id="startTime"
					class="form-control" placeholder=""
					title="The earliest a snapshot can occur. Click the box to choose a date." />
				
				  
				<label for="endTime">
					Snapshots End Time:
				</label>
				<input type="text" name="endTime" id="endTime"
					class="form-control" placeholder=""
					title="The latest a snapshot can occur. Click the box to choose a date." />
			</div>
			
			<div class="form-group">
				<label>Image Download Options</label>
			
				<div class="checkbox">
					<label>
						<input type="checkbox" name="includeVisible" value="true" checked />
						Include Visible Light Images
					</label>
				</div>
				
				<div class="checkbox">
					<label>
						<input type="checkbox" name="includeFluorescent" value="true" checked />
						Include Fluorescent Images
					</label>
				</div>
				
				<div class="checkbox">
					<label>
						<input type="checkbox" name="includeInfrared" value="true" checked />
						Include Near Infrared Images
					</label>
				</div>
				
				<div class="checkbox">
					<label>
						<input type="checkbox" name="includeWatering" value="true" checked />
						Include Watering Snapshots
					</label>
				</div>
				
				<input type="hidden" name="downloadKey" value="${ downloadKey }">
			</div>
			
			<div class="downloadLink hidden"></div>
			<a id ="generateDownloadLink" class="btn btn-default btn-block btn-large">
				Generate Download Link
			</a>
			
			<br />
			
<!--
			<label for="newTag">
					Add/Remove Tag:
			</label>
			<input type="text" name="newTag" id="newTag"
				class="form-control" placeholder="my_new_tag1"
				title="A new tag to add or remove from the database. Tags can only be composed of underscores or alphanumeric characters." />
			
			<div class="checkbox">
				<label>
					<input type="radio" name="addTag" value="true" checked />
					Add tag
				</label>
			</div>
			<div class="checkbox">
				<label>
					<input type="radio" name="addTag" value="false" />
					Remove tag
				</label>
			</div>
			
			<a id="changeTag" class="btn btn-default btn-block btn-large">
				Change Tag
			</a>
			
			<br />
-->
			
			<a id="previewQuery" class="btn btn-default btn-block btn-large">
				Preview Query
			</a>
			
			<br />
			
			<div id="queryPreview" class ="hidden"></div>
			
		</form>
	</div>
</div>
</jsp:body>
</tags:userarea-template>
</body>

<script type="text/javascript" src="<c:url value="/resources/js/queryElement.js"/>"></script>

<script>
$(document).ready(function(){
	
    $("#barcode").tooltip();
    $("#measurementLabel").tooltip();
    $("#startTime").tooltip();
	$("#endTime").tooltip();
	
	$("#startTime").datetimepicker();
	$("#endTime").datetimepicker();
	
    // Generate download link
    $("#generateDownloadLink").click(function() {
        $(".downloadLink").removeClass("hidden").show().addClass("alert alert-success");
        var downloadURL = '<c:url context="/phenofront" value="/massdownload" />' + "?" + $("#query-builder").serialize();
        $(".downloadLink").html("<a href='"+ downloadURL + "'>Your Download Link </a>This link may only be used once.");
    });
    
    // Helper methods for the preview operations
 	function displayQuery(query, snapshots) {
 		var query = queryElement(query, snapshots);
 		$("#queryPreview").empty();
 		$("#queryPreview").show();
 		
 		$("#queryPreview").removeClass("hidden alert alert-danger");
 		
 		$("#queryPreview").append(query);
 	}
    
 	// Query preview
    $("#previewQuery").click(function() {
    	console.log('<c:url context="/phenofront" value="/userarea/querypreview" />');
    	var form = $(this);
		$.ajax({
			type: "POST",
			url: '<c:url context="/phenofront" value="/userarea/querypreview" />',
			data: $("#query-builder").serialize(),
			success: function(queryJSON) {
				displayQuery(queryJSON["query"], queryJSON["snapshots"]);
			},
			error: function(xhr, status, error) {
				$("#queryPreview").empty();
				$("#queryPreview").show();
				
				$("#queryPreview").removeClass("hidden");
				$("#queryPreview").addClass("alert alert-danger");
				
				$("#queryPreview").text(xhr.responseText);
			}
		});
    });
});
</script>
</html>
