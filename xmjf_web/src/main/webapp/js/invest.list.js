$(function () {
    /**
     * 页面加载完毕,填充表格数据及页码
     */
    loadInvestData();

    /**
     * 期限查询切换
     */
    $(".tab").click(function () {
        /**
         * 当前节点选中
         * 其他选中节点取消
         */
        $(this).addClass("list_active");
        $(".tab").not($(this)).removeClass("list_active");

        //获取点击数据
        var index = $(this).index();
        var isHistory = 0;
        if (index == 4) {
            isHistory = 1;
        }

        //传递参数
        loadInvestData(isHistory, null, index);

    })


})

/**
 * 方法中的参数:
 * 1. isHistory
 * 2. itemType
 * 3. itemCycle
 * 4. pageNum
 * 5. pageSize
 *
 */
function loadInvestData(isHistory, itemType, itemCycle, pageNum, pageSize) {
    var params = {};
    //默认查询可投标项目
    params.isHistory = 0;

    //判断,若给予查询值,则根据查询值来判断
    if (!isEmpty(isHistory)) {
        //查询给予参数
        params.isHistory = isHistory;
    }
    if (!isEmpty(itemType)) {
        params.itemType = itemType;
    }
    if (!isEmpty(itemCycle) && itemCycle != 0) {
        params.itemCycle = itemCycle;
    }
    if (!isEmpty(pageNum)) {
        params.pageNum = pageNum;
    }
    if (!isEmpty(pageSize)) {
        params.pageSize = pageSize;
    }

    //ajax请求
    $.ajax({
        type: "post",
        url: ctx + "/basItem/queryBasItemsByParams",
        data: params,
        dataType: "json",
        success: function (data) {
            //接收paginator及list数据
            var paginator = data.paginator;
            var list = data.list;
            if (list.length == 0) {
                $("#pages").html("<img style='margin-left: -70px;padding:40px;' " +
                    "src='/img/zanwushuju.png'>");
                $("#pcItemList").html("");
            } else {
                //初始化页面
                initTrHtml(list);
                //初始化进度框
                initItemScale();
                //初始化分页信息
                initPageHtml(paginator);
                //初始化倒计时方法
                initDjs();

            }

        }
    })
}

function initTrHtml(list) {
    if (list.length > 0) {
        //每循环一次,多一行
        var trs = ""
        for (var i = 0; i < list.length; i++) {
            var tempData = list[i];
            trs = trs + "<tr>";
            trs = trs + "<td><strong>" + tempData.itemRate + "</strong><span>%";
            //判断附加率,有则加
            if (!isEmpty(tempData.itemAddRate)) {
                trs = trs + "+" + tempData.itemAddRate
            }
            trs = trs + "%</span></td>";

            //期限添加
            trs = trs + "<td>" + tempData.itemCycle;
            //itemCycleUnit,1:天,2:月...
            if (tempData.itemCycleUnit == 1) {
                trs = trs + "天";
            }
            if (tempData.itemCycleUnit == 2) {
                trs = trs + "月";
            }
            if (tempData.itemCycleUnit == 3) {
                trs = trs + "季";
            }
            if (tempData.itemCycleUnit == 4) {
                trs = trs + "年";
            }
            trs = trs + "</td>";

            //产品添加
            trs = trs + "<td>";
            trs = trs + tempData.itemName;

            //判断项目的样式,层层判断
            if (tempData.itemIsnew == 1) {
                trs = trs + "<strong class='colorful' new>NEW</strong>";
            }
            if (tempData.itemIsnew == 0) {
                if (tempData.moveVip == 1) {
                    trs = trs + "<strong class='colorful' app>APP</strong>";
                }
            }

            if (tempData.itemIsnew == 0) {
                if (tempData.moveVip == 0) {
                    if (tempData.itemIsrecommend == 1) {
                        trs = trs + "<strong class='hot' app>HOT</strong>";
                    }
                }
            }

            if (tempData.itemIsnew == 0) {
                if (tempData.moveVip == 0) {
                    if (tempData.itemIsrecommend == 0) {
                        if (!isEmpty(tempData.password)) {
                            trs = trs + "<strong class='hot' psw>LOCK</strong>";
                        }
                    }
                }
            }

            trs = trs + "</td>";

            //添加信用评分
            trs = trs + "<td class='trust_range'>";
            if (tempData.total > 90) {
                trs = trs + "A+";
            }
            if (tempData.total > 85 && tempData.total <= 90) {
                trs = trs + "A";
            }
            if (tempData.total > 75 && tempData.total <= 85) {
                trs = trs + "A-";
            }
            if (tempData.total > 65 && tempData.total <= 75) {
                trs = trs + "B";
            }

            trs = trs + "</td>";

            //添加担保机构,统一图片
            trs = trs + "<td class='company'>";
            trs = trs + "<img src='/img/logo.png' alt='xmjf'>"
            trs = trs + "</td>";

            //判断,状态=10时,才显示进度条
            if (tempData.itemStatus == 1) {
                /**
                 * 倒计时实现, 定义countdown的节点
                 * @type {string}
                 */
                trs = trs + "<td>";
                trs = trs + "<strong class='countdown time' data-item='" + tempData.id + "' data-time='" + tempData.syTime + "'>";
                trs = trs + "<time class='hour'></time>&nbsp;:";
                trs = trs + "<time class='min'></time>&nbsp;:";
                trs = trs + "<time class='sec'></time>";
                trs = trs + "</td>";
            } else {
                trs = trs + "<td class='itemScale' data-val='" + tempData.itemScale + "'>";
                trs = trs + "</td>";
            }


            //项目操作,根据进度
            trs = trs + "<td>";
            var status = tempData.itemStatus;
            var href=ctx+"/basItem/itemDetailPage?itemId="+tempData.id;
            if (status == 1) {
                trs = trs + "<p><a href='"+href+"' ><input class=‘countdownButton’ valid type=‘button’ value='即将开标'></a></p>";
            }
            if (status == 10 || status == 13 || status == 18) {
                trs = trs + "<p class='left_money'>可投金额" + tempData.itemOngoingAccount + "元</p>"
                    + "<p><a href='"+href+"'><input valid type='button' value='立即投资'></a></p>";
            }
            if (status == 20) {
                trs = trs + "<p><a href='"+href+"'><input not_valid type='button' value='已抢完'></a></p>";
            }
            if (status == 30 || status == 31) {
                trs = trs + "<p><a href='"+href+"'><input not_valid type='button' value='还款中'></a></p>";
            }
            if (status == 32) {
                trs = trs + "<p style=‘position: relative’><a href='"+href+"' class=‘yihuankuan’><input not_valid type='button' value='已还款'></a></p>";
            }
            if (status == 23) {
                trs = trs + "<p><a href='"+href+"'><input not_valid type='button' value='已满标'></a></p>";
            }
            trs = trs + "</td>";


            trs = trs + "</tr>";
        }
        //添加截点
        $("#pcItemList").html(trs);
    }

}

function initPageHtml(paginator) {
    //获取数组
    var navigatepageNums = paginator.navigatepageNums;
    if (navigatepageNums.length > 0) {
        //拼接,数组中的数字,代表有几页
        var list = "";
        for (var j = 0; j < navigatepageNums.length; j++) {
            var page = navigatepageNums[j];
            //转到指定页面
            var href = "javascript:toPageData(" + page + ")";
            //选中页面,判断
            if (page == paginator.pageNum) {

                list = list + "<li class='active'><a href='" + href + "' title='第" + page + "页'>" + page + "</a></li>"
            } else {
                list = list + "<li><a href='" + href + "' title='第" + page + "页'>" + page + "</a></li>";
            }


        }
        //装配
        $("#pages").html(list);
    }


}

function initItemData(itemType) {
    //获取类别
    var itemType = itemType;
    var index = $(".list_active").index();
    var isHistory = 0;
    if (index == 4) {
        isHistory = 1;
    }
    loadInvestData(isHistory, itemType, index);

}

function toPageData(page) {
    var pageNum = page;
    var itemType = $("#itemType").val();
    var index = $(".list_active").index();
    var isHistory = 0;
    if (index == 4) {
        isHistory = 1;
    }
    loadInvestData(isHistory, itemType, index, pageNum);

}


//渲染进度框
function initItemScale() {
    $(".itemScale").radialIndicator();
    var arrs = $(".itemScale");
    if (arrs.length > 0) {
        arrs.each(function () {
            //取得进度值
            var val = $(this).attr("data-val");
            var radialObj = $(this).data('radialIndicator');
            radialObj.option("barColoer", "orange");
            radialObj.option("percentage", true);
            radialObj.option("barWidth", 10);
            radialObj.option("radius", 40);
            //传入进度条值
            radialObj.value(val)

        })
    }
}

function initDjs() {
    /**
     * 遍历每个含有countdown类的节点,并设置相应属性,放入timer方法中
     */
    $(".countdown").each(function () {
        var sytime = $(this).attr("data-time");
        var itemId = $(this).attr("data-item");
        timer(sytime, $(this), itemId);
    })
}

//倒计时
function timer(intDiff, obj, itemId) {
    if (obj.timers) {
        clearInterval(obj.timers);
    }

    obj.timers = setInterval(function () {
        var day = 0,
            hour = 0,
            minute = 0,
            second = 0;//时间默认值
        if (intDiff > 0) {
            day = Math.floor(intDiff / (60 * 60 * 24));
            hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
            minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
            second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
        }

        if (minute <= 9) minute = '0' + minute;
        if (second <= 9) second = '0' + second;
        obj.find('.hour').html(hour);
        obj.find('.min').html(minute);
        obj.find('.sec').html(second);
        intDiff--;
        if (intDiff == -1) {
            $.ajax({
                url: ctx + '/basItem/updateBasItemStatusToOpen',
                dataType: 'json',
                type: 'post',
                data: {
                    itemId: itemId
                },
                success: function (data) {
                    if (data.code == 200) {
                        window.location.reload()
                    }
                },
                error: function (textStatus, errorThrown) {

                }
            });
        }
    }, 1000);
}