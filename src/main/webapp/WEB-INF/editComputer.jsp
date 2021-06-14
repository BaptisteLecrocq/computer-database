<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<head>
<title>Edit Computer</title>
<meta charset="UFT-8" name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="../../cdb/static/css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="../../cdb/static/css/font-awesome.css" rel="stylesheet" media="screen">
<link href="../../cdb/static/css/main.css" rel="stylesheet" media="screen">
</head>
<body>
    <header class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <a class="navbar-brand" href="dashboard.html"> Application - Computer Database </a>
             <ul>
            	<c:url value="/edit" var="lienFR" >
            		<c:param name="lang" value="fr" />
            	</c:url>
            	<c:url value="/edit" var="lienEN" >
            		<c:param name="lang" value="en" />
            	</c:url>
            	<li><a href="${ lienFR }">FR</a></li>
		        <li><a href="${ lienEN }">EN</a></li>            
            </ul>
        </div>
    </header>
    <section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <div class="label label-default pull-right">
                       id : <c:out value="${ idComputer }"/>
                    </div>
                    <h1><fmt:message key="label.editComputer" /></h1>
                    
                     <c:if test="${ errors.size() > 0 }" >
                      		<div class="form-group alert alert-danger">
					    <section id="error">
					    	<ul>
					    		<c:forEach var="error" items="${ errors }" >
					    			<li>
					    				<fmt:message key="${ error }" />
					    			</li>
					    		</c:forEach>
					    	</ul>
					    </section>
					    </div>
					</c:if>  
							

                    <form:form modelAttribute="computerDTO" action="edit" method="POST">
                        <form:input path="id" type="hidden" value="${ idComputer }" id="id" name="id"/> 
                         <fieldset>                       	                      	
                        
                            <div class="form-group">
                          		<fmt:message key="column.name" var="nameTranslation" />
                                <label for="name"><fmt:message key="column.name" /></label>
                                <form:input path="name" type="text" class="form-control" id="name" name="name" placeholder="${ nameTranslation }" required="required" />
                            </div>
                            <div class="form-group">
                                <label for="introduced"><fmt:message key="column.introduced" /></label>
                                <form:input path="introduced" type="date" class="form-control" id="introduced" name="introduced" placeholder="Introduced date" />
                            </div>
                            <div class="form-group">
                                <label for="discontinued"><fmt:message key="column.discontinued" /></label>
                                <form:input path="discontinued" type="date" class="form-control" id="discontinued" name="discontinued" placeholder="Discontinued date" />
                                
                                <div id="errorDiscontinued" class="" >
                                </div>
                            	
                            </div>
                            <div class="form-group">
                                <label for="companyId"><fmt:message key="column.company" /></label>
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
                        	<strong>
                        		<fmt:message key="label.errorEdit" />
                        	</strong>
                        	<label id = erroradd ></label>
                        </div>
                        
                        <div class="actions pull-right">
                            <input type="submit" value='<fmt:message key="button.edit" />' class="btn btn-primary">
                            <fmt:message key="other.or" />
                            <c:url value ="/app" var = "lienDashboard" />
                            <a href="${ lienDashboard }" class="btn btn-default">
                            	<fmt:message key="button.cancel" />
                            </a>
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