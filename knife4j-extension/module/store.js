(function(){
   var Store={
       set:function(key,data){
           var localSto=window.localStorage;
           if(localSto){
               var strdata="";
               if(typeof(data)=='string'){
                   strdata=data;
               }else if(typeof(data)=="object"){
                   strdata=JSON.stringify(data);
               }
               localSto.setItem(key,strdata);
           }
       },
       get:function(key){
            var localSto=window.localStorage;
            if(localSto){
                var s=localSto.getItem(key);
                return s;
            }
            return null;
       },
       getJson:function(key){
            var localSto=window.localStorage;
            if(localSto){
                var s=localSto.getItem(key);
                return JSON.parse(s);
            }
            return null;
       }
   }
   window.Store=Store;
})()