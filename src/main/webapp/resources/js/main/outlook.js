var realmenudata;

//这里可以定义页面的全局变量
$(document).ready(function() {
		
		layer.load('资源努力加载中......', 1000000);
		var menus;
	    var url=ctx+"/mainmenu";
	    
//	    $.ajax({
//			url: ctx +"/checkWeb",  //请求地址
//			dataType: "text",
//			type: "GET",
//			cache: false,
//			async: true,
//			data: "",
//			success: function(jsonDate) {
//				var i = layer.alert();
//				if(jsonDate="ok"){
//					$.getJSON(url, function(data) {
						//menus=eval("["+JSON.stringify(data)+"]");
	    var data = '[{"menuid":9,"menuname":"电商管理系统","icon":null,"url":null,"type":3,"permission":"commerce","menus":[{"menuid":112,"menuname":".","icon":"","url":"/12","type":0,"permission":"/column","menus":[{"menuid":123,"menuname":"模板管理","icon":"","url":"templet","type":0,"permission":null,"menus":[{"menuid":147,"menuname":"模板套装列表","icon":"","url":"templetList/templetListInfo","type":1,"permission":null,"menus":[]},{"menuid":119,"menuname":"模板列表","icon":"","url":"templet/templetInfo","type":1,"permission":null,"menus":[]}]},{"menuid":115,"menuname":"栏目管理","icon":"","url":"/column/list","type":0,"permission":null,"menus":[{"menuid":117,"menuname":"栏目列表","icon":"","url":"column/columnInfo","type":4,"permission":"column/columnInfo","menus":[]}]},{"menuid":116,"menuname":"商品信息管理","icon":"","url":"goods/goodsinfo1","type":0,"permission":"goods/goodsinfo","menus":[{"menuid":120,"menuname":"商品列表","icon":"","url":"goods/goodsinfo","type":1,"permission":"goods/goodsinfo","menus":[]},{"menuid":121,"menuname":"商品信息发布","icon":"","url":"publishgoods/publishgoods","type":1,"permission":"publishgoods/publishgoods","menus":[]}]},{"menuid":154,"menuname":"文件打包管理","icon":"","url":"publish/publish1","type":0,"permission":"publish/publish","menus":[{"menuid":155,"menuname":"文件打包列表","icon":"","url":"publish/publish","type":1,"permission":"publish/publish","menus":[]}]},{"menuid":122,"menuname":"订单管理","icon":"icon-nav","url":"order/orderinfo1","type":0,"permission":null,"menus":[{"menuid":124,"menuname":"订单列表","icon":"","url":"order/orderinfo","type":1,"permission":"order/orderinfo","menus":[]}]}]}]}]';
						loadmenu(data);
//					});
//					layer.close(i);
//					return;
//				}else{
//					layer.msg('服务器链接失败!');
//					layer.close(i);
//					return;
//				}
//			}
//		});
});




//弹出加载层
function load() { 
    $("<div class=\"datagrid-mask\"></div>").css({ display: "block", width: "100%", height: $(window).height() }).appendTo("body"); 
    $("<div class=\"datagrid-mask-msg\"></div>").html("正在加载，请稍候。。。").appendTo("body").css({ display: "block", left: ($(document.body).outerWidth(true) - 190) / 2, top: ($(window).height() - 45) / 2 }); 
} 
 
//取消加载层  
function disLoad() { 
    $(".datagrid-mask").remove(); 
    $(".datagrid-mask-msg").remove(); 
} 


function loadmenu (_menus) {
	tabClose();
	tabCloseEven();
	
	// 导航菜单绑定初始化
	$("#wnav").accordion( {
		animate : false
	});
	addNewNav(_menus);
	InitLeftMenu(_menus);
}


function Clearnav() {
	var pp = $('#wnav').accordion('panels');
	$.each(pp, function(i, n) {
		if (n) {
			var t = n.panel('options').title;
			$('#wnav').accordion('remove', t);
		}
	});

	pp = $('#wnav').accordion('getSelected');
	if (pp) {
		var title = pp.panel('options').title;
		$('#wnav').accordion('remove', title);
	}
}

