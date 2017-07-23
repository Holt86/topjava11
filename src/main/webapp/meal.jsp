<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Meal</title>
</head>
<body>
<jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>

<h2 align="center">${meal.id == 0 ? "Create" : "Update"} meal</h2>
<br>
<br>
<form action="${pageContext.request.contextPath}/meals" method="post">

    <input type="hidden" name="id" value="${meal.id}">


    <table align="center">
        <tr>
            <td>DateTime</td>
            <td><input type="datetime-local" name="dateTime" value="${meal.dateTime}"></td>
        </tr>
        <tr>
            <td>Description</td>
            <td><input type="text" name="description" value="${meal.description}"></td>
        </tr>
        <tr>
            <td>Calories</td>
            <td><input type="text" name="calories" value="${meal.calories}"></td>
        </tr>
        <tr>
            <td><input type="submit" name="submit" value="Submit"></td>
            <td></td>
        </tr>
    </table>
</form>

</body>
</html>
