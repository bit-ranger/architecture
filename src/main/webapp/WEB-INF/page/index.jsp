<%@ page import="java.util.Enumeration" %>
<%@page contentType="text/html; charset=UTF-8" %>
<%@include file="overall/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en-AU">
<head>
    <title>index</title>
</head>
<body>
    多条件查询--GET
    <form  action="${root}/user"  method="get">
        <table>
            <tr>
                <td>page<input type="text" name="pn"/></td>
            </tr>
            <tr>
                <td>id<input name="v_id" type="text" /></td>
            </tr>
            <tr>
                <td>name<input name="v_name" type="text"/></td>
            </tr>
            <tr>
                <td>role<input name="v_role" type="text"/></td>
            </tr>
            <tr>
                <td>power<input name="v_power" type="text"/></td>
            </tr>
        </table>
        <input type="submit" value="查询">
    </form>
    <br/>

    添加数据--POST
    <form action="${root}/user"  method="post">
        <table>
            <tr>
                <td>name<input name="name" type="text"/></td>
            </tr>
            <tr>
                <td>role<input name="role" type="text"/></td>
            </tr>
            <tr>
                <td>power<input name="power" type="text"/></td>
            </tr>
        </table>
        <input type="submit" value="添加">
    </form>
    <br/>

    <form action="${root}/user/file" method="post" enctype="multipart/form-data">
        <input type="file" name="uploadFile" />
        <input type="submit" value="上传" />
    </form>
    <br/>


测试session共享----
    Server Info:
    <%
        out.println(request.getLocalAddr() + " : " + request.getLocalPort()+"<br>");
    %>
    <%
        out.println("<br> ID " + session.getId()+"<br>");

        out.print("<b>Session 列表</b>");

        Enumeration e = session.getAttributeNames();

        while (e.hasMoreElements()) {
            String name = (String)e.nextElement();
            String value = session.getAttribute(name).toString();
            out.println( name + " = " + value+"<br>");
            System.out.println( name + " = " + value);
        }
    %>
    <form action="${root}/user" method="get">
        名称:<input type="text" size="20" name="dataName">
        <br>
        值:<input type="text" size="20" name="dataValue">
        <br>
        <input type="submit">
    </form>
</body>
</html>
