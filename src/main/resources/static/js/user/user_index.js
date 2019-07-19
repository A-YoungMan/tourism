/**
 * 用户首页操作
 */
function contactCssShow(){/*用户联系人提示样式*/
    /*用户联系人提示样式*/
    setInterval(function(){
        $(".contactSave").attr('disabled',true);
        $("input[name='cardno']").next("p").remove();
        $("input[name='cardno']").removeClass("redBorder");
        var name = $("input[name='name']").val();
        var phone = $("input[name='phone']").val();
        if(name == ""){
            $("input[name='name']").removeClass("successBorder")
            $("input[name='name']").addClass("redBorder");
            $(".contactSave").attr('disabled',true);
        }else{
            $("input[name='name']").removeClass("redBorder");
            $("input[name='name']").addClass("successBorder");
            $(".contactSave").attr('disabled',false);
        }
        if(phone == ""){
            $("input[name='phone']").removeClass("successBorder")
            $("input[name='phone']").addClass("redBorder");
            $(".contactSave").attr('disabled',true);
        }else{
            if(!(/^1[3456789]\d{9}$/.test(phone))){
                $("input[name='phone']").next("p").remove();
                $("input[name='phone']").after("<p style='color: red'>请输入正确的手机号码!</p>")
                $(".contactSave").attr('disabled',true);
            }else{
                $("input[name='phone']").next("p").remove();
                $("input[name='phone']").removeClass("redBorder");
                $("input[name='phone']").addClass("successBorder");
                $(".contactSave").attr('disabled',false);
            }
        }
        /*匹配身份证号码*/
        var cardno = $("input[name='cardno']").val().trim();///
        if(cardno !=""){
            var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
            if((reg.test(cardno)) == false){
                $("input[name='cardno']").removeClass("successBorder");
                $("input[name='cardno']").addClass("redBorder");
                $("input[name='cardno']").next("p").remove();
                $("input[name='cardno']").after("<p style='color: red'>请输入正确的身份证号码!</p>")
                $(".contactSave").attr('disabled',true);
            }else{
                $("input[name='cardno']").next("p").remove();
                $("input[name='cardno']").removeClass("redBorder");
                $("input[name='cardno']").addClass("successBorder");
                $(".contactSave").attr('disabled',false);
            }
        }
    },200)
}
/*序列订单操作详情，删除等*/
function SequenceOnClickEdit(){
    $(".deleteOrder").on("click",function(){
        var id = $(this).prev("a").attr("name");
        var tr = $(this).parents("tr");//找到父类元素tr
        $(tr).parents(".card-body").prev(".card-header").next("p").remove();
        var url="/user/ajax";
        $.post(url,{act:"deleteSequence",sequenceId:id},function(data){
            if(data.ok){
                var index =$(".deleteOrder").parents("table").find("tr").length;
                if(index>1){ //多条，删除一条
                    $(tr).parents(".card-body").prev(".card-header").after("<p style='color: greenyellow'>删除成功!</p>")
                    $(tr).remove();//删除当前行数
                }else{// 一条清空div
                    tr.parents(".card-body").prev(".card-header").parent("div").remove();
                    alert("删除成功");
                }
            }
        })
    })
}
//页面刷新开始请求ajax判断是否有存在未操作的订单
function ajaxSequence(){
    var url = "/user/ajax"
    $.post(url,{act:"querySequence"},function(data){
        if( typeof(data.sequence)  != "undefined"){
            $("#prompt").removeAttr("hidden")
            $("#prompt").next("div").removeAttr("hidden");
            $("#ShowH3").html("当前系统检测到您有<span style='color: red'>“"+data.sequence+"”</span>条订单未处理,请尽快处理!");
        }
    })

    $(".close").on("click",function(){
        $("#prompt").attr("hidden",true)
        $("#prompt").next("div").attr("hidden",true)
    })
    $(".orderDetail").on("click",function(){
        $("#prompt").attr("hidden",true)
        $("#prompt").next("div").attr("hidden",true)
    })
    var index = 0;
    var close = setInterval(function(){
        if(index == 5){
            $("#prompt").attr("hidden",true)
            $("#prompt").next("div").attr("hidden",true)
        }
        $("#prompt").next("div").children("h2").children("span").next("span").remove();
        $("#prompt").next("div").children("h2").children("span").after("<span style='color:white'>还有"+(5-index)+"秒自动关闭!</span>")
        index++;
    },1000)
}
//查询日期
function findAllDate(){
    var date = new Date();
    $(".stat-digit:eq(0)").text(date.getFullYear())
    $(".stat-digit:eq(1)").text(date.getMonth()+1)
    $(".stat-digit:eq(2)").text(date.getDate())
    var arr = ['日','一','二','三','四','五','六']
    $(".stat-digit:last").text(arr[date.getDay()])
}
/*Ajax请求添加删除修改联系人*/
function userAddOrDeleteContact(){
    $("input[name='name']").keyup(function(){
        var value = $(this).val();
        var url = "/user/contact?act=query";
        $.post(url,{name:value},function(data){
            if(typeof (data.ok) == "undefined"){
                $(this).addClass("successBorder");
                $(this).removeClass("redBorder");
                $(".contactSave").attr('disabled',false);
            }else{
                $(this).removeClass("successBorder");
                $(this).addClass("redBorder");
                $(".contactSave").attr('disabled',true);
            }
        })
    })
    /*内容发生变化时修改信息*/
    $(".contactBox").find(".td").keyup(function(){
        //获取id
        var id = $(this).parents("table").next("a").attr("name");
        var index = $(".contactBox").find(".td").index($(this))%4;
        var text = $(this).text().trim();//获取当前输入的值
        switch (index) {
            case 0://名字
                if(text !=""){
                    ajaxUpdate($(this),"name",text,id);
                }else{
                    $(this).addClass("redBorder")
                }
                break;
            case 1://手机号码
                if(text !=""){
                    var is = isPhone(text);
                    if(is){
                        ajaxUpdate($(this),"phone",text,id);
                    }else{
                        $(this).addClass("redBorder")
                    }
                }
                break;
            case 2://身份证号码
                if(text !=""){
                    var is = isCardno(text);
                    if(is){
                        ajaxUpdate($(this),"cardno",text,id);
                    }else{
                        $(this).addClass("redBorder")
                    }
                }
                break;
            case 3://电子邮件
                if(text !=""){
                    var is = isEmail(text);
                    if(is){
                        ajaxUpdate($(this),"email",text,id);
                    }else{
                        $(this).addClass("redBorder")
                    }
                }
                break;
        }

    })
    /*内部方法Ajax修改操纵*/
    function ajaxUpdate(objcet,type,text,id){
        var url="/user/contact?act=update&"+type+"="+text;
        $.post(url,{id:id},function(data){
            if(data.ok){
                $(objcet).removeClass("redBorder");
            }
        })
    }
    /*删除*/
    $(".contactDelete").on("click",function(){
        if(confirm('确定删除吗?')){
            $("#contact").parents("row").prev("h4").next("p").remove();
            var id = $(this).attr("name");
            var a =$(this);
            $(a).prev("table").parent("div").siblings("h4").next("p").remove();
            var url = "/user/contact?act=delete";
            $.post(url,{id:id},function(data){
                if(data.ok){
                    $(a).prev("table").parent("div").siblings("h4").after("<p style='color: green'>删除联系人成功!</p>")
                    $(a).prev("table").parent("div").remove();
                }else{
                    $(a).prev("table").parent("div").siblings("h4").after("<p style='color: red'>删除联系人失败!</p>")
                }
            })
        }
    })
    /*添加*/
    $(".contactSave").on("click",function(){
        $(".contactDelete").prev("table").parent("div").siblings("h4").next("p").remove();
        var value =$("#contact").serialize();
        var url= "/user/contact?act=save";
        $("#contact").parents("row").prev("h4").next("p").remove();
        $.ajax({
            url:url ,
            type: 'POST',
            data: value,
            success: function(data){
                if(data.ok){
                    $(".contactBox:eq(0)").clone(true).appendTo($(".contactBox").parent("div"));
                    $("#contact").parents(".row").prev("h4").after("<p style='color: green'>添加联系人成功!</p>")
                    $("input").val("");
                    /*添加成功后后面追加div追加至最后一个div*/
                    var index =$(".contactBox").length;
                    index = index-1<0?0:index-1;
                    $(".contactBox:last").attr("hidden",false);
                    $(".contactBox:last").find("a").attr("name",data.contact.id)
                    $(".contactBox:last").find("tr:eq(0)").find("td:eq(1)").text(data.contact.name)
                    var sex ;
                    if(data.contact.sex ==0){
                        sex = "男"
                    }else if(data.contact.sex ==1){
                        sex = "女"
                    }else{
                        sex = "中性"
                    }
                    $(".contactBox:last").find("tr:eq(1)").find("td:eq(1)").text(sex)
                    $(".contactBox:last").find("tr:eq(2)").find("td:eq(1)").text(data.contact.phone)
                    $(".contactBox:last").find("tr:eq(3)").find("td:eq(1)").text(data.contact.cardno)
                    $(".contactBox:last").find("tr:eq(4)").find("td:eq(1)").text(data.contact.email)
                    var date = new Date(data.contact.birthday).Format("yyyy-MM-dd")
                    $(".contactBox:last").find("tr:eq(5)").find("td:eq(1)").text(date)
                }else{
                    $("#contact").parents(".row").prev("h4").after("<p style='color: red'>添加联系人失败!</p>")
                }
            }
        })
    })
}
/*用于验证手机号*/
function isPhone(str){
    if(!(/^1[3456789]\d{9}$/.test(str))){
        return false;
    }else{
        return true;
    }
}
/*用于验证身份证号码*/
function isCardno(str){
    var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    if((reg.test(str)) == false){
        return false;
    }else{
        return true;
    }
}
/*用于验证电子邮件*/
function isEmail(str){
    var re = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
    if(re.test(str)){
        return true;
    }else{
        return false;
    }
}
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}