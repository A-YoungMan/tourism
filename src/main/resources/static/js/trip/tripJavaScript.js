/*AJax管理员操作景点js Admin/trip/list 增删改查等操作*/
/*加入回收站*/
function addRecovery(id, a) {
    var url = "/admin/trip/recovery?act=add&id=" + id;
    $.get(url, {}, function (data) {
        if (data.success) {
            $(a).parents("tr").remove();
            $('.bulle').removeClass("bulle-error");
            $('.bulle').html("成功加入回收站!").addClass('bulle-alert').show().delay(2500).fadeOut();
        } else {
            $('.bulle').removeClass("bulle-error");
            $('.bulle').html(data.error).addClass('bulle-alert').show().delay(2500).fadeOut();
        }
    })
}

/*读取发车*/
function startSave() {
    $(".start").click(function () {
        //初始化选择
        var a = $("#start").attr("class");
        a = a.substr(0, a.toString().length - 2);
        $("#start").attr("class", a).css("display", "none");
        $(".modal-backdrop").remove();
        //把出发地的的值放入input元素中
        var province = $("input[name='province']").val();
        var city = $("input[name='city']").val();
        var area = $("input[name='area']").val();
        $("input[name='cityName']").val(province + city + area);
    })
}

/*新增项目方法AJax 提交*/
function addTrip() {
    //拼接表单出来
    var htmlText = "<td style='text-align: center'><input class='form-control' type='text' name='num'placeholder='编号...'style='height: 30px;margin-top: 2px;width: 66px;'></td>" +
        "<td ><input class='form-control' type='text' name='title'placeholder='标题...' style='height: 30px;margin-top: 2px;width: 105px;'></td>" +
        "<td ><input class='form-control' type='text' name='sTitle' placeholder='小标题..' style='height: 30px;margin-top: 2px;width: 87px;'></td>" +
        "<td ><input class='form-control' type='text' name='cityName' placeholder='出发城市..' autocomplete='off' style='height: 30px;margin-top: 2px;width: 108px;' order-toggle='modal' order-target='.bs-example-modal-sm'></td>" +
        "<td ><input class='form-control' type='text' name='time' placeholder='出行天数..' style='height: 30px;margin-top: 2px;width: 87px;margin-left: 10px;'> </td>" +
        "<td ></td>"+
        "<td ><select name='hotel' class='form-control' style='height: 30px;margin-top: 2px;width: 105px;padding: 0'> " +
        "<option value='宾馆'>宾馆</option> " +
        "<option value='民宿'>民宿</option>" +
        "<option value='三星级酒店'>三星级酒店</option>" +
        "<option value='四星级酒店'>四星级酒店</option>" +
        "<option value='五星级酒店'>五星级酒店</option>" +
        "</select></td>" +
        "<td></td>"+
        "<td></td>"+
        "<td ><input type='radio' name='isOk' value='0' class='is' checked><label>上架</label>" +
        "<input type='radio'  name='isOk' value='1' class='is' ><label>下架</label></td>"
    $(".add").click(function () {
        //添加
        $(this).parents("tr").before("<tr class='form-group'>" + htmlText + "<td><a class='btn btn-info btn-xs button' >保存</a>  <a class='btn btn-danger btn-xs delete'>关闭</a></td></tr>");
        /*关闭*/
        $(".delete").on("click", function () {
            $(this).parents("tr").remove();
        })
        $("input[name='cityName']").on("click",function(){
            $(".bs-example-modal-sm").modal('show');
        })
        /*读取表单数据 使用ajax提交*/
        $(".button").on("click", function () {
            var tr = $(this).parents("tr");
            var tbody = $(this).parents("tbody");
            var province = $("input[name='province']").val();
            var city = $("input[name='city']").val();
            var area = $("input[name='area']").val();
            var tbody = $(this).parents("tbody");
            var ulIndex = $(this).parents("ul");
            var index = $(".subitems").next("ul").index(ulIndex) //获取下标
            var placeName = ulIndex.parents("span").prev(".project").text(); //获取景点的名称
            placeName = placeName.toString().replace("-", "");
            placeName = placeName.replace(/(^\s*)|(\s*$)/g, "");
            /*Ajax*/
            var url = "/admin/trip/addtrip";
            var data = $(this).parents("form").serialize();
            //转换.param(params);将表单序列化为键值对（key1=value1&key2=value2…）后提交，使用标准的 URL-encoded 编码表示文本字符串
            var text = $.param({"province": province, "name": city, "area": area})
            data = data + '&' + text;
            $.post(url, data, function (data) {
                if (data.ok) {
                    /*拼接弹窗!*/
                    $('.bulle').removeClass("bulle-error");
                    $('.bulle').html('保存成功!').addClass('bulle-alert').show().delay(2500).fadeOut();
                    $(tr).remove();//删除当前行//
                    var trIndex = $(tbody).find("tr").length-1;//获取当前的tbody下的所有tr的个数-1
                    $(tbody).find("tr:lt("+trIndex+")").remove(); //最后一条add添加不删除
                    //$(tbody).empty();//清空Tbody再添加
                    url = "/admin/trip/ajax?type=" + index + "&name=" + placeName;
                    $.getJSON(url,{}, function (data) {
                        $.each(data.list, function (i, v) {
                            var rate = v.goodRate == null ? 0 : v.goodRate;
                            var city = v.city ==null ?"未填写":v.city.name;
                            var title = v.title;
                            if(title.length >= 10){
                                title = title.substring(0,10)+".....";
                            }
                            //截取小标题
                            var stitle = v.stitle;
                            if(stitle.length >= 10){
                                stitle = stitle.substring(0,10)+".....";
                            }
                            //拼接表单出来
                            var htmlText = "<td style='vertical-align:middle;'>" + v.num + "</td>" +
                                "<td contenteditable class='vtitle' style='width: 105px;vertical-align:middle;'>" + title + "</td>" +
                                "<td contenteditable class='vstitle' style='width: 105px;vertical-align:middle;'>" +stitle + "</td>" +
                                "<td style='vertical-align:middle;'>" + city+ "</td>" +
                                "<td contenteditable class='time' style='width: 100px;vertical-align:middle;'>" + v.time + "</td>" +
                                "<td style='vertical-align:middle;'><a class='btn btn-info btn-xs tripPrice' order-toggle='modal' order-target='.tripPriceBox'>查看</a></td>" +
                                "<td style='vertical-align:middle;'>" + v.hotel + "</td>" +
                                "<td style='vertical-align:middle;'>" + rate + "</td>" +
                                "<td style='vertical-align:middle;'>" + str(v.createTime) + "</td>"
                            if (v.isOk == 0) {
                                htmlText = htmlText + "<td style='vertical-align:middle;'><a class='btn btn-success btn-xs up'>已上架<span hidden>" + v.id + "</span></a>&nbsp;&nbsp;" +
                                    "<a class='btn btn-default btn-xs down' style='color: white'>已下架<span hidden>" + v.id + "</span></a></td>"
                            } else {
                                htmlText = htmlText + "<td style='vertical-align:middle;'><a class='btn btn-default btn-xs up' style='color:white'>已上架<span hidden>" + v.id + "</span></a>&nbsp;&nbsp;" +
                                    "<a class='btn btn-success btn-xs down'>已下架<span hidden>" + v.id + "</span></a></td>"
                            }
                            htmlText = htmlText + "<td style='vertical-align:middle;'><a class='btn btn-primary btn-xs theme' order-toggle='modal' order-target='.themeBox'>主题</a>&nbsp;<a class='btn btn-warning btn-xs img' order-toggle='modal' order-target='.imgBox'>图片</a>&nbsp;<a class='btn btn-default btn-xs trip' order-toggle='modal' order-target='.tripBox'>行程</a>&nbsp;<a class='btn btn-default btn-xs detail' order-toggle='modal' order-target='.detailBox'>说明</a>&nbsp;<a class='btn btn-info btn-xs' href='/admin/trip/edit?id=" + v.id + "'>修改</a>&nbsp;<a class='btn btn-danger btn-xs' onClick='addRecovery(" + v.id + ",this)'>删除</a></td>"
                            tbody.prepend("<tr>" + htmlText + "</tr>")
                        })
                        /*价格详情*/
                        $(".tripPrice").on("click",function(){ //修改价格//添加标题
                            $(".tripPriceBox").modal('show');
                            var title =$(this).parent("td").prevAll(".vtitle").text();
                            //获取景区href中读取id
                            var href= $(this).parent("td").nextAll().find(".btn-info").attr("href");
                            //景区id
                            var tripId =href.substring(href.indexOf("id=")+3,href.length);//获取id
                            $("#priceTitle").empty();
                            $("#priceTitle").html(title+"<span hidden>"+tripId+"</span>");
                        })
                        /*行程详情*/
                        $(".trip").on("click",function(){
                            $(".tripBox").modal('show');
                            //获取标题
                            var title =$(this).parent("td").prevAll(".vtitle").text();
                            //景区id
                            var href= $(this).siblings(".btn-info").attr("href");
                            var tripId =href.substring(href.indexOf("id=")+3,href.length);//获取id
                            $("#tripTitle").html(title+"<span hidden>"+tripId+"</span>");
                        })
                        /*图片详情*/
                        $(".img").on("click",function(){
                            $(".imgBox").modal('show');
                            //获取标题
                            var title =$(this).parent("td").prevAll(".vtitle").text();
                            //景区id
                            var href= $(this).siblings(".btn-info").attr("href");
                            var tripId =href.substring(href.indexOf("id=")+3,href.length);//获取id
                            $("#imgTitle").html(title+"<span hidden>"+tripId+"</span>");
                        })
                        themeTitle($(".theme"));//获取名称和主题
                        updataTitleSTitle($(".vtitle"));//修改标题
                        updataTitleSTitle($(".vstitle"));//修改标题
                        updataTitleSTitle($(".time"));//修改标题
                        /*查看信息详情*/
                        $(".detail").on("click",function(){
                            $(".detailBox").modal('show');
                            var className =$(this).next("a").attr("href"); //获取id
                            var id =className.substring(className.indexOf("id=")+3,className.length); //截取获取当前景区id
                            var url = "/admin/trip/detail?act=query&tripId="+id;//查询
                            $.get(url,{},function(data){
                                $("#place").empty();//清空
                                $("#place").html(data.detail.place);
                                $("#hotel").empty();
                                $("#hotel").html(data.detail.hotel);
                                $("#food").empty();
                                $("#food").html(data.detail.food);
                                $(".h2").text(data.detail.trip.title);
                                $("#id").text(data.detail.id)
                            })
                        })
                        /*上架*/
                        $(".up").click(function () {
                            updownAjax($(this),"up");
                        });
                        /*下架*/
                        $(".down").click(function () {
                            updownAjax($(this),"down");
                        });
                    });
                } else {
                    $('.bulle').removeClass("bulle-alert");
                    $('.bulle').html(data.error).addClass('bulle-error').show().delay(2500).fadeOut();
                }
            })
        })
    })
}