function addFirstNav(data) {

	$.each(data, function(i, sm) {
		var menulist = "";
		menulist += '<ul>';
		addli(sm.menuid,sm.menuname);

	});


	$('#css3menu a').click(function() {
		$('#css3menu a').removeClass('active');
		$(this).addClass('active');
		var name=$(this).attr('name');
		var d;
		$.each(data, function(i, sm) {
			if(sm.menuname==name){
				d=sm.menus;
			}

		});
		//var d = realmenudata[d];
		Clearnav();
		addNav(d);
		//addSecondNav(d);
		InitLeftMenu(d);
	});
}
function addli(id,txt) {    
    var css3menuul=$('#css3menu');    
    var li= document.createElement("li");    
    var href_a = document.createElement("a");  
        href_a.href="javascript:;";  
        href_a.innerHTML =txt;  
        href_a.name =txt; 
        href_a.title =txt; 
        li.appendChild(href_a);  
        css3menuul.append(li);    
}  
function addNav(data) {

	$.each(data, function(i, sm) {
		var menulist = "";
		menulist += '<ul>';
		$.each(sm.menus, function(j, o) {
			menulist += ' <li><div><a ref="' + o.menuid + '" href="#" rel="'
			+ctx+ o.url + '" ><span class="icon ' + o.icon
					+ '" >&nbsp;</span><span class="nav">' + o.menuname
					+ '</span></a></div></li>';
		});
		menulist += '</ul>';
		$('#wnav').accordion('add', {
			title : sm.menuname,
			content : menulist,
			iconCls : 'icon ' + sm.icon
		});

	});

	var pp = $('#wnav').accordion('panels');
	var t = pp[0].panel('options').title;
	$('#wnav').accordion('select', t);

}

function addSecondNav(data) {

	
		var menulist = "";
		menulist += '<ul>';
		$.each(data.menus, function(j, o) {
			menulist += ' <li><div><a ref="' + o.menuid + '" href="#" rel="'
					+ctx+ o.url + '" ><span class="icon ' + o.icon
					+ '" >&nbsp;</span><span class="nav">' + o.menuname
					+ '</span></a></div></li>';
		});
		menulist += '</ul>';
		$('#wnav').accordion('add', {
			title : data.menuname,
			content : menulist,
			iconCls : 'icon ' + data.icon
		});

	

	var pp = $('#wnav').accordion('panels');
	var t = pp[0].panel('options').title;
	$('#wnav').accordion('select', t);

}
function addrootNav(data) {

	$.each(data, function(i, sm) {
		alert(sm.menuid);
		$('west').title=sm.menuname;

	});

}


function addNewNav(data) {
	var menudata;
	$.each(data, function(k, menu) {
		menudata=menu.menus;
	});
	realmenudata=menudata;
	//addNav(menudata);
	addFirstNav(menudata);
	var d;
	var firstMenuName = $('#css3menu a:first').attr('name');
	 $('#css3menu a:first').addClass('active');
	$.each(menudata, function(i, sm) {
		if(sm.menuname==firstMenuName){
			d=sm.menus;
		}

	});
	addNav(d);
}

// 初始化左侧
function InitLeftMenu(_menus) {
	hoverMenuItem();

	$('#wnav li a').on('click', function() {

		var tabTitle = $(this).children('.nav').text();
		
		var url = $(this).attr("rel");
		
		var menuid = $(this).attr("ref");
		
		var icon = getIcon(menuid,_menus);
	
		addTab(tabTitle, url, icon);
		$('#wnav li div').removeClass("selected");
		$(this).parent().addClass("selected");
	});

}

/**
 * 菜单项鼠标Hover
 */
function hoverMenuItem() {
	$(".easyui-accordion").find('a').hover(function() {
		$(this).parent().addClass("hover");
	}, function() {
		$(this).parent().removeClass("hover");
	});
}

// 获取左侧导航的图标
function getIcon(menuid,_menus) {
	var icon = 'icon ';
	$.each(_menus, function(i, n) {
		$.each(n.menus, function(j, o) {
			$.each(o.menus, function(k, m){
				if (m.menuid == menuid) {
					icon += m.icon;
					return false;
				}
			});
		});
	});
	return icon;
}

