<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="fl.node.server.*,java.io.*,java.util.*"%>
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Main page</title>
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
</head>
<body>


<h1>Distributed learning network</h1>

<br><br>

<div class="w3-cell-row" style="width:50%">
	<div class="w3-container w3-cell w3-border w3-round-xlarge" style="width:50%">   
	<br><a href="manager.jsp">Manager</a> 
	</div>
	<div class="w3-container w3-cell w3-border w3-round-xlarge" style="width:50%">
	<br> 
	</div>
</div>
<div class="w3-cell-row" style="width:50%">
	<div class="w3-container w3-cell w3-border w3-round-xlarge" style="width:50%">   
	<br><a href="install_files.html">CAT install files</a> 
	</div>
	<div class="w3-container w3-cell w3-border w3-round-xlarge" style="width:50%">
	<br> 
	</div>
</div>	

	<br>
	
	<div class="w3-cell-row" style="width:50%">
	
	<div class="w3-container w3-cell w3-border w3-round-xlarge" style="width:50%">   
	<br>
	
	</div>
	<div class="w3-container w3-cell w3-border w3-round-xlarge" style="width:50%"> <br> 
	<%
            // Set refresh, autoload time as 5 seconds
            response.setIntHeader("Refresh", 5);
            // Get current time
            Calendar calendar = new GregorianCalendar();
            String am_pm;
            
            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            
            if(calendar.get(Calendar.AM_PM) == 0)
               am_pm = "AM";
            else
               am_pm = "PM";
            String CT = hour+":"+ minute +":"+ second +" "+ am_pm;
            out.println("Current Time: " + CT + "\n");
         %>
	</div>
	</div>
	
		

<br><br>

</body>
</html>