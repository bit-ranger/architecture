<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="../global/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en-AU">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
    <link href="${ctx}/resources/styles/home.css" rel="stylesheet" type="text/css">
    <script src="${ctx}/resources/scripts/loginCheck.js" type="text/javascript"></script>
    <title>Editor</title>
</head>
<body>
    <nav class="navbar navbar-default navbar-fixed-top" style="opacity: .9" role="navigation">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <a class="navbar-brand" href="${ctx}/">Index</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav navbar-right">
                    <li class="active"><a href="${ctx}/">Login</a></li>
                </ul>
            </div><!-- /.navbar-collapse -->
        </div><!-- /.container-fluid -->
    </nav>

    <div class="row center" style="padding-top: 100px;">
        <div class="col-lg-12">
            <sf:form action="${ctx}/blog/release" method="post" id="loginCheck">
                <input type="text" name="title" class="elongated" title="title"/>
                <textarea id="editor" name="content" rows="10" cols="6" title="content">content</textarea>
                <select name="classid" title="class">
                    <c:forEach items="${requestScope.articleclassList}" var="articleclass">
                        <option value="${articleclass.classid}">${articleclass.name}</option>
                    </c:forEach>
                </select>
                <input type="submit"/>
            </sf:form>
        </div>
    </div>

    <s:hasBindErrors name="article">
        <c:if test="${errors.fieldErrorCount > 0}">
            字段错误：<br/>
            <c:forEach items="${errors.fieldErrors}" var="error">
                <s:message var="message" code="${error.code}" arguments="${error.arguments}" text="${error.defaultMessage}"/>
                ${error.field}------${message}<br/>
            </c:forEach>
        </c:if>

        <c:if test="${errors.globalErrorCount > 0}">
            全局错误：<br/>
            <c:forEach items="${errors.globalErrors}" var="error">
                <s:message var="message" code="${error.code}" arguments="${error.arguments}" text="${error.defaultMessage}"/>
                <c:if test="${not empty message}">
                    ${message}<br/>
                </c:if>
            </c:forEach>
        </c:if>
    </s:hasBindErrors>


</body>
</html>
