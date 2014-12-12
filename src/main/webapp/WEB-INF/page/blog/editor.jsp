<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="../global/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en-AU">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
    <link href="${styles}/home.css" rel="stylesheet" type="text/css">
    <title>Editor</title>
    <script src="${scripts}/loginCheck.js" type="text/javascript"></script>
    <script src="${scripts}/editor/ckeditor.js" type="text/javascript"></script>
    <script type="text/javascript">
        window.onload = function()
        {
            CKEDITOR.replace('editor');
        };
    </script>
</head>
<body>
    <nav class="navbar navbar-default navbar-fixed-top" style="opacity: .9" role="navigation">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <a class="navbar-brand" href="${root}">Index</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav navbar-right">
                    <li class="active"><a href="${loginURL}" target="_blank">Login</a></li>
                </ul>
            </div><!-- /.navbar-collapse -->
        </div><!-- /.container-fluid -->
    </nav>

    <div class="row center" style="padding-top: 100px;">
        <div class="col-lg-12">
            <s:form action="${releaseURL}" method="post" id="loginCheck">
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

</body>
</html>
