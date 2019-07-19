/*景区图片操作*/
//修改标题主图片标题
function updateTitle(){
    $("#main").next("h2").focus(function(){//获取焦点事件
        var id = $("#base64Img").attr("title");
        if(typeof (id) !="undefined"){
            $(this).keyup(function(){//键盘按下事件
                var title =$(this).text();
                var url='/admin/trip/img/update?date='+new Date();
                $.post(url,{id:id,name:title},function(data){
                })
            })
        }else{
            $(this).removeAttr("contenteditable");
        }
    })
}
/*添加Trip主图片*/
function addTripMainImg(e){
    var id = $("#base64Img").attr("title");//获取图片id
    var tripId =$("#imgTitle").children("span").text();//获取id
    var imgId = $("#base64Img").attr("title");//获取图片id
    var inputVal = $("#main").children("input").val();//获取文件后缀
    var fileEnd = inputVal.substring(inputVal.indexOf(".")+1,inputVal.length);
    var imgType = ['jpg','jpeg','gif','ai','png'];
    var is = false;
    for(var i = 0;i<imgType.length;i++){
        if(imgType[i] == fileEnd){
            is = true;
        }
    }
    if(is){
        var reader = new FileReader();
        reader.readAsDataURL(e.target.files[0]); // 读出 base64
        reader.onloadend = function () {
            // 图片的 base64 格式, 可以直接当成 img 的 src 属性值
            var dataURL = reader.result;//base64
            if(typeof(id)=="undefined"){ //新增
                var url= '/admin/trip/img/add?tripId='+tripId+"&type=0&date="+new Date();
                $.post(url,{base64:dataURL},function(data){
                    if(data.ok){
                        //主图添加
                        $("#imgMain").find("i").css("display","none");
                        $("#base64Img").css("display","block");
                        $("#base64Img").attr("title",data.tripPicture.id);
                        $("#base64Img").attr("src",data.tripPicture.base64);
                        $("#main").next("h2").html(data.tripPicture.name);
                        $("#imgMain").next("a").css("display","block");
                        $("#main").next("h2").prop("contenteditable",true);//设置标题可编辑
                    }else{
                        $("#imgTitle").next("p").remove();
                        $("#imgTitle").after("<p style='color: red'>"+data.error+"</p>");
                        $("#imgMain").find("i").css("display","block");
                        $("#imgMain").next("a").css("display","none");
                        $("#base64Img").css("display","none");
                        $("#base64Img").attr("src","");
                    }
                })
            }else{ //修改
                var url= '/admin/trip/img/update?date='+new Date();
                $.post(url,{id:id,base64:dataURL},function(data){
                    if(data.ok){//修改主图
                        $("#base64Img").attr("src",data.base64);
                        if(data.name != ""){
                            $("#main").next("h2").html(data.name);
                        }
                    }
                })
            }
        };
    }else{
        $("#imgTitle").next("p").remove();
        $("#imgTitle").after("<p style='color: red'>文件类型不匹配!支持文件类型:"+imgType.toString()+"</p>");
    }
}

