
<%@page import="com.tedrasoft.remotestorage.common.*" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Remote storage file list</title>
    </head>
    <body>
        <h1>Remote storage file list</h1>
        <hr/>
        <c:if test="${error}">
            <h1><c:out value="${errorMessage}"/></h1>
        </c:if>

        <c:if test="${!error}">
             <form id="searchForm" method="get" action="<c:url value="/search"/>">
                <br/>
                 <input type="text" value="" name="searchTerm"/>&nbsp;
		         <input type="submit" value="Search"/>
                <br/>
            </form>
            
            
             <br/>
             Current path: <c:out value="${fileList.relativePath}"/> &nbsp;&nbsp; <a href="<c:url value="/client?up=true"/>"><button>Up</button></a>
             <a href="<c:url value="/prepareFolderForm"/>"><button>New Folder</button></a>    
             <a href="<c:url value="/prepareFileForm"/>"><button>Upload File</button></a>        
            <c:if test="${fileList!=null}">
                <br/>
                <hr>
                <h2>Files</h2>
                <table border="1" cellpadding="5" cellspacing="5" width="100%">
                    <tr>
                        <th>File name</th>
                        <th>Delete</th>
                        <th>Action</th>
                       
                    </tr>
                    <c:forEach var="file" items="${fileList.fileList}">
                        <tr>
                            <td><c:out value="${file}"/></td>
                            <td><a href="<c:url value="/delete?name=${file}"/>"><button>Delete</button></a></td>
                            <td>
                            	<c:if test="${fn:contains(file, '.')}">
                        			<a href="<c:url value="/download?name=${file}"/>"><button>Download</button></a>
                    			</c:if>
                            	<c:if test="${!fn:contains(file, '.')}">
                        			<a href="<c:url value="/client?nextFolder=${file}"/>"><button>Navigate</button></a>
                    			</c:if>
                            </td>
                           
                        </tr>
                    </c:forEach>
                </table>
            </c:if>
            <br/>
            <br/>

        </c:if>
    </body>
</html>
