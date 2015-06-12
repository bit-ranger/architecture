<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="../global/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en-AU">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
    <link href="${pageContext.request.contextPath}/resources/styles/home.css" rel="stylesheet" type="text/css">
    <script src="${pageContext.request.contextPath}/resources/scripts/loginCheck.js" type="text/javascript"></script>
    <title>Editor</title>
</head>
<body>
    <nav class="navbar navbar-default navbar-fixed-top" style="opacity: .9" role="navigation">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/">Index</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav navbar-right">
                    <li class="active"><a href="${pageContext.request.contextPath}/" target="_blank">Login</a></li>
                </ul>
            </div><!-- /.navbar-collapse -->
        </div><!-- /.container-fluid -->
    </nav>

    <div class="row center" style="padding-top: 100px;">
        <div class="col-lg-12">
            <s:form action="${pageContext.request.contextPath}/blog/release" method="post" id="loginCheck">
                <input type="text" name="title" class="elongated"/>
                <textarea id="editor" name="content"></textarea>
                <select name="classid">
                    <c:forEach items="${articleclassList}" var="articleclass">
                        <option value="${articleclass.classid}">${articleclass.name}</option>
                    </c:forEach>
                </select>
                <input type="submit"/>
            </s:form>
        </div>
    </div>

    <spring:hasBindErrors name="article">
        <c:if test="${errors.fieldErrorCount > 0}">
            字段错误：<br/>
            <c:forEach items="${errors.fieldErrors}" var="error">
                <spring:message var="message" code="${error.code}" arguments="${error.arguments}" text="${error.defaultMessage}"/>
                ${error.field}------${message}<br/>
            </c:forEach>
        </c:if>

        <c:if test="${errors.globalErrorCount > 0}">
            全局错误：<br/>
            <c:forEach items="${errors.globalErrors}" var="error">
                <spring:message var="message" code="${error.code}" arguments="${error.arguments}" text="${error.defaultMessage}"/>
                <c:if test="${not empty message}">
                    ${message}<br/>
                </c:if>
            </c:forEach>
        </c:if>
    </spring:hasBindErrors>


</body>
</html>
