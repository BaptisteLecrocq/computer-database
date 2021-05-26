<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<!-- Bootstrap -->
	<link href="../../cdb/static/css/bootstrap.min.css" rel="stylesheet" media="screen">
	<link href="../../cdb/static/css/font-awesome.css" rel="stylesheet" media="screen">
	<link href="../../cdb/static/css/main.css" rel="stylesheet" media="screen">
	<title>Insert title here</title>
</head>
<body>

<header class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <a class="navbar-brand" href="#"> Application - Computer Database </a>
        </div>
    </header>

    <section id="main">
        <div class="container">
        
            <h1 id="homeTitle">
                <c:out value="${ count }" /> Computers found
            </h1>       
                 
            <div id="actions" class="form-horizontal">
                <div class="pull-left">
                    <form id="searchForm" action="#" method="GET" class="form-inline">

                        <input type="search" id="searchbox" name="search" class="form-control" placeholder="Search name" />
                        <input type="submit" id="searchsubmit" value="Filter by name"
                        class="btn btn-primary" />
                    </form>
                </div>
                <div class="pull-right">
                    <a class="btn btn-success" id="addComputer" href="addComputer.html">Add Computer</a> 
                    <a class="btn btn-default" id="editComputer" href="#" onclick="$.fn.toggleEditMode();">Edit</a>
                </div>
            </div>
        </div>

        <form id="deleteForm" action="#" method="POST">
            <input type="hidden" name="selection" value="">
        </form>

        <div class="container" style="margin-top: 10px;">
            <table class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <!-- Variable declarations for passing labels as parameters -->
                        <!-- Table header for Computer Name -->

                        <th class="editMode" style="width: 60px; height: 22px;">
                            <input type="checkbox" id="selectall" /> 
                            <span style="vertical-align: top;">
                                 -  <a href="#" id="deleteSelected" onclick="$.fn.deleteSelected();">
                                        <i class="fa fa-trash-o fa-lg"></i>
                                    </a>
                            </span>
                        </th>
                        <th>
                            Computer name
                        </th>
                        <th>
                            Introduced date
                        </th>
                        <!-- Table header for Discontinued Date -->
                        <th>
                            Discontinued date
                        </th>
                        <!-- Table header for Company -->
                        <th>
                            Company
                        </th>

                    </tr>
                </thead>
                <!-- Browse attribute computers -->
                <tbody id="results">
                	<c:forEach var="computer" items="${ page }">
                		<tr>
                			<td class="editMode">
	                            <input type="checkbox" name="cb" class="cb" value="0">
	                        </td>
	                        <td>
	                            <a href="editComputer.html" onclick="">
	                            	<c:out value="${ computer.name }" />
	                            </a>
	                        </td>
	                        <td>
	                        	<c:out value="${ computer.introduced }" default="" />
	                        </td>
	                        <td>
	                        	<c:out value="${ computer.discontinued }" default="" />
	                        </td>
	                        <td>
	                        	<c:out value="${ computer.company }" default="" />
	                        </td>
	                    </tr>
	                </c:forEach>	                        
	                    
                </tbody>
            </table>
        </div>
	      
    </section>

    <footer class="navbar-fixed-bottom">
        <div class="container text-center">
            <ul class="pagination">
                <li>
                	<c:choose>
                		<c:when test="${ pageNumber < 5 }">
                			<c:set var="correctNumber" value="0" />
                		</c:when>
                		<c:otherwise>
                			<c:set var="correctNumber" value="${ pageNumber - 5 }" />
                		</c:otherwise>                	
                	</c:choose>
                	<c:url value="/app" var="lienDashboardPrevious">
                		<c:param name="pageNumber" value="${ correctNumber }" />
                	</c:url>
                    <a href="<c:out value="${ lienDashboardPrevious }" default="#" />" aria-label="Previous">
                      <span aria-hidden="true">&laquo;</span>
                    </a>
              </li>
              
              <c:choose>
              	<c:when test="${ pageNumber < 2 }">
              		<c:set var = "beginPage" value = "${ 4 - pageNumber }" />
              		<c:set var = "endPage" value = "${ 8 - pageNumber }" />
              	</c:when>
              	<c:when test="${ (pageNumber + 2 ) > count/12 }">
              		<c:set var = "beginPage" value = "${ count/12 - pageNumber }" />
              		<c:set var = "endPage" value = "${ count/12 - pageNumber + 4}" />
              	</c:when>
              	<c:otherwise>
              		<c:set var = "beginPage" value = "2" />
              		<c:set var = "endPage" value = "6" />
              	</c:otherwise>
              </c:choose>
              
              <c:forEach var="i" begin="${beginPage}" end="${endPage}" step="1">
              	<li>
              		<c:url value="/app" var="lienDashboard">
              			<c:param name="pageNumber" value="${ pageNumber + i -4}" />
              		</c:url>
              		<a href="<c:out value="${ lienDashboard }" />" >
              			<c:out value="${ pageNumber + i -4}" />
              		</a>
              	</li>
              </c:forEach>
              
              <li>
              	<c:choose>
                		<c:when test="${ (pageNumber + 5) > count/12 }">
                			<c:set var="correctNumber" value="${ pageNumber }" />
                		</c:when>
                		<c:otherwise>
                			<c:set var="correctNumber" value="${ pageNumber + 5 }" />
                		</c:otherwise>                	
                	</c:choose>
                	<c:url value="/app" var="lienDashboardNext">
                		<c:param name="pageNumber" value="${ correctNumber }" />
                	</c:url>
                    <a href="<c:out value="${ lienDashboardNext }" default="#" />" aria-label="Next">
                      <span aria-hidden="true">&raquo;</span>
                    </a>
            </li>
        </ul>
        </div>

        <div class="btn-group btn-group-sm pull-right" role="group" >
            <button type="button" class="btn btn-default">10</button>
            <button type="button" class="btn btn-default">50</button>
            <button type="button" class="btn btn-default">100</button>
        </div>

    </footer>
<script src="../../cdb/static/js/jquery.min.js"></script>
<script src="../../cdb/static/js/bootstrap.min.js"></script>
<script src="../../cdb/static/js/dashboard.js"></script>

</body>
</html>