/*样式*/
function expansion() {
    var placeName;//用于获取当前点击的景点名称
    var index;//记录行程类型下标
    $(".project").click(function () {
        var is = $(this).next("span").attr("class");
        if (is == "no") {
            //$(".project").html($(".project").html().replace("-","+")); //修改符号
            $(".project").each(function () {//遍历
                $(this).html($(this).html().replace("-", "+"))
            })
            $(".project").next("span").attr("class", "no");
            var on = $(this).html(); //修改符号
            //获取景点名称
            placeName = $(this).text();
            placeName = placeName.toString().substring(1, placeName.maxLength).replace("+", "");
            on = on.replace("+", "-");
            $(this).html(on);
            var b = $(this).next("span").attr("class");
            if (b == "no") {
                $(this).next("span").attr("class", "");
            } else {
                $(this).next("span").attr("class", "no").style.color = "";
            }
        } else {
            $(".project").each(function () {//遍历修改符号
                $(this).html($(this).html().replace("-", "+"))
            })
            $(".project").next("span").attr("class", "no");
        }
    })

    /*子项目展示*/
    $(".subitems").click(function () {
        var css = $(this).next("ul").css("display");
        if (css == "none") {//未展开
            $(".subitems").each(function () {//遍历修改符号
                $(this).html($(this).html().replace("-", "+"))
            })
            $(".subitems").next("ul").attr("class", "no");
            //获取当前下标
            index = $(".subitems").next("ul").index($(this).next("ul"));
            // //调用Ajax请求方法
            var name = placeName.replace(/(^\s*)|(\s*$)/g, "");
            var type = index;
            var url = "/admin/trip/ajax?type=" + type + "&name=" + name;
            var tbody = $(this).next("ul").find("tbody");//获取当前元素的属性
            var size = tbody.children("tr").length;
            tbody.children("tr").each(function (i) {
                if (i == size - 1) {
                } else {
                    $(this).remove();
                }
            })
            $.getJSON(url, {}, function (data) {
                $.each(data.list, function (i, v) {
                    var rate = v.goodRate == null ? 0 : v.goodRate;
                    var city = v.city ==null ?"未填写":v.city.name;
                    var title = v.title; //截取长标题
                    if(title.length >= 10){
                        title = title.substring(0,10)+".....";
                    }
                    //截取小标题
                    var stitle = v.stitle;
                    if(stitle.length >= 10){
                        stitle = stitle.substring(0,10)+".....";
                    }
                    //拼接表单出来
                    var htmlText = "<td style='vertical-align:middle;'>" + v.num + "</td>" +
                        "<td contenteditable class='vtitle' style='width: 105px;vertical-align:middle;'>" + title + "</td>" +
                        "<td contenteditable class='vstitle' style='width: 105px;vertical-align:middle;'>" + stitle + "</td>" +
                        "<td style='vertical-align:middle;'>" + city+ "</td>" +
                        "<td  contenteditable class='time' style='width: 100px;vertical-align:middle;'>" + v.time + "</td>" +
                        "<td style='vertical-align:middle;'><a class='btn btn-info btn-xs tripPrice' order-toggle='modal' order-target='.tripPriceBox'>查看</a></td>" +
                        "<td style='vertical-align:middle;'>" + v.hotel + "</td>" +
                        "<td style='vertical-align:middle;'>" + rate + "</td>" +
                        "<td style='vertical-align:middle;'>" + str(v.createTime) + "</td>"
                    if (v.isOk == 0) {
                        htmlText = htmlText + "<td style='vertical-align:middle;'><a class='btn btn-success btn-xs up' >已上架<span hidden>" + v.id + "</span></a>&nbsp;&nbsp;" +
                            "<a class='btn btn-default btn-xs down'>已下架<span hidden>" + v.id + "</span></a></td>"
                    } else {
                        htmlText = htmlText + "<td style='vertical-align:middle;'><a class='btn btn-default btn-xs up'>已上架<span hidden>" + v.id + "</span></a>&nbsp;&nbsp;" +
                            "<a class='btn btn-success btn-xs down' >已下架<span hidden>" + v.id + "</span></a></td>"
                    }
                    htmlText = htmlText + "<td style='vertical-align:middle;' ><a class='btn btn-primary btn-xs theme' order-toggle='modal' order-target='.themeBox'>主题</a>&nbsp;<a class='btn btn-warning btn-xs img' order-toggle='modal' order-target='.imgBox'>图片</a>&nbsp;<a class='btn btn-default btn-xs trip' order-toggle='modal' order-target='.tripBox'>行程</a>&nbsp;<a class='btn btn-default btn-xs detail' order-toggle='modal' order-target='.detailBox'>说明</a>&nbsp;<a class='btn btn-info btn-xs' href='/admin/trip/edit?act=add&id=" + v.id + "'>修改</a>&nbsp;<a class='btn btn-danger btn-xs'  onClick='addRecovery(" + v.id + ",this)'>删除</a></td>"
                    tbody.prepend("<tr>" + htmlText + "</tr>")
                });
                $(".tripPrice").on("click",function(){ //修改价格//添加标题
                    $(".tripPriceBox").modal('show');
                    var title =$(this).parent("td").prevAll(".vtitle").text();
                    //获取景区href中读取id
                    var href= $(this).parent("td").nextAll().find(".btn-info").attr("href");
                    //景区id
                    var tripId =href.substring(href.indexOf("id=")+3,href.length);//获取id
                    $("#priceTitle").empty();
                    var url = "/admin/trip/ajax/query";
                    $.post(url,{id:tripId,type:0},function(data){
                        $("#priceTitle").html(data.title+"<span hidden>"+tripId+"</span>");
                    })
                })
                /*行程详情*/
                $(".trip").on("click",function(){
                    $(".tripBox").modal('show');
                    //获取标题
                    var title =$(this).parent("td").prevAll(".vtitle").text();
                    //景区id
                    var href= $(this).siblings(".btn-info").attr("href");
                    var tripId =href.substring(href.indexOf("id=")+3,href.length);//获取id
                    var url = "/admin/trip/ajax/query";
                    $.post(url,{id:tripId,type:0},function(data){
                        $("#tripTitle").html(data.title+"<span hidden>"+tripId+"</span>");
                    })
                })
                /*图片详情*/
                $(".img").on("click",function(){
                    $(".imgBox").modal('show');
                    //获取标题
                    var title =$(this).parent("td").prevAll(".vtitle").text();
                    //景区id
                    var href= $(this).siblings(".btn-info").attr("href");
                    var tripId =href.substring(href.indexOf("id=")+3,href.length);//获取id
                    var url = "/admin/trip/ajax/query";
                    $.post(url,{id:tripId,type:0},function(data){
                        $("#imgTitle").html(data.title+"<span hidden>"+tripId+"</span>");
                    })
                })
                themeTitle($(".theme"));//获取名称和主题
                updataTitleSTitle($(".vtitle"));//修改标题
                updataTitleSTitle($(".vstitle"));//修改标题
                updataTitleSTitle($(".time"));//修改标题
                /*查看信息详情*/
                $(".detail").on("click",function(){
                    $(".detailBox").modal('show');
                    var className =$(this).next("a").attr("href"); //获取id
                    var id =className.substring(className.indexOf("id=")+3,className.length); //截取获取当前景区id
                    var url = "/admin/trip/detail?act=query&tripId="+id;//查询
                    $.get(url,{},function(data){
                        $("#place").empty();//清空
                        $("#place").html(data.detail.place);
                        $("#hotel").empty();
                        $("#hotel").html(data.detail.hotel);
                        $("#food").empty();
                        $("#food").html(data.detail.food);
                        $(".h2").text(data.detail.trip.title);
                        $("#id").text(data.detail.id)
                    })
                })
                /*上架*/
                $(".up").click(function () {
                    updownAjax($(this),"up");
                });
                /*下架*/
                $(".down").click(function () {
                    updownAjax($(this),"down");
                });
            });
            //修改符号
            var on = $(this).html();
            on = on.replace("+", "-");
            $(this).html(on);
            var is = $(this).next("ul").attr("class");
            if (is == "no") {
                $(this).next("ul").attr("class", "");
            } else {
                $(this).next("ul").attr("class", "no").style.color = "";
            }
        } else {//已展开
            $(".subitems").each(function () {//遍历修改符号
                $(this).html($(this).html().replace("-", "+"))
            })
            $(".subitems").next("ul").attr("class", "no");
        }
    })
}
/*主题名称与id*/
function themeTitle(object){
    $(object).on("click",function(){
        $(".themeBox").modal('show');
        //获取标题
        var title =$(this).parent("td").prevAll(".vtitle").text();
        //获取景区href中读取id
        var href= $(this).siblings(".btn-info").attr("href");
        //景区id
        var tripId =href.substring(href.indexOf("id=")+3,href.length);//获取id
        $("#themeTitle").empty();
        var url = "/admin/trip/ajax/query";
        $.post(url,{id:tripId,type:0},function(data){
            $("#themeTitle").html(data.title+"<span hidden>"+tripId+"</span>");
        })
    })
}
/*Ajax修改标题或小标题 、天数*/
function updataTitleSTitle(object){
    var text;
    $(object).on("focus",function(){ //获取焦点时
        text = $(this).text().replace(/(^\s*)|(\s*$)/g, ""); //去掉前后空格
            /*修改标题*/
            $(object).on("keyup",function(){//失去焦点时
                var title = $(this).text().replace(/(^\s*)|(\s*$)/g, "");
                if(title != ""){
                    if(text != title){
                        var edit = object;//获取当前对象
                        var className = edit.attr("class");//获取class名称
                        var id =$(this).nextAll("td").children(".up").children("span").text();
                        var url = "/admin/trip/recovery?act=title&id="+id;
                        switch (className) {
                            case"vtitle": //标题
                                url=url+"&title="+title;
                                break;
                            case"vstitle": //小标题
                                url=url+"&sTitle="+title;
                                break;
                            case"time": //天数
                                url=url+"&time="+title;
                                break;
                        }
                        $.get(url,{},function(data){
                            if (data.success != null) {
                                /*拼接弹窗!*/
                                $('.bulle').removeClass("bulle-error");
                                $('.bulle').html(data.success).addClass('bulle-alert').show().delay(2500).fadeOut();
                            }else{
                                /*拼接弹窗!*/
                                $('.bulle').removeClass("bulle-error");
                                $('.bulle').html(data.error).addClass('bulle-alert').show().delay(2500).fadeOut();
                            }
                        })
                    }
                }else{
                    $(this).text("不能为空!")
                }
            })
    })
}
/*Ajax请求上架或下架*/
function updownAjax(object, type) {
    var edit = object;//获取当前对象
    var className = edit.attr("class");
    if(className.toString().indexOf("btn-success") == -1){//-1则是没有上架
        var id = edit.children("span").text();//获取id
        var url = "/admin/trip/recovery?act=" + type + "&id=" + id;
        $.get(url, {}, function (data) {
            if (data.success != null) {
                edit.removeClass("btn-default").addClass("btn-success");
                edit.siblings("a").removeClass("btn-success").addClass("btn-default");
                alert(data.success)
            } else {
                alert(data.error)
            }
        })
    }
}
/*截取日期方法*/
function str(str) {
    str = str.substr(0, str.lastIndexOf("T"));
    return str;
}

