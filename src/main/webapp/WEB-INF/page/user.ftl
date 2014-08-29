<html>
<head>
    <title>hello</title>
    <script src="${root}/scripts/jquery.min.js" type="text/javascript"></script>
    <script src="${root}/scripts/editor/ckeditor.js" type="text/javascript"></script>
    <script type="text/javascript">
        CKEDITOR.replace('editor');
    </script>
    <script type="text/javascript">
        function req(id){
            $.ajax({
                type : "POST",
                url : "${base}/user/json?id="+id,
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
        [#list users as user]
            <tr>
                <td><a href="javascript:void(0);" onclick="req('${user.id}');">${user.id}</a></td>
                <td>${user.name}</td>
                <td>${user.role}</td>
                <td>${user.power}</td>
                <td><a href="${base}/user/delete?id=${user.id}">删除</a></td>
            </tr>
       [/#list]
    </table>
    ${user.name}

    <textarea id="editor" class="ckeditor"></textarea>
</body>
</html>
