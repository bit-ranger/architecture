var LOGINCHECK = {
    ID : "loginCheck",
    CHECK_URI : "loginCheck",
    replaceID:function(id){
        LOGINCHECK.ID = id;
    },
    replaceCHECK_URI:function(uri){
        LOGINCHECK.CHECK_URI = uri;
    },
    getROOT:function(action){
        if(action == null || action == undefined || action.length == 0){
            throw "action is '" + action + "'";
        }
        var root = action.match(/^\w*:\/{2}[^/]*\//);
        return root;
    },
    check:function(form){
        var isLogin = false;
        var check_url = LOGINCHECK.getROOT(form.attr("action")) + LOGINCHECK.CHECK_URI;
        $.ajax({
            url : check_url,
            type : "get",
            crossDomain : true,
            cache : false,
            async : false,
            timeout : 2000,
            success : function(message){
                isLogin = message;
            },
            error : function(XMLHttpRequest, textStatus, errorThrown){
                throw errorThrown;
            }
        });
        return isLogin;
    }
};
$(document).ready(
    function(){
        var form = $("#"+LOGINCHECK.ID);
        form.submit(function(){
            try{
                if(!LOGINCHECK.check(form)){
                    alert("请登录后重试!");
                    return false;
                }
            }catch(e){
                alert(e);
                return false;
            }
        });
    }
);


