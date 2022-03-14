<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="fl.node.server.jaxws.FLServer,fl.node.server.web.FLWeb,java.io.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>AusCAT: Distributed learning network</title>
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<style>
/* The grid: Three equal columns that floats next to each other */
.column {
    float: left;
    text-align: center;
    font-size: 10px;
    cursor: pointer;
    color: white;
}
.containerTab {
    padding: 20px;
    color: white;
}
/* Clear floats after the columns */
.row:after {
    content: "";
    display: table;
    clear: both;
}
/* Closable button inside the container tab */
.closebtn {
    float: left;
    color: white;
    font-size: 10px;
    cursor: pointer;
}
</style>

</head>

<body>
	
	<script type="text/javascript" src="js/jquery331.js"></script>
	<script type="text/javascript">
	
	var tableState;

	setInterval(function(){ 

		var inputElements = document.getElementsByClassName('activateNode');
	    var clientIndexArray = new Array(inputElements.length);
	    for (var i=0; i < inputElements.length; i++){ clientIndexArray[i]=i.toString(); }
		var clientIndexString = clientIndexArray.join(",");
		
	   
		$.get('FLTableStateServlet', function(responseText) {
			
			var inputElements = document.getElementsByClassName('activateNode');
		    var checkedValue = new Array(inputElements.length);
			for (var i=0; i < inputElements.length; ++i) {
			           checkedValue[i] = inputElements[i].checked;
			           //$('#test'+i).text(checkedValue[i]);          
			}
			var algDisplay = document.getElementsByClassName('containerTab');
			
			var output = responseText;
			
			if (!(JSON.stringify(tableState) === JSON.stringify(output))) {
				
		        if (output.length>2) {
		        	
		        	var networkTable = "<TABLE class=\"w3-table w3-striped w3-bordered\" style=\"width:85%\">";
		        	networkTable+= "<TR><TH style=\"width: 5%\">Selection</TH>";
		        	networkTable+= "<TH style=\"width: 15%\">Data Centre</TH>";
		        	networkTable+= "<TH style=\"width: 20%\">Status</TH>";
		        	networkTable+= "<TH style=\"width: 10%\">Current Job</TH>";
		        	networkTable+= "<TH style=\"width: 50%\">Algorithm Message</TH></TR>";
		        	
			        for (var i=0; i < output.length/3; i++) {
			        	
			        	networkTable+= "<TR><TD><CENTER>";
			        	networkTable+= "<INPUT class=\"activateNode\" TYPE=\"CHECKBOX\" NAME=\"node" + i + "_selected\" onclick='handleActivationClick()'";
			        	if (checkedValue[i]) { 
			        		networkTable+="checked"; 
			        	} 
			        	networkTable+="></CENTER></TD>";
			        	networkTable+="<TD>" + output[3*i] + "</TD>";
			        	networkTable+="<TD>" + output[3*i+1] + "</TD>";
			        	networkTable+="<TD>" + "</TD>";
			        	networkTable+="<TD><div class=\"row\">";	    
						networkTable+="<div class=\"column\" onclick=\"openTab('node" + i + "_tabmessage');\" style=\"background:green;\">----></div>";	    
						networkTable+="</div>";
						networkTable+="<div id=\"node" + i + "_tabmessage\" class=\"containerTab\" style=\"display:";
						if (i>=algDisplay.length) {
							networkTable+= "none";
						} 
						else{ 
							networkTable+= algDisplay[i].style.display; 
						}  
						networkTable+=";background:green\">";
						networkTable+="<span onclick=\"this.parentElement.style.display='none'\" class=\"closebtn\">x</span>";
						networkTable+="<b>" + output[3*i] + " algorithm output</b>";
						networkTable+="<div id=\"node" + i + "_algmessage\">" + output[3*i+2] + "</div></div></TD></TR>";
						
			        }
			        networkTable+="</TABLE>";
			        
		        }        
		        $('#tablePrint').html(networkTable);
		        tableState = output;
		        //$('#json_output').text(responseText);
			}
			
		});
		
		/*
		$.get('FLServletStatus', {clientIndex : clientIndexString}, function(responseText) {
	        var output = responseText.split(",");
	        for (var i=0; i<output.length; i++) {
	        	if (output[i]!=null) {
	        		$('#node'+i+'_status').text(output[i]);
	        	}
	        }
	    });
		
		for (var i=0; i < inputElements.length; i++){ 
			$.get('DLAlgOutputServlet', {clientIndex : clientIndexArray[i]}, function(responseText) {
				$('#node'+responseText[0]+'_algmessage').html(responseText);
				
			});
		}
		*/
	},200);

	
	
	
	
	</script>
	
	<TABLE style="width:100%" cellspacing="10">
	<TR>
	<TD> </TD>
	<TD>
	<p><strong>AusCAT: Distributed Learning Network</strong></p>
	<hr>
	<br>
