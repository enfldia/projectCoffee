	$(function(){ //html /css 준비가 끝나면 실행해라

            $(".faqTab>li").click(function(){
             var i  = $(this).index();
                console.log(i);
                $(".faqTab>li").removeClass('on');
                $(".faqTab>li").eq(i).addClass('on');

               $(".faqCon dl").css('display','none');
               $(".faqCon dl").eq(i).css('display','block');
                return false;//링크금지
            });
            $(".faqTab > li").click(function() {
              var $clickedLi = $(this);

              // 다른 li 요소에서 active 클래스 제거
              $(".faqTab > li").not($clickedLi).removeClass("active");

              // 클릭한 li 요소에 active 클래스 추가
              $clickedLi.addClass("active");

              return false;
            });


        $('.con1 dt').click(function() {
          var kkk = $(this).next('dd').css('display');

          if (kkk === 'none') {
            $('dd').slideUp(400);
            $(this).next('dd').slideDown(400);
            $('dl dt').removeClass('on');
            $(this).addClass('on');
          } else {
            $(this).next('dd').slideUp(400);
            $(this).removeClass('on');
          }
        });
        $('.con2 dt').click(function() {
                  var ddd = $(this).next('dd').css('display');

                  if (ddd === 'none') {
                    $('dd').slideUp(400);
                    $(this).next('dd').slideDown(400);
                    $('dl dt').removeClass('on');
                    $(this).addClass('on');
                  } else {
                    $(this).next('dd').slideUp(400);
                    $(this).removeClass('on');
                  }
                });
        $('.con3 dt').click(function() {
                  var eee = $(this).next('dd').css('display');

                  if (eee === 'none') {
                    $('dd').slideUp(400);
                    $(this).next('dd').slideDown(400);
                    $('dl dt').removeClass('on');
                    $(this).addClass('on');
                  } else {
                    $(this).next('dd').slideUp(400);
                    $(this).removeClass('on');
                  }
                });
        });