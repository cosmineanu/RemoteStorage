<%@page import="com.tedrasoft.remotestorage.common.*" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create folder form</title>
    </head>
    <body>
        <h1>Create folder form</h1>
        <hr/>
        
             <form id="folderForm" method="POST" enctype="application/x-www-form-urlencoded" action="<c:url value="/newFolder"/>">
                <br/>
               	 <input type="text" name="name"><br>&nbsp;
		         <input type="submit" value="Create"/>
                <br/>
            </form>
            		<br/>
			<a href="<c:url value="/client"/>"><button>To File List</button></a>
     </body>
</html>