<!--  
	<
	FLWeb WC = new FLWeb();
	WC.initWebClient();
	FLServer server = WC.getServerObject();
	int num_nodes = server.getNumberofCurrentAddedClients();
	String[] clientNames=new String[num_nodes];
	for (int i=0; i<num_nodes; i++) {
		clientNames[i] = server.getClientName(i);
	}
	>
-->

	<form action="upload" method="post" id="fileuploadform" enctype="multipart/form-data">
    	<input type="file" name="file" id="filefield" />
    	<input type="submit" value="upload" />
    </form>      
    
	<br>
	<input type="button" onclick="handleStartClick()" value="Start algorithm">
	<br>
	<input type="button" onclick="handleRefreshButton()" value="Refresh server">
	<br><hr><br>
	
	<strong>Network</strong>:
	
	<br>
	<div id="tablePrint"> </div>
	<br>
	<br>
	<br>
	<div id="json_output"> </div>
	
</TD>
</TR>
</TABLE>



<script>

function handleActivationClick(){

	var inputElements = document.getElementsByClassName('activateNode');
    var checkedValue = new Array(inputElements.length);
    var checkedValueArray = new Array(inputElements.length);
	for (var i=0; i < inputElements.length; ++i) {
	           checkedValue[i] = inputElements[i].checked; 
	}
	  
	var checkValueString = checkedValue.join();
	
	$.get('FLServletActivation', {clientsActive: checkValueString},
									function(messageActive) {
        //{clientsActive0 : checkedValue[0], clientsActive1 : checkedValue[1], clientsActive2 : checkedValue[2],	clientsActive3 : checkedValue[3]}
        var clientStatus = messageActive.split(",");
        for (var i=0; i<clientStatus.length; i++) {
        	if (clientStatus[i]!=null) {
        		$('#node'+i+'_status').text(clientStatus[i]);
        	}
        }
    });
	
}


function handleStartClick(){
	$.get('FLServletStartAlgorithm', function(messageStart) { 	});
}
function handleRefreshButton(){
	$.get('FLServletRefresh', function(messageStart) { 	});	
}

function openTab(tabName) {
    var i, x;
    x = document.getElementsByClassName("containerTab");
    /*for (i = 0; i < x.length; i++) {
        x[i].style.display = "none";
    }*/
    document.getElementById(tabName).style.display = "block";
}

</script>

<script type="text/javascript">

 
 $("#fileuploadform").on("submit", function(e) {
	    e.preventDefault();
	    var poster = $("#filefield").val();
	    var formData = new FormData($(this)[0]);
	    $.ajax({
	        url: $(this).attr("action"),
	        type: 'POST',
	        data: formData,
	        async: false,
	        beforeSend: function() {
	            $("#message1").show().html("Uploading...");
	        },
	        success: function(data) {
	            $("#message1").html(data).fadeOut(5000);
	            $("#filefield").val("");
	        },
	        cache: false,
	        contentType: false,
	        processData: false
	    });
	    return false;
	});
 
 </script>

	</body>
	
</html>