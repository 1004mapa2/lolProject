document.addEventListener("DOMContentLoaded", function () {
    파라미터보내기();
})



function 파라미터보내기() {
    const urlParams = new URLSearchParams(window.location.search);
    comsaveId = urlParams.get('comsaveId');
    tier = urlParams.get('tier');
    fetch(`http://localhost:8081/3?comsaveId=${comsaveId}&tier=${tier}`)
    .then(response => {
        if (!response.ok) {
            throw new Error('http 오류: ' + response.status);
        }
        return response.json();
    })
    .then(data => {
        console.log(data);
    })
}