//绑定城市下拉框的change事件
$("#city").change(loadArea);
$("#area").change(function () {
    test = test.substring(0, indextwe);
    var area = $("#area").find('option:selected').text();//获取被选中的值
    $("input[name='area']").empty();
    $("input[name='area']").val(area);//赋值给input
})

/*初始化省级*/
function init() {
    $("#province").attr("hidden", false);
    $.getJSON("/ajax/region", {}, function (data) {
        $("#province").empty();
        $.each(data, function (i, v) {
            $("#province").append("<option value='" + v.id + "''>" + v.name + "</option>");
        })
    })
}

//读取地区列表
function loadArea() {
    $("#area").attr("hidden", false);
    //获取选中的城市id
    var cityId = $("#city").val();
    var cityName = $("#city").find('option:selected').text();//获取被选中的值
    indextwe = test.toString().length;
    $("input[name='city']").empty();
    $("input[name='city']").val(cityName);//赋值给input

    $.getJSON("/ajax/region", {"pid": cityId}, function (json) {
        $("#area").empty();
        $.each(json, function (i, v) {
            $("#area").append("<option value='" + v.id + "''>" + v.name + "</option>");
        })
    })
}

//绑定省份下拉框的change事件
$("#province").change(function () {
    //获取选中的省份id
    $("#city").attr("hidden", false);
    var provinceId = $(this).val();
    test = $(this).find('option:selected').text();//获取被选中的值
    indexneo = test.toString().length;
    $("input[name='province']").empty();
    $("input[name='province']").val(test);//赋值给input

    $.getJSON("/ajax/region", {"pid": provinceId}, function (json) {
        $("#city").empty();
        $.each(json, function (i, v) {
            $("#city").append("<option value='" + v.id + "''>" + v.name + "</option>");
        })
    })
})
/*Ajax修改产品详情详情*/
function updataDetail(object){
    var text;
    $(object).on("focus",function(){ //获取焦点时
        text = $(this).html();
    })
    /*修改标题*/
    $(object).on("blur",function(){//失去焦点时
        var title = $(this).html();
        if(text != title){
            var edit = object;//获取当前对象
            var id =$("#id").text();//获取详情的id
            var type =edit.attr("id");//获取id名称
            var place;
            var hotel;
            var food;
            switch (type) {
                case "place":
                    place = title;
                    break;
                case "hotel":
                    hotel = title;
                    break;
                case "food":
                    food = title;
                    break;
            }
            var url = "/admin/trip/detail?act=update&id="+id;
            $.post(url,{place:place,hotel:hotel,food:food},function(data){
                edit.empty();
                switch (type) {
                    case "place":
                        $("#place").html(data.detail.place);
                        break;
                    case "hotel":
                        $("#hotel").html(data.detail.hotel);
                        break;
                    case "food":
                        $("#food").html(data.detail.food);
                        break;
                }
            })
        }
    })
}
/*标题或小标题缩进内容*/
function titleIndent(object){
    object.on("focus",function(){
        var td = $(this);
        //获取id
        var clazz = $(this).attr("class");
        var id = $(this).parent("tr").find("td:last").find(".btn-info").attr("href");
        id = id.substring(id.indexOf("id=")+3,id.length);
        var url = "/admin/trip/ajax/query";
        if(clazz == "vtitle"){//标题
            $.post(url,{id:id,type:0},function(data){
                td.text(data.title)
            })
        }else{ //小标题
            $.post(url,{id:id,type:1},function(data){
                td.text(data.stitle)
            })
        }
    })
    object.on("blur",function(){
        object.each(function(){
            var text = $(this).text().trim();
            if(text.length >= 10){
                $(this).text(text.substring(0,10)+".....");
            }
        })
    })
}
