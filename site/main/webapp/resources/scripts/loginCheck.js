var LOGINCHECK = "logincheck";
var LOGINCHECK_URI = "loginCheck";
$(document).ready(
    function(){
        var form = $("#"+LOGINCHECK);
        form.submit(function(){
            try{
                if(logincheck_func()){
                    form.submit();
                }
            }catch(e){
                alert(e);
                return false;
            }
        });

        var logincheck_func = function(){
            var isLogin = false;
            var url = getURL();
            $.ajax({
                url:url,
                type:"get",
                success:function(message){
                    isLogin = message;
                }
            });
            return isLogin;
        }

        var getURL = function(){
            var action = form.attr("action");
            if(action == null || action == undefined){
                throw "action is" + action;
            }
            var arr = action.split("//");
            var scheme = arr[0];
            var sp = arr[1].split("/")[0];
            return scheme + "//" + sp + "/" + LOGINCHECK_URI;
        }
    }
);


