$(function () {
    //显示单独项目进度
    $("#rate").radialIndicator();
    var val = $("#rate").attr("data-val");
    var radialObj = $("#rate").data('radialIndicator');
    radialObj.option("barColoer", "orange");
    radialObj.option("percentage", true);
    radialObj.option("barWidth", 10);
    radialObj.option("radius", 40);
    //传入进度条值
    radialObj.value(val)

    //借款详情,风险措施,投标记录跳转
    $("#tabs div").click(function () {
        //增加选中显示类
        $(this).addClass("tab_active");
        //根据选中的当前类的序号值,显示不同的content页面.
        var show=$("#contents .tab_content").eq($(this).index());
        show.show();
        //取消先前的选中
        $("#tabs div").not($(this)).removeClass("tab_active");
        //非显示页面隐藏
        $("#contents .tab_content").not(show).hide();
        if($(this).index()==2){
            //loadInvestRecodesList();
        }
    })
});

//点击充值后的认证,ajax无需传入数值
function toRecharge() {
    $.ajax({
        type:"post",
        url:ctx+"/user/userAuthCheck",
        dataType:"json",
        success:function (data) {
            //如果验证通过,提示以验证,可以跳转.
            if (data.code==200){
                window.location.href=ctx+"/account/rechargePage";

            }else{
                //layer插件
                layer.confirm(data.msg,{
                    btn: ['执行认证','稍后认证']
                },function () {
                    window.location.href=ctx+"/user/auth";
                });
            }
        }

    })
    
}