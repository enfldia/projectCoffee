// sec2 slide
let slideWrap = $(".slide_wrap"),
    slideShow = slideWrap.find(".slide_show"),
    slideList = slideShow.find(".sec2list"),
    slides = slideList.find(".slideimg"), // 변수명을 slideimg로 변경
    slideBtn = slideWrap.find(".slide_btn");

let slideCount = slides.length,
    slideWidth = slides.innerWidth(),
    showNum = 3,
    num = 0,
    currentIndex = 0,

    slideCopy = slides.slice(0, showNum).clone();
slideList.append(slideCopy);

// 이미지 움직이기
function backShow() {
    if (num === 0) {
        // 시작
        num = slideCount;
        slideList.css("left", -num * slideWidth + "px");
    }
    num--;
    slideList.stop().animate({ left: -slideWidth * num + "px" }, 400);
}

function nextShow() {
    if (num === slideCount) {
        // 마지막
        num = 0;
        slideList.css("left", num);
    }
    num++;
    slideList.stop().animate({ left: -slideWidth * num + "px" }, 400);
}

// 왼쪽, 오른쪽 버튼 설정
slideBtn.on("click", "button", function () {
    if ($(this).hasClass("prev")) {
        // 왼쪽 버튼을 클릭
        backShow();
    } else {
        // 오른쪽 버튼을 클릭
        nextShow();
    }
});



// 스크롤모션
AOS.init({
    duration: 1200,
})


//아코디언 메뉴
$(".que").click(function() {
    $(this).next(".anw").stop().slideToggle(300);
    $(this).toggleClass('on').siblings().removeClass('on');
    $(this).next(".anw").siblings(".anw").slideUp(300); // 1개씩 펼치기
});