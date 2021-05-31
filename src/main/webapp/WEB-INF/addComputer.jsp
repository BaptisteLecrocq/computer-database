<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="../../cdb/static/css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="../../cdb/static/css/font-awesome.css" rel="stylesheet" media="screen">
<link href="../../cdb/static/css/main.css" rel="stylesheet" media="screen">
</head>
<body>
    <header class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <a class="navbar-brand" href="dashboard.html"> Application - Computer Database </a>
        </div>
    </header>

    <section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <h1>Add Computer</h1>
                    <form action="add" method="POST">
                        <fieldset>
                        	
                        	<c:if test="${ errors.size() > 0 }" >
                        		<div class="form-group alert alert-danger">
							    <section id="error">
							    	<ul>
							    		<c:forEach var="error" items="${ errors }" >
							    			<li>
							    				<c:out value="${ error }" />
							    			</li>
							    		</c:forEach>
							    	</ul>
							    </section>
							    </div>
							</c:if>
                        	
                        
                            <div class="form-group">
                                <label for="computerName">Computer name</label>
                                <input type="text" class="form-control" id="computerName" name="computerName" placeholder="Computer name">
                            </div>
                            <div class="form-group">
                                <label for="introduced">Introduced date</label>
                                <input type="date" class="form-control" id="introduced" name="introduced" placeholder="Introduced date">
                            </div>
                            <div class="form-group">
                                <label for="discontinued">Discontinued date</label>
                                <input type="date" class="form-control" id="discontinued" name="discontinued" placeholder="Discontinued date">
                            </div>
                            <div class="form-group">
                                <label for="companyId">Company</label>
                                <select class="form-control" id="companyId" name="companyId">
                                	<option value="0">--</option>
                                	<c:forEach var="company" items="${ companyList }">
                                		<option value="${ company.id }">
                                			<c:out value="${ company.name }" />
                                		</option>
                                	</c:forEach>
                                </select>
                            </div>                  
                        </fieldset>
                        
                        <div class="alert alert-danger page-alert" id="alert-message" style="display: none;">
                        	<strong>Action add Canceled ! </strong>
                        	<label id = erroradd ></label>
                        </div>
                        
                        <div class="actions pull-right">
                            <input type="submit" value="Add" class="btn btn-primary">
                            or
                            <c:url value ="/app" var = "lienDashboard" />
                            <a href="${ lienDashboard }" class="btn btn-default">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
	<script src="../../cdb/static/js/jquery.min.js"></script>
	<script src="../../cdb/static/js/addComputer.js"></script>
</body>
</html>