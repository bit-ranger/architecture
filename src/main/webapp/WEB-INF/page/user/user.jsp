<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="../global/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en-AU">
<head>
    <title>User Info</title>
    <script type="text/javascript">
        function del(id,dom){
            $.ajax({
                type : "DELETE",
                url : "${selfHref}/" + id,
                success : function(json){
                    if(json == "success"){
                        $(dom).remove();
                    }
                }
            });
        }

        function show(id){
            $.ajax({
                type : "GET",
                url : "${jsonURL}/" + id,
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
            <table>
                <s:form action="${selfURL}/${user.userid}" method="put" enctype="multipart/form-data">
                    <tr>
                        <td>name<input name="name" type="text" value="${user.name}"/></td>
                        <td>password<input name="password" type="text" value="${user.password}"/></td>
                        <td><input type="submit" value="修改"></td>
                        <td><a href="javascript:void(0);" onclick="show('${user.userid}');">查看</a></td>
                        <td><a href="javascript:void(0);" onclick="del('${user.userid}',this.parentNode.parentNode)">删除</a></td>
                    </tr>
                </s:form>
            </table>
        </c:forEach>
    </table>

    多条件查询--GET
    <form  action="${selfURL}"  method="get">
        <table>
            <tr>
                <td>page<input type="text" name="pn"/></td>
                <td>id<input name="userid" type="text" /></td>
                <td>name<input name="name" type="text"/></td>
            </tr>
        </table>
        <input type="submit" value="查询">
    </form>

    添加数据--POST
    <form action="${selfURL}"  method="post">
        <table>
            <tr>
                <td>name<input name="name" type="text"/></td>
                <td>password<input name="password" type="text"/></td>
            </tr>
        </table>
        <input type="submit" value="添加">
    </form>
</body>
</html>
