<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>index</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/user"  method="post">
        <table>
            <tr>
                <td>page<input type="text" name="pn"/></td>
            </tr>
            <tr>
                <td>id<input type="text" name="s_id"/></td>
            </tr>
            <tr>
                <td>name<input type="text" name="s_name"/></td>
            </tr>
            <tr>
                <td>role<input type="text" name="s_role"/></td>
            </tr>
            <tr>
                <td>power<input type="text" name="s_power"/></td>
            </tr>
        </table>
        <input type="submit" value="查询">
    </form>
    <form action="${pageContext.request.contextPath}/user/get"  method="post">
        <table>
            <tr>
                <td>id<input type="text" name="id"/></td>
            </tr>
        </table>
        <input type="submit" value="获取">
    </form>
    <form action="${pageContext.request.contextPath}/user/add"  method="post" >
        <table>
            <tr>
                <td>name<input type="text" name="name"/></td>
            </tr>
            <tr>
                <td>role<input type="text" name="role"/></td>
            </tr>
            <tr>
                <td>power<input type="text" name="power"/></td>
            </tr>
        </table>
        <input type="submit" value="添加">
    </form>
    <form action="${pageContext.request.contextPath}/user/edit"  method="post" >
        <table>
            <tr>
                <td>id<input type="text" name="id"/></td>
            </tr>
            <tr>
                <td>name<input type="text" name="name"/></td>
            </tr>
            <tr>
                <td>role<input type="text" name="role"/></td>
            </tr>
            <tr>
                <td>power<input type="text" name="power"/></td>
            </tr>
        </table>
        <input type="submit" value="修改">
    </form>
</body>
</html>