//修改副图片标题
function updateDeputyTitle(){
    $(".liDeputy").find("h4").focus(function(){//获取焦点事件
        var id = $(this).prev("div").find("img").attr("title");
        if(typeof (id) !="undefined"){
            $(this).keyup(function(){//键盘按下事件
                var title =$(this).text();
                var url='/admin/trip/img/update?date='+new Date();
                $.post(url,{id:id,name:title},function(data){
                })
            })
        }else{
            $(this).removeAttr("contenteditable");
        }
    })
}
/*添加副图*/
function addTripDeputyImg(click,e){
    var id =click.find("img").attr("title");//获取图片id
    var tripId =$("#imgTitle").children("span").text();//获取id
    var inputVal = $("#deputy").children("input").val();//获取文件后缀
    var fileEnd = inputVal.substring(inputVal.indexOf(".")+1,inputVal.length);
    var imgType = ['jpg','jpeg','gif','ai','png'];
    var is = false;
    for(var i = 0;i<imgType.length;i++){
        if(imgType[i] == fileEnd){
            is = true;
        }
    }
    if(is){
        var reader = new FileReader();
        reader.readAsDataURL(e.target.files[0]); // 读出 base64
        reader.onloadend = function () {
            // 图片的 base64 格式, 可以直接当成 img 的 src 属性值
            var dataURL = reader.result;//base64
            if(typeof(id)=="undefined"){
                var url= '/admin/trip/img/add?date='+new Date();
                $.post(url,{base64:dataURL,tripId:tripId,type:"1"},function(data){
                    if(data.ok){
                        var number = $("#ul").find(".liDeputy").length;//获取个数
                        $("#ul > .liDeputy:lt("+(number - 1)+")").remove();//除了最后一个不删除其余全部删除
                        if(data.tripPictureList !=""){
                            $.each(data.tripPictureList,function(i,v){
                                if(v.type == 1){
                                    $('.liDeputy:last').clone(true).appendTo($("#ul"));
                                    $(".liDeputy:eq("+(i)+")").find('i').css("display","none");
                                    $(".liDeputy:eq("+(i)+")").find('.imgDeputy').css("display","block");
                                    $(".liDeputy:eq("+(i)+")").find('.imgDeputy').attr("src",v.base64);
                                    $(".liDeputy:eq("+(i)+")").find('.imgDeputy').attr("title",v.id);
                                    $(".liDeputy:eq("+(i)+")").find("h4").html(v.name)
                                    $(".liDeputy:eq("+(i)+")").find(".deleteImg").css("display","block");
                                }
                            })
                            /*最后一条*/
                            $("#ul").find(".btnDeputy:last").find('i').css("display","block");
                            $("#ul").find('.imgDeputy:last').css("display","none");
                            $("#ul").find(".imgDeputy:last").attr("src","");
                            $("#ul").find(".imgDeputy:last").removeAttr("title");
                            $("#ul").find(".deleteImg:last").css("display","none");
                        }
                    }else{
                        $("#imgTitle").next("p").remove();
                        $("#imgTitle").after("<p style='color: red'>"+data.error+"</p>");
                        click.children("i").css("display","block");
                        click.find("img").css("display","none");
                        click.find("img").attr("src","");
                        click.find("img").removeAttr("title");
                    }
                })
            }else{ //修改
                var url= '/admin/trip/img/update?date='+new Date();
                $.post(url,{id:id,base64:dataURL},function(data){
                    click.find("img").attr("src",data.base64);
                })
            }
        };
    }else{
        $("#imgTitle").next("p").remove();
        $("#imgTitle").after("<p style='color: red'>文件类型不匹配!支持文件类型:"+imgType.toString()+"</p>");
    }
}

/*删除图片*/
function deleteImg(){
    $(".deleteImg").on("click",function(){
        var id = $(this).prev("a").find("img").attr("title");//获取id
        var type =$(this).prev("a").attr("id");//用于判断是否是主图或副图
        var li = $(this).parents(".liDeputy");
        var url = "/admin/trip/img/delete";
        $.post(url,{id:id},function(data){
            if(data.ok){
                $("#imgTitle").next("p").remove();
                $("#imgTitle").after("<p style='color: green'>成功删除一张图片!</p>");
                if(type == "imgMain"){//主图
                    $("#imgMain").find("img").css("display","none");
                    $("#imgMain").find("img").attr("src","");
                    $("#imgMain").find("img").removeAttr("title");
                    $("#imgMain").find("i").css("display","block");
                    $("#imgMain").next("a").css("display","none");
                    $("#main").next("h2").html("标题");
                }else{//副图
                    li.remove();//删除当前li
                }
            }else{
                $("#imgTitle").next("p").remove();
                $("#imgTitle").after("<p style='color: red'>删除失败请重试!</p>");
            }
        })
    })
}