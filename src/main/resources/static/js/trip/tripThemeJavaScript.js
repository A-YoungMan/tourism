/*Trip景区主题Ajax请求操作*/
function themeAjaxEdit(){
    /*查询当前拥有的主题*/
    var tripId=$("#themeTitle").children("span").text();//获取id
    var url = "/admin/trip/theme?act=query&tripId="+tripId;
    var tripTheme =AjaxGetRequest(url); //查询当前景区的以有的所有主题
    if(tripTheme !=null){
        $("#thisTitle").nextAll().remove();
        $.each(tripTheme.list,function(i,v){
            $("#thisTitle").after("<a class='btn btn-success "+v.id+"' style='margin-right: 10px;margin-bottom: 10px' order-toggle='tooltip' order-placement='top' title='"+v.detail+"' >"+v.name+"</a>")
        })
    }
    /*查询主题所有*/
    var url="/admin/trip/theme?act=list&tripId="+tripId;
    var theme =AjaxGetRequest(url);//查询所有
    $("#themeAll").siblings("a").remove();
    $.each(theme.list,function(i,v){
        $("#themeAll").after("<a class='btn btn-success "+v.id+"'  style='margin-right: 10px;margin-bottom: 10px' order-toggle='tooltip' order-placement='top' title='"+v.detail+"' >"+v.name+"</a>")
    })
    /*所有主题被点击时为添加*/
    var addurl="/admin/trip/theme?act=add";
    addthemeOrTripClick($("#themeAll").nextAll("a"),addurl,$("#thisTitle"),tripId);
    /*点击删除*/
    var removeUrl = "/admin/trip/theme?act=delete"
    removeTheme($("#thisTitle").nextAll("a"),removeUrl,$("#themeAll"),tripId)
}
/*添加*/
function addthemeOrTripClick(clickObject,AddOrDeleteurl,addDode,tripId){
    $(clickObject).click(function(){
        var className =  $(this).attr("class");
        var themeId =className.substring(className.indexOf("ss")+2,className.length);
        //添加
        var url =AddOrDeleteurl+"&themeId="+themeId+"&tripId="+tripId;
        var theme =AjaxGetRequest(url);
        $(this).remove();//删除当前元素
        $(this).next("div").remove();//删除提示弹窗
        if(theme !=null){
            $(addDode).nextAll().remove();
            $.each(theme.list,function(i,v){
                $(addDode).after("<a class='btn btn-success "+v.id+"' style='margin-right: 10px;margin-bottom: 10px' order-toggle='tooltip' order-placement='top' title='"+v.detail+"'>"+v.name+"</a>")
            })
            /*点击删除*/
            var removeUrl = "/admin/trip/theme?act=delete"
            removeTheme($("#thisTitle").nextAll("a"),removeUrl,$("#themeAll"),tripId)
        }
    })
}
/*删除*/
function removeTheme(objce,removerUrl,addDode,tripId){
    $(objce).click(function(){
        var className =  $(this).attr("class");
        var themeId =className.substring(className.indexOf("ss")+2,className.length);
        //添加
        var url =removerUrl+"&themeId="+themeId+"&tripId="+tripId;
        var theme =AjaxGetRequest(url);
        $(this).remove();//删除当前元素
        $(this).next("div").remove();//删除提示弹窗
        if(theme !=null){
            /*查询所有*/
            var allUrl = "/admin/trip/theme?act=list&tripId="+tripId;
            var findAll = AjaxGetRequest(allUrl);
            if(findAll !=null){
                $(addDode).nextAll().remove();
                $.each(findAll.list,function(i,v){
                    $(addDode).after("<a class='btn btn-success "+v.id+"' style='margin-right: 10px;margin-bottom: 10px' order-toggle='tooltip' order-placement='top' title='"+v.detail+"'>"+v.name+"</a>")
                })
                /*点击添加*/
                var addurl="/admin/trip/theme?act=add";
                addthemeOrTripClick($("#themeAll").nextAll("a"),addurl,$("#thisTitle"),tripId);
            }
        }
    })
}