<%@page contentType="text/html; charset=UTF-8" %>
<%@include file="overall/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en-AU">
<head>
    <title>hello</title>
    <script src="${root}/resources/scripts/editor/ckeditor.js" type="text/javascript"></script>
    <script type="text/javascript">
        window.onload = function()
        {
            CKEDITOR.replace('editor');
        };
    </script>
    <script type="text/javascript">
        function del(id,dom){
            $.ajax({
                type : "DELETE",
                url : "${resource.getLink('self').href}" + id,
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
                url : "${resource.getLink('self').href}/json/" + id,
                success : function(json){
                    alert(json.power);
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
                <s:form action="${root}/user/${user.id}" method="put">
                    <tr>
                        <td>name<input name="v_name" type="text" value="${user.name}"/></td>
                        <td>role<input name="v_role" type="text" value="${user.role}"/></td>
                        <td><input type="submit" value="修改"></td>
                        <td><a href="javascript:void(0);" onclick="show('${user.id}');">查看power</a></td>
                        <td><a href="javascript:void(0);" onclick="del('${user.id}',this.parentNode.parentNode)">删除</a></td>
                    </tr>
                </s:form>
            </table>
        </c:forEach>
    </table>

    <textarea id="editor"/>
</body>
</html>
