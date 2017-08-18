<%--
  Created by IntelliJ IDEA.
  User: lizhuohang
  Date: 17/8/18
  Time: 14:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="/security" %>

<c:set var="BASE" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>首页</title>
</head>

<h1>首页</h1>
<security:guest>
    <a href="${BASE}/login">登录</a>
</security:guest>
<security:user>
    <p>${userInfo}</p>
    <a href="<c:url value="/logout"/>">注销</a>
</security:user>
<body>

</body>
</html>
