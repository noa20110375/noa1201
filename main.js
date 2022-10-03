const toggleBtn = document.querySelector('.navbar__toggleBtn');
const menu = document.querySelector('.navbar__menu');

// 클릭이 될 때 마다 지정한 함수를 호출
// 마우스를 클릭 했을 때 액티브가 있으면 추가 없으면 삭제
toggleBtn.addEventListener('click',() => {
    menu.classList.toggle('active');
    console.log("실행");
    
});