function addTab(subtitle, url, icon) {
	if (!$('#tabs').tabs('exists', subtitle)) {
		$('#tabs').tabs('add', {
			title : subtitle,
			content : createFrame(url),
			closable : true,
			icon : icon
		});
	} else {
		$('#tabs').tabs('select', subtitle);
		$('#mm-tabupdate').click();
	}
	tabClose();
}

function createFrame(url) {
	var s = '<iframe scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:100%;"></iframe>';
	return s;
}

function tabClose() {
	/* 双击关闭TAB选项卡 */
	$(".tabs-inner").dblclick(function() {
		var subtitle = $(this).children(".tabs-closable").text();
		$('#tabs').tabs('close', subtitle);
	});
	/* 为选项卡绑定右键 */
	$(".tabs-inner").bind('contextmenu', function(e) {
		$('#mm').menu('show', {
			left : e.pageX,
			top : e.pageY
		});

		var subtitle = $(this).children(".tabs-closable").text();

		$('#mm').data("currtab", subtitle);
		$('#tabs').tabs('select', subtitle);
		return false;
	});
}
// 绑定右键菜单事件
function tabCloseEven() {
	// 刷新
	$('#mm-tabupdate').click(function() {
		var currTab = $('#tabs').tabs('getSelected');
		var url = $(currTab.panel('options').content).attr('src');
		$('#tabs').tabs('update', {
			tab : currTab,
			options : {
				content : createFrame(url)
			}
		});
	});
	// 关闭当前
	$('#mm-tabclose').click(function() {
		var currtab_title = $('#mm').data("currtab");
		$('#tabs').tabs('close', currtab_title);
	});
	// 全部关闭
	$('#mm-tabcloseall').click(function() {
		$('.tabs-inner span').each(function(i, n) {
			var t = $(n).text();
			$('#tabs').tabs('close', t);
		});
	});
	// 关闭除当前之外的TAB
	$('#mm-tabcloseother').click(function() {
		$('#mm-tabcloseright').click();
		$('#mm-tabcloseleft').click();
	});
	// 关闭当前右侧的TAB
	$('#mm-tabcloseright').click(function() {
		var nextall = $('.tabs-selected').nextAll();
		if (nextall.length == 0) {
			// msgShow('系统提示','后边没有啦~~','error');
			alert('后边没有啦~~');
			return false;
		}
		nextall.each(function(i, n) {
			var t = $('a:eq(0) span', $(n)).text();
			$('#tabs').tabs('close', t);
		});
		return false;
	});
	// 关闭当前左侧的TAB
	$('#mm-tabcloseleft').click(function() {
		var prevall = $('.tabs-selected').prevAll();
		if (prevall.length == 0) {
			alert('到头了，前边没有啦~~');
			return false;
		}
		prevall.each(function(i, n) {
			var t = $('a:eq(0) span', $(n)).text();
			$('#tabs').tabs('close', t);
		});
		return false;
	});

	// 退出
	$("#mm-exit").click(function() {
		$('#mm').menu('hide');
	});
}

// 弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
function msgShow(title, msgString, msgType) {
	$.messager.alert(title, msgString, msgType);
}

// 本地时钟
function clockon() {
	var now = new Date();
	var year = now.getFullYear(); // getFullYear getYear
	var month = now.getMonth();
	var date = now.getDate();
	var day = now.getDay();
	var hour = now.getHours();
	var minu = now.getMinutes();
	var sec = now.getSeconds();
	var week;
	month = month + 1;
	if (month < 10)
		month = "0" + month;
	if (date < 10)
		date = "0" + date;
	if (hour < 10)
		hour = "0" + hour;
	if (minu < 10)
		minu = "0" + minu;
	if (sec < 10)
		sec = "0" + sec;
	var arr_week = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
	week = arr_week[day];
	var time = "";
	time = year + "年" + month + "月" + date + "日" + " " + hour + ":" + minu
			+ ":" + sec + " " + week;

	$("#bgclock").html(time);

	var timer = setTimeout("clockon()", 200);
}


