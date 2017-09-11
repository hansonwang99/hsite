/**
 * Created by Administrator on 2017/7/16.
 */

$(function() {
    loadCategories();
});


function loadConfig(){
    $.ajax({
        async: true,
        type: 'POST',
        dataType: 'json',
        url: '/user/getConfig',
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest);
            console.log(textStatus);
            console.log(errorThrown);
        },
        success: function(config){
            gconfig=config;
            $("#defaultCollectType").html("");
            $("#defaultModel").html("");
            $("#defaultFavorites").html("");
            initConfigDatas(config);
            //设置默认选中收藏夹
            obj = document.getElementById("layoutFavoritesName");
            for(i=0;i<obj.length;i++){
                if(obj[i].value == config.defaultFavorties){
                    obj[i].selected = true;
                    $("#defaultFavorites").append("<strong>默认收藏夹(" +obj[i].text +")");
                }
            }
        }
    });
}

function loadCategories(){
    $.ajax({
        async: false,
        type: 'POST',
        dataType: 'json',
        url: '/category/getCategory/0',
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest);
            console.log(textStatus);
            console.log(errorThrown);
        },
        success: function(categories){

            $("#unread").html("");
            $("#favorites > li").each(function(i){
                if(i != 0 && i != 1){
                    $(this).remove();
                }
            });
            $("#layoutFavoritesName").html("");

            initFavorites(categories);
        }
    });
}

function loadFollows(){
    $.ajax({
        async: false,
        type: 'POST',
        dataType: 'json',
        url: '/user/getFollows',
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest);
            console.log(textStatus);
            console.log(errorThrown);
        },
        success: function(follows){
            gfollows=follows;
            initFollows(follows);
        }
    });
}

function initFavorites(categories){

    $("#favoritesSelect").empty();
    $("#postCategory").empty();
    for(var i=0;i<categories.length;i++){
        var id = categories[i].id ;
        var name = categories[i].name;
        if(getByteLen(name)>16){
            name = cut_str(name,8)+"...";
        }
        var count = categories[i].count;
        var url ='/standard/'+ id + "/0";
        if(name=="未读列表"){
            var favorite="<a href=\"javascript:void(0);\" onclick=\"locationUrl('"+url+"','unread')\" title="+name+" >";
            if(count > 0){
                favorite=favorite+"<div class=\"label label-success pull-right\">"+count+"</div>";
            }
            favorite=favorite+"<em class=\"icon-paper-clip\"></em>";
            favorite=favorite+"<span>"+name+"</span>";
            favorite=favorite+"</a>";
            $("#unread").append(favorite)
        }else{
            var favorite="<li id="+id+">";
            favorite=favorite+"<a href=\"javascript:void(0);\" onclick=\"locationUrl('"+url+"','"+id+"')\" title="+name+" >";
            favorite=favorite+"<div class=\"text-muted mr pull-right\">"+count+"</div>";
            favorite=favorite+"<span>"+name+"</span>";
            favorite=favorite+"</a></li>";
            $("#newCategory").after(favorite)

            $("#postCategory").append("<option value='"+ id +"'>"+name+"</option>");
        }
        //collct favorites
        $("#favoritesSelect").append("<option value=\"" + id + "\">" + name + "</option>");
        $("#layoutFavoritesName").append("<option value=\"" + id + "\">" + name + "</option>");

    }
    if(null != gconfig){
        $("#favoritesSelect").val(gconfig.defaultFavorties);
    }
}

function getByteLen(val) {
    var len = 0;
    for (var i = 0; i < val.length; i++) {
        var a = val.charAt(i);
        if (a.match(/[^\x00-\xff]/ig) != null)
        {
            len += 2;
        }
        else
        {
            len += 1;
        }
    }
    return len;
}

function cut_str(str, len){
    var char_length = 0;
    for (var i = 0; i < str.length; i++){
        var son_str = str.charAt(i);
        encodeURI(son_str).length > 2 ? char_length += 1 : char_length += 0.5;
        if (char_length >= len){
            var sub_len = char_length == len ? i+1 : i;
            return str.substr(0, sub_len);
            break;
        }
    }
}

function initConfigDatas(config){
    $("#defaultCollectType").append("<strong>默认"+config.collectTypeName+"收藏（点击切换）</strong>")
    $("#defaultModel").append("<strong>收藏时显示" +config.modelName+"模式</strong>");
}

function initFollows(follows){
    $("#friends").html("");
    var friends="";
    for(var i=0;i<follows.length;i++){
        var name="<a href=\"javascript:void(0);\" onclick=\"showAt('"+follows[i]+"')\" >"+follows[i]+"</a>";
        friends=friends+name;
    }
    $("#friends").append(friends);
}



