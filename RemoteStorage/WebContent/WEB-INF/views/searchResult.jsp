
<%@page import="com.tedrasoft.remotestorage.common.*" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search Result</title>
    </head>
    <body>
        <h1>Search Result</h1>
        <hr/>
        <c:if test="${error}">
            <h1><c:out value="${errorMessage}"/></h1>
        </c:if>

        <c:if test="${!error}">
                <c:if test="${searchList!=null}">
                <br/>
                <hr>
                
                <table border="1" cellpadding="5" cellspacing="5" width="100%">
                    <tr>
                        <th>File Name</th>
                        
                    </tr>
                    <c:forEach var="file" items="${searchList.fullPathList}">
                        <tr>
                            <td><c:out value="${file}"></c:out></td>
                            
                        </tr>
                    </c:forEach>
                </table>
            </c:if>
            <br/>
            <br/>
		
        </c:if>
        		<br/>
		<a href="<c:url value="/client"/>"><button>To File List</button></a>
    </body>
</html>
