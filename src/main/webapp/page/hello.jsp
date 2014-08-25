<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>hello</title>
    <script src="${pageContext.request.contextPath}/scripts/jquery.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/scripts/editor/ckeditor.js" type="text/javascript"></script>
    <script type="text/javascript">
        CKEDITOR.replace('editor');
    </script>
    <script type="text/javascript">
        function req(id){
            $.ajax({
                type : "POST",
                url : "${pageContext.request.contextPath}/home/getJson.sx?id="+id,
                success : function(json){
                    alert(json.name);
                }
            });
        }
    </script>
</head>
<body>
    <table>
        <c:forEach items="${users}" var="user">
            <tr>
                <td><a href="javascript:void(0);" onclick="req('${user.id}');">${user.id}</a></td>
                <td>${user.name}</td>
                <td>${user.role}</td>
                <td>${user.power}</td>
            </tr>
        </c:forEach>
    </table>
    ${user.name}

    <textarea id="editor" class="ckeditor"></textarea>
</body>
</html>
