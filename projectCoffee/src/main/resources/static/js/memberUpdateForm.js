// 오류창
function showAlert() {
    const successMessage = [[${successMessage}]];
    const errorMessage = [[${errorMessage}]];

    if (successMessage) {
        alert(successMessage);
    }
    if (errorMessage) {
        alert(errorMessage);
    }


    // showAlert 함수를 페이지 로딩 시 호출
    $(document).ready(function () {
        showAlert();
    });


    // 회원 탈퇴 확인
    function deleteMember() {
        if (confirm('삭제하시면 복구할수 없습니다. \n 정말로 삭제하시겠습니까?')) {
            window.location.href = "/members/delete/[[${memberUpdateDto.id}]]";
        } else {
            return false;
        }
    }
}