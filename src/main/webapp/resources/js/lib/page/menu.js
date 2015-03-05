var _menus = {
	basic : [ {
		"menuid" : "90",
		"icon" : "icon-sys",
		"menuname" : "学生管理",
		"menus" : [ {
			"menuid" : "911",
			"menuname" : "学生列表",
			"icon" : "icon-nav",
			"url" : "student/studentinfo"
		}, {
			"menuid" : "912",
			"menuname" : "预约管理",
			"icon" : "icon-nav",
			"url" : "book/booklist"
		}]
	}, {
		"menuid" : "10",
		"icon" : "icon-sys",
		"menuname" : "栏目管理",
		"menus" : [ {
			"menuid" : "111",
			"menuname" : "栏目列表",
			"icon" : "icon-nav",
			"url" : "column/columnInfo"
		}]
	}, {
		"menuid" : "20",
		"icon" : "icon-sys",
		"menuname" : "模板管理",
		"menus" : [ {
			"menuid" : "211",
			"menuname" : "模板列表",
			"icon" : "icon-nav",
			"url" : "templet/templetInfo"
		}]
	}, {
		"menuid" : "20",
		"icon" : "icon-sys",
		"menuname" : "商品信息管理",
		"menus" : [ {
			"menuid" : "211",
			"menuname" : "商品列表",
			"icon" : "icon-nav",
			"url" : "goods/goodsinfo"
		},{
			"menuid" : "211",
			"menuname" : "商品信息发布",
			"icon" : "icon-nav",
			"url" : "publishgoods/publishgoods"
		} ]
	} , {
		"menuid" : "40",
		"icon" : "icon-sys",
		"menuname" : "订单管理",
		"menus" : [ {
			"menuid" : "411",
			"menuname" : "订单列表",
			"icon" : "icon-nav",
			"url" : "order/orderinfo"
		} ]
	}, {
		"menuid" : "40",
		"icon" : "icon-sys",
		"menuname" : "文件打包管理",
		"menus" : [ {
			"menuid" : "411",
			"menuname" : "文件包列表",
			"icon" : "icon-nav",
			"url" : "publish/publish"
		} ]
	}   ],
	point : [{
		"menuid" : "30",
		"icon" : "icon-sys",
		"menuname" : "资产管理",
		"menus" : [ {
			"menuid" : "211",
			"menuname" : "设备管理",
			"icon" : "icon-nav",
			"url" : "#"
		}, {
			"menuid" : "213",
			"menuname" : "车次管理",
			"icon" : "icon-nav",
			"url" : "#"
		} ]

	}]
};
        //设置登录窗口
        function openPwd() {
            $('#w').window({
                title: '修改密码',
                width: 300,
                modal: true,
                shadow: true,
                closed: true,
                height: 160,
                resizable:false
            });
        }
        //关闭登录窗口
        function closePwd() {
            $('#w').window('close');
        }

        

        //修改密码
        function serverLogin() {
            var $newpass = $('#txtNewPass');
            var $rePass = $('#txtRePass');

            if ($newpass.val() == '') {
                msgShow('系统提示', '请输入密码！', 'warning');
                return false;
            }
            if ($rePass.val() == '') {
                msgShow('系统提示', '请在一次输入密码！', 'warning');
                return false;
            }

            if ($newpass.val() != $rePass.val()) {
                msgShow('系统提示', '两次密码不一至！请重新输入', 'warning');
                return false;
            }

            $.post('/ajax/editpassword.ashx?newpass=' + $newpass.val(), function(msg) {
                msgShow('系统提示', '恭喜，密码修改成功！<br>您的新密码为：' + msg, 'info');
                $newpass.val('');
                $rePass.val('');
                close();
            })
            
        }

        $(function() {

            openPwd();

            $('#editpass').click(function() {
                $('#w').window('open');
            });

            $('#btnEp').click(function() {
                serverLogin();
            })

			$('#btnCancel').click(function(){closePwd();})

            $('#loginOut').click(function() {
                $.messager.confirm('系统提示', '您确定要退出本次登录吗?', function(r) {

                    if (r) {
                        location.href = '/ajax/loginout.ashx';
                    }
                });
            })
        });
