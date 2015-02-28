function showCategory(firsturl){
            $('#MyTree').tree({   
                 checkbox: false,   
                 url:firsturl,   
                 onBeforeExpand:function(node,param){  
                     $('#MyTree').tree('options').url = "/category/getCategorys.java?Id=" + node.id;
                 },               
                onClick:function(node){
                    var state=node.state;
                      if(!state){                                   //判断当前选中的节点是否为根节点
                          currentId=node.id;
                        $("#chooseOk").attr( "disabled" , false );   //如果为根节点 则OK按钮可用
                        }else{
                        $("#chooseOk").attr( "disabled" , true );    //如果不为根节点 则OK按钮不可用
                        }
                    } 
            });
    }