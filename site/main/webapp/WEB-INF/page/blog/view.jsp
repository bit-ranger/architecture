<%--
  Created by IntelliJ IDEA.
  User: sllx
  Date: 14-12-2
  Time: 下午9:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${article.title}</title>
</head>
<body>
    <span>${article.title}</span>
    <span>${article.user.name}</span>
    <span>${article.articleclass.name}</span>
    ${article.content}
</body>
</html>
