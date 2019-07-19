/*日期操作遍历日期*/
function findAlldate(year,month,tripId){
    /*ajax请求*/
    var url = "/admin/trip/price?act=list&tripId="+tripId+"&month="+month+"&year="+year;
    var data = AjaxGetRequest(url);
    var date = new Date(year,(month-1));
    var week = date.getDay();
    var day = new Date(year,(month),0).getDate();
    var index = 1;///用于记录j的循环次数
    $("#priceDay").empty();
    for(var i =0;i<6;i++){
        var tr ="";
        var td ="";
        for(var j =0 ;j<7;j++){
            if(i ==0){ //第一行
                if(week ==j ){
                    var money =AjaxListTr(data.list,index);
                    td = td+"<td >"+index+money+"</td>"
                    index ++;
                }else if(j > week){
                    var money =AjaxListTr(data.list,index);
                    td = td+"<td >"+index+money+"</td>"
                    index ++;
                }else{
                    td = td+"<td></td>"
                }
            }else if(i == 4 || i == 5){//最后一行
                var money =AjaxListTr(data.list,index);
                var d =index > day ? "" : index;
                if(d == ""){
                    td = td+"<td></td>";
                }else{
                    td = td+"<td>"+d+money+"</td>";
                }
                index ++;
            }else{//中间行
                var money =AjaxListTr(data.list,index);
                td = td+"<td >"+index+money+"</td>";
                index ++;
            }
        }
        tr ="<tr>"+ td+"</tr>";
        $("#priceDay").append(tr);
    }
    updatePrice();//修改或删除价格
    deleteDate(year,month,tripId);
}
/*修改价格*/
function updatePrice(){
    var price ;
    $(".editPrice").on("focus",function(){ //获取焦点
        price =$(this).text().trim();
    })
    $(".editPrice").on("blur",function(){//失去焦点
        var object = $(this);
        var dataPrice =$(this).text().trim();
        var id = $(this).attr("id");//获取id
        var url = "/admin/trip/price?act=";
        if(dataPrice == ""){//为空时删除
            url = url+"delete&priceId="+id;
            $.get(url,{},function(data){
                if(data.ok){
                    $(object).parent("p").remove();//删除当前条
                    $("#dateH2").next("p").remove();
                    $("#dateH2").after("<p style='color: green;text-align: center'>删除成功!</p>")
                }
            })
        }else if(price!=dataPrice){//不一致时请求ajax修改
            //判断是否是合法整数，或两位小数
            var s =dataPrice. match(/^[1-9][0-9]*([.][0-9]{1,2})?$/);
            if(s !=null){
                url = url+"update&priceId="+id+"&price="+dataPrice;
                $.get(url,{},function(data){
                    if(data.ok){
                        $("#dateH2").next("p").remove();
                        $("#dateH2").after("<p style='color: green;text-align: center'>修改价格成功!</p>")
                    }
                })
            }else{
                $("#dateH2").next("p").remove();
                $("#dateH2").after("<p style='color: red;text-align: center'>价格错误，只能‘整数(不能0开头)’或‘小数(两位)’!</p>")
            }
        }

    })
}
/*清空当前日期下的所有价格*/
function deleteDate(year,month,tripId){
    $("#deleteDate").on("click",function(){
        var url="/admin/trip/price/deleteDate";
        $.post(url,{tripId:tripId,year:year,month:month},function(data){
            if(data.ok){
                findAlldate(year,month,tripId);
                $("#dateH2").next("p").remove();
                $("#dateH2").after("<p style='color: green;text-align: center'>删除清空成功!</p>")
            }
        })
    })
}

