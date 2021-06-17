<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Test</title>
	</head>
	<body>
		<p>Message dynamique :</p>
			<p>
				${ test }
			</p>
        <p>Récupération du bean :</p>
       	<p>
			${ bean.prenom }
			${ bean.nom }
    	</p>
	    <c:out value="test" />
        
	</body>
</html>