<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
    <base href="<%=request.getContextPath()%>">
</head>
<body>

<h3 align="center">Meals for User</h3>

<table border="1" align="center">
    <thead>
    <tr>
        <td width="150">DateTime</td>
        <td width="150">Description</td>
        <td width="150">Calories</td>
        <td width="150"></td>
    </tr>
    </thead>
    <c:forEach items="${meals}" var="meal">
        <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.model.MealWithExceed"/>
        <tr style="color: ${meal.exceed ? 'red' : 'blue'}">
            <td>${meal.dateTime.toString().replace("T", " ")}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="${pageContext.request.contextPath}/meals?action=delete&id=${meal.id}">Delete</a><br>
                <a href="${pageContext.request.contextPath}/meals?action=update&id=${meal.id}">Update</a>
            </td>
        </tr>
    </c:forEach>
</table>
<h3 align="center"><a href="${pageContext.request.contextPath}/meals?action=create">Create Meal</a></h3>



</body>
</html>