/*ajxalist集合遍历*/
function AjaxListTr(list,num){
    var str ="";
    for(var i=0;i<list.length;i++){
        var date = new Date(list[i].date);
        if( num == date.getDate()){
            str ="<p style='color: red' >¥<span contenteditable='true' class='editPrice' id='"+list[i].id+"'>"+list[i].price+"</span>起</p>"
            break;
        }
    }
    return str;
}
/*修改年份月份 */
function yearDate(){
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth()+1;
    $("#year").text(year+"年");
    $("#month").text(+month+"月");
}
/*单天价格添加*/
function priceDayadd(){
    $("#day").click(function(){
        //获取id
        var tripId =$("#priceTitle").children("span").text();
        var date =$("#dateDay").val();
        var price = $("input[name='priceDay']").val();
        if(date.trim() !=""){
            if(isDate(date.trim())){
                if(price.trim()!=null){ //价格不能为空
                    var s =price. match(/^[1-9][0-9]*([.][0-9]{1,2})?$/);
                    if(s!=null){ //ajax请求
                        var url="/admin/trip/price/add?tripId="+tripId+"&price="+price+"&date="+date;
                        // var order =AjaxGetRequest(url);
                        $.post(url,{tripId:tripId,price:price,date:date},function(data){
                            if(data.ok){
                                $("#day").next("p").remove(); //删除提示
                                $("#day").after("<p style='color:green'>添加价格成功!(单个)</p>")
                                //获取id
                                var tripId =$("#priceTitle").children("span").text();
                                var year = $("#year").text().substring(0,$("#year").text().length-1);
                                var month = $("#month").text().substring(0,$("#month").text().length-1);;
                                $("#priceDay").find("tr").remove();
                                findAlldate(year,month,tripId);
                                /*清空*/
                                $("#dateDay").val("");
                                $("input[name='priceDay']").val("");
                            }else{
                                $("#day").next("p").remove(); //删除提示
                                $("#day").after("<p style='color:red'>"+data.error+"</p>")
                            }
                        })
                    }else{
                        $(this).next("p").remove();
                        $(this).after("<p style='color: red'>价格错误，只能‘整数(不能0开头)’或‘小数(两位)’！</p>")
                    }
                }else{
                    $(this).next("p").remove();
                    $(this).after("<p style='color: red'>价格不能为空！</p>")
                }
            }else{
                $(this).next("p").remove();
                $(this).after("<p style='color: red'>日期格式错误！</p>")
            }
        }else{
            $(this).next("p").remove();
            $(this).after("<p style='color: red'>日期不能为空！</p>")
        }

    })
}

/*多日期选择*/
function pricesSave(){
    $("#pricesSave").click(function(){
        //获取id
        var tripId =$("#priceTitle").children("span").text();
        var date =$("input[name='date']").val(); //获取区间日期
        var price = $("input[name='price']").val(); //获取单价
        if(date.indexOf("—")!=-1){
            var arry = date.split("—");
            var is1 =isDate(arry[0].trim());
            var is2 = isDate(arry[1].trim());
            if(is1 && is2){
                if(price.trim()!=""){
                    var s =price. match(/^[1-9][0-9]*([.][0-9]{1,2})?$/);
                    if(s!=null){
                        var url="/admin/trip/price/adds?tripId="+tripId+"&price="+price+"&date="+date;
                        //    var order =AjaxGetRequest(url);
                        $.post(url,{tripId:tripId,price:price,date:date},function(data){//AJaxPost Request
                            var error = data.errorList.toString();
                            if(data.ok){
                                $("#pricesSave").next("p").remove(); //删除提示
                                if(error != ""){
                                    $("#pricesSave").after("<p style='color:green'>添加价格成功!(多个),<span style='color: red'>"+error+":日期已有价格</span></p>")
                                }else{
                                    $("#pricesSave").after("<p style='color:green'>添加价格成功(多个)!</p>")
                                }
                                //获取id
                                var tripId =$("#priceTitle").children("span").text();
                                var year = $("#year").text().substring(0,$("#year").text().length-1);
                                var month = $("#month").text().substring(0,$("#month").text().length-1);;
                                $("#priceDay").find("tr").remove();
                                findAlldate(year,month,tripId);
                            }else{
                                $("#pricesSave").next("p").remove(); //删除提示
                                if(error != ""){
                                    $("#pricesSave").after("<p style='color:red'>多日期价格添加失败!,"+data.errorList.toString()+":日期已有价格</p>");
                                }else{
                                    $("#pricesSave").after("<p style='color:red'>日期价格添加失败!(多个)!</p>")
                                }
                            }
                        })

                    }else{
                        $(this).next("p").remove();
                        $(this).after("<p style='color: red'>价格错误，只能‘整数(不能0开头)’或‘小数(两位)’！</p>")
                    }
                }else{
                    $(this).next("p").remove();
                    $(this).after("<p style='color:red;'>价格不能为空!</p>")
                }
            }else{
                $(this).next("p").remove();
                $(this).after("<p style='color:red;'>日期格式错误!</p>")
            }
        }else{
            $(this).next("p").remove();
            $(this).after("<p style='color:red;'>日期格式错误!</p>")
        }
    })
}

/*判断字符串是否为日期的合法合法*/
function isDate(str){
    var is = false;
    var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/;//匹配日期
    var r = str.match(reg);
    if(r !=null){
        is = true;
    }
    return is;
}