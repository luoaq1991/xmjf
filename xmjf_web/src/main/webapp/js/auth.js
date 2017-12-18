$(function () {
    $("#identityNext").click(function () {
        //获取输入值
        var realName=$("#realName").val();
        var idCard=$("#card").val();
        var businessPassword=$("#_ocx_password").val();
        var confirmPassword=$("#_ocx_password1").val();
        //判断非空
        if (isEmpty(realName)){
            layer.tips("真实姓名不能为空","#realName");
            return;
        }
        if (isEmpty(idCard)){
            layer.tips("身份证号不能为空","#card");
            return;
        }
        if(idCard.length!=18){
            layer.tips("身份证不合法","#card");
        }
        if (isEmpty(businessPassword)||isEmpty(confirmPassword)||(businessPassword!=confirmPassword)){
            layer.tips("密码非法","##_ocx_password");
        }

        //ajax交互参数
        $.ajax({
            type:"post",
            url:ctx+"/user/userAuth",
            data:{
                realName:realName,
                idCard:idCard,
                businessPassword:businessPassword,
                confirmPassword:confirmPassword
            },
            dataType:"json",
            success:function (data) {
                if (data.code==200){
                    alert("认证通过");
                }else {
                    layer.tips(data.msg,"#identityNext");
                    return;
                }
            }
        })
    })
})