<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
    
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
							
                    <form:form modelAttribute="computerDTO" action="add" method="POST" >
                        <fieldset>                       	                      	
                        
                            <div class="form-group">
                                <label for="name">Computer name</label>
                                <form:input path="name" type="text" class="form-control" id="name" name="name" placeholder="Computer name" required="required" />
                            </div>
                            <div class="form-group">
                                <label for="introduced">Introduced date</label>
                                <form:input path="introduced" type="date" class="form-control" id="introduced" name="introduced" placeholder="Introduced date" />
                            </div>
                            <div class="form-group">
                                <label for="discontinued">Discontinued date</label>
                                <form:input path="discontinued" type="date" class="form-control" id="discontinued" name="discontinued" placeholder="Discontinued date" />
                                
                                <div id="errorDiscontinued" class="" >
                                </div>
                            	
                            </div>
                            <div class="form-group">
                                <label for="companyId">Company</label>
                                <form:select path="company" class="form-control" id="company" name="company">
                                	<option value="0">--</option>
                                	<c:forEach var="company" items="${ companyList }">
                                		<option value="${ company.id }">
                                			<c:out value="${ company.name }" />
                                		</option>
                                	</c:forEach>
                                </form:select>
                            </div>                  
                        </fieldset>
                        
                        <div class="alert alert-danger page-alert" id="alert-message" style="display: none;">
                        	<button type="button" class="close" data-dismiss="alert" aria-label="Close">
								<span aria-hidden="true">×</span>
							</button>
                        	<strong>Action add Canceled ! </strong>
                        	<label id = erroradd ></label>
                        </div>
                        
                        <div class="actions pull-right">
                            <input type="submit" value="Add" class="btn btn-primary">
                            or
                            <c:url value ="/app" var = "lienDashboard" />
                            <a href="${ lienDashboard }" class="btn btn-default">Cancel</a>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </section>
	<script src="../../cdb/static/js/jquery.min.js"></script>
	<script src="../../cdb/static/js/bootstrap.min.js"></script>
	<script src="../../cdb/static/js/addComputer.js"></script>
</body>
</html>