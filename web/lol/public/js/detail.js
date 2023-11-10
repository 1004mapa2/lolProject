document.addEventListener("DOMContentLoaded", function () {
    document.querySelector('#tab1').style.color = '#DA81F5';
    파라미터보내기();
})



function 파라미터보내기() {
    const urlParams = new URLSearchParams(window.location.search);
    comsaveId = urlParams.get('comsaveId');
    tier = urlParams.get('tier');
    fetch(`http://localhost:8081/getDetailInfo?comsaveId=${comsaveId}&tier=${tier}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('http 오류: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            var a = document.querySelector('.championComBox').querySelectorAll('img');
            a[0].src = "/img/" + data[0].topName + ".png";
            a[1].src = "/img/" + data[0].jungleName + ".png";
            a[2].src = "/img/" + data[0].middleName + ".png";
            a[3].src = "/img/" + data[0].bottomName + ".png";
            a[4].src = "/img/" + data[0].utilityName + ".png";
            var b = document.querySelector('.definitionBox').querySelectorAll('a');
            b[0].innerHTML = data[0].winRate;
            b[1].innerHTML = data[0].pickCount;

            for (let i = 1; i < data.length; i++) {
                var 열 =
                    `<div class="loseComDiv">
                        <div class="championComBox">
                            <img src="/img/${data[i].topName}.png">
                            <img src="/img/${data[i].jungleName}.png">
                            <img src="/img/${data[i].middleName}.png">
                            <img src="/img/${data[i].bottomName}.png">
                            <img src="/img/${data[i].utilityName}.png">
                        </div>
                        <div class="definitionBox">
                            <a>${data[i].loseCount}</a>
                        </div>
                    </div>`
                document.querySelector('.loseComListDiv').insertAdjacentHTML('beforeend', 열);
            }
        })
}