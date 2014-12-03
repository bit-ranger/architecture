<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="../global/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en-AU">
<head>
    <title>hello</title>
    <script src="${scripts}/loginCheck.js" type="text/javascript"></script>
    <script src="${scripts}/editor/ckeditor.js" type="text/javascript"></script>
    <script type="text/javascript">
        window.onload = function()
        {
            CKEDITOR.replace('editor');
            LOGINCHECK_URL = "${root}/";
        };
    </script>
</head>
<body>
    <a href="${root}">首页</a>
    <s:form action="${releaseURL}" method="post" id="logincheck">
        <select name="classid">
            <c:forEach items="${articleclassList}" var="articleclass">
                <option value="${articleclass.classid}">${articleclass.name}</option>
            </c:forEach>
        </select>
        <input type="text" name="title"/>
        <textarea id="editor" name="content"></textarea>
        <input type="submit"/>
    </s:form>

    <input type="button" onclick="alert(LOGINCHECK);"/>
</body>
</html>
