var index=0;//每张图片的下标，

window.onload=function(){
    var start=setInterval(autoPlay,1000);//开始轮播，每秒换一张图

    $('imgchangediv').onmouseover=function(){//当鼠标光标停在图片上，则停止轮播
        clearInterval(start);
    }
    $('imgchangediv').onmouseout=function(){//当鼠标光标停在图片上，则开始轮播
        start=setInterval(autoPlay,1000);
    }

    var lis=$('imgyuan').getElementsByTagName('font');//得到所有圆圈
    //当移动到圆圈，则停滞对应的图片
    var funny = function(i){
        lis[i].onmouseover = function(){
            changeImg(i)
        }
    }
    for(var i=0;i<lis.length;i++){
        funny(i);
    }
}
//一轮过后，还是第二轮
function autoPlay(){
    if(index>7){
        index=0;
    }
    changeImg(index++);
}
//对应圆圈和图片同步
function changeImg(index) {
    var list = $('imgchangediv').getElementsByTagName('img');
    var list1 = $('imgyuan').getElementsByTagName('font');
    for (i = 0; i < list.length; i++) {
        list[i].style.display = 'none';
        list1[i].style.backgroundColor = 'white';
    }
    list[index].style.display = 'block';
    list1[index].style.backgroundColor = 'red';
}