<%@page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en-AU">
<html>
<head>
    <title>登陆</title>
</head>
<body>
    <form action="${landingURL}" method="post">
        <input type="hidden" name="redirectURL" value="${redirectURL}"/>
        <input type="text" name="name"/>
        <input type="password" name="password"/>
        <input type="submit" value="登陆"/>
    </form>
</body>
</html>
