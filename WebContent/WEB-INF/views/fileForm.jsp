<%@page import="com.tedrasoft.remotestorage.common.*" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>File Upload Form</title>
    </head>
    <body>
        <h1>File Upload Form</h1>
        <hr/>
        
             <form id="searchForm" method="POST" enctype="multipart/form-data" action="<c:url value="/saveFile"/>">
                <br/>
               	 <input type="file" name="file" ><br>&nbsp;
		         <input type="submit" value="Upload"/>
                <br/>
            </form>
            		<br/>
		<a href="<c:url value="/client"/>"><button>To File List</button></a>
            </body>
            </html>