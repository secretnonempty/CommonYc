var sysResource;
$.extend($.fn.validatebox.defaults.rules, {

	nochange: {
        validator: function (value, param) {
            return false;
        },
        message: '该信息不可编辑'
    },
    CHS: {
        validator: function (value, param) {
            return /^[\u0391-\uFFE5]+$/.test(value);
        },
        message: '请输入汉字'
    },
    character: {
        validator: function (value, param) {
            return /^[a-zA-Z]+$/.test(value);
        },
        message:'请输入字母'
    },
    number: {
        validator: function (value, param) {
            return /^\d+$/.test(value);
        },
        message: '请输入数字'
    },
    CHSAndcharacter: {
        validator: function (value, param) {
            return /^[\u0391-\uFFE5a-zA-Z]+$/.test(value);
        },
        message: '请输入字母或者汉字'
    },
    numberAndcharacter: {
        validator: function (value, param) {
            return /^[a-zA-Z0-9]+$/.test(value);
        },
        message: '请输入数字或者字母'
    },
    mobile: {
        validator: function (value, param) {
            return /^\d{0,15}$/.test(value);
        },
        message: '手机号码不正确'
    },
    loginName: {
        validator: function (value, param) {
            var zz= /^[a-zA-Z0-9][\w]{0,}$/.test(value);
            var length=value.length <= param[0];
            return zz&&length;
        },
        message: '登录名称只能由字母、数字开头的{0}位以内的数字、字母、下划线组成。'
    },
    password: {
        validator: function (value, param) {
            return /^[a-zA-Z0-9]{8}$/.test(value);
        },
        message: '密码由8位字母和数字组成。'
    },
    equalTo: {
        validator: function (value, param) {
            return value == $(param[0]).val();
        },
        message: '两次输入的字符不一至'
    },
    code: {
        validator: function (value, param) {
            var zz= /^[a-zA-Z0-9]{0,}$/.test(value);
            var length=value.length <= param[0];
            return zz&&length;
        },
        message: '编码只能由{0}位以内的数字、字母组成。'
    },
    maxlength:{
    	validator: function (value, param) {
    	 var length=value.length <= param[0];
    	 return length;
    },
    message:'输入必须小于{0}个字符.'
    },
    minlength:{
    	validator: function (value, param) {
    	 var length=value.length >= param[0];
    	 return length;
    },
    message:'输入必须大于{0}个字符.'
    },
});

//
function addLoaderEvent(func){
	var oldonload=window.onload;
	if(typeof window.onload!='function'){
	window.onload=func;
	}else{
	window.onload=function(){
	oldonload();
	func();
	}
	}
	}
