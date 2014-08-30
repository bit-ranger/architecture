<%@page contentType="text/html; charset=UTF-8" %>
<%@include file="overall/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en-AU">
<head>
    <title>hello</title>
    <script src="${root}/scripts/editor/ckeditor.js" type="text/javascript"></script>
    <script type="text/javascript">
        window.onload = function()
        {
            CKEDITOR.replace('editor');
        };
    </script>
    <script type="text/javascript">
        function req(id){
            $.ajax({
                type : "POST",
                url : "${root}/user/json?id="+id,
                success : function(json){
                    alert(json.name);
                }
            });
        }
    </script>
</head>
<body>

    <a href="${root}">首页</a>
    <table>
        <c:forEach items="${users}" var="user">
            <tr>
                <td><a href="javascript:void(0);" onclick="req('${user.id}');">${user.id}</a></td>
                <td>${user.name}</td>
                <td>${user.role}</td>
                <td>${user.power}</td>
                <td><a href="${base}/user/delete?id=${user.id}">删除</a></td>
            </tr>
        </c:forEach>
    </table>
    ${user.name}

    <textarea id="editor"/>
</body>
</html>