var userXmlhttp = new getXMLObject();
function userGoUrl(url,params) {
    fixUrl(url,params);
    if(userXmlhttp) {
        //var params = "";
        userXmlhttp.open("POST",url,true);
        userXmlhttp.onreadystatechange = userHandleServerResponse;
        userXmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8');
        userXmlhttp.send(params);
        with(document)0[(getElementsByTagName('head')[0]||body).appendChild(createElement('script')).src='http://v3.jiathis.com/code_mini/jia.js?uid=2126448'];
    }
}




/**
 * 后退
 */
function historyBack(){
    if(flag == 1){
        goUrl(firstUrl);
    }else{
        goUrl(secondUrl);
    }
}






function userHandleServerResponse() {
    if (userXmlhttp.readyState == 4) {
        //document.getElementById("mainSection").innerHTML =xmlhttp.responseText;
        var text = userXmlhttp.responseText;
        if(text.indexOf("<title>Favorites error Page</title>") >= 0){
            window.location.href="/error.html";
        }else{
            $("#usercontent").html(userXmlhttp.responseText);
        }

    }
}

function showContent(url){
    $.get("/html/"+url+".html", function(data){
        $("#main-content").html(data);
        initContentPage();
    },"html");
}

function updateFavorites(){
    var ok = $('#updateFavoritesForm').parsley().isValid({force: true});
    if(ok){
        $.ajax({
            async: false,
            type: 'POST',
            dataType: 'json',
            data:$("#updateFavoritesForm").serialize(),
            url: '/favorites/update',
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                console.log(XMLHttpRequest);
                console.log(textStatus);
                console.log(errorThrown);
            },
            success: function(response){
                if(response.rspCode == '000000'){
                    $("input[name='favoritesName']").val("");
                    loadFavorites();
                    locationUrl("/standard/" + $("#favoritesId").val() + "/0",$("#favoritesId").val());
                    $("#updateFavoritesBtn").attr("aria-hidden","true");
                    $("#updateFavoritesBtn").attr("data-dismiss","modal");
                }else{
                    $("#updateErrorMsg").text(response.rspMsg);
                    $("#updateErrorMsg").show();
                }
            }
        });
    }
}

function delFavorites(){
    $.ajax({
        async: false,
        type: 'POST',
        dataType: 'json',
        data:"id=" + $("#favoritesId").val(),
        url: '/favorites/del',
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest);
            console.log(textStatus);
            console.log(errorThrown);
        },
        success: function(response){
            locationUrl("/standard/my/0","home");
            loadFavorites();
            loadConfig();
            $("#delFavoritesBtn").attr("aria-hidden","true");
            $("#delFavoritesBtn").attr("data-dismiss","modal");
        }
    });
}

function showNotice(type){
    var temp = $(".label.label-danger").html();
    if(type == "letter"){
        temp = temp - $("#newLetterNoticeCount").val();
        $("#newLetterNotice").html("0 条新消息");
    }else if(type == "praise"){
        temp = temp - $("#newPraiseMeCounts").val();
        $("#praiseMeNewNotice").html("0 条新消息");
    }else if(type == "comment"){
        temp = temp - $("#newCommentMeCount").val();
        $("#commentMeNewNotice").html("0 条新消息");
    }else if(type == "at"){
        temp = temp - $("#newAtMeCount").val();
        $("#atMeNewNotice").html("0 条新消息");
    }
    if(temp==0){
        $(".label.label-danger").hide();
    }else{
        $(".label.label-danger").html(temp);
    }
    if(type == "letter"){
        locationUrl('/letter/letterMe','letterMe');
    }else if(type == "praise"){
        locationUrl('/notice/praiseMe','praiseMe');
    }else if(type == "comment"){
        locationUrl('/notice/commentMe','commentMe');
    }else if(type == "at"){
        locationUrl('/notice/atMe','atMe');
    }

}

function myrefresh(){
    $.ajax({
        url : "/notice/getNoticeNum",
        data : '',
        type : 'POST',
        dataType : "json",
        error : function(XMLHttpRequest, textStatus, errorThrown) {
        },
        success : function(result) {
            if(result.rspCode == '000000'){
                var totalNum = result.data.newPraiseMeCount+result.data.newCommentMeCount+result.data.newAtMeCount+result.data.newLetterNotice;
                if(Number(totalNum) > 0){
                    $("#noticeNum").show();
                    $("#noticeNum").text(totalNum);
                }
                $("#atMeNewNotice").text(result.data.newAtMeCount+" 条新消息");
                $("#newAtMeCount").val(result.data.newAtMeCount);
                $("#commentMeNewNotice").text(result.data.newCommentMeCount+" 条新消息");
                $("#newCommentMeCount").val(result.data.newCommentMeCount);
                $("#praiseMeNewNotice").text(result.data.newPraiseMeCount + " 条新消息");
                $("#newPraiseMeCounts").val(result.data.newPraiseMeCount);
                $("#newLetterNotice").text(result.data.newLetterNotice+" 条新消息");
                $("#newLetterNoticeCount").val(result.data.newLetterNotice);
            }
        }
    });
}
