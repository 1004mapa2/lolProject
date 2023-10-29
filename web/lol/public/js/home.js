document.addEventListener("DOMContentLoaded", function () {
    document.querySelector('#tab1').style.color = '#DA81F5';
    ALLTIER받아오기();
    CHAMPIONNAME받아오기(document.querySelector('.searchInput').value);
})

function CHAMPIONNAME받아오기(value) {
    fetch('http://localhost:8081/2', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(value)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('http 오류: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            document.querySelector('main ul').innerHTML = "";
            if (data.length == 0) {
                var 없음 =
                    `<div>그런 챔프는 없어..<div>`;
                document.querySelector('main ul').insertAdjacentHTML('beforeend', 없음);
            } else {
                data.forEach(function (item) {
                    var 열 =
                        `<li><img src="/img/${item.ChampionEngName}.png"></li>`;
                    document.querySelector('main ul').insertAdjacentHTML('beforeend', 열);
                })
            }
        })
}

document.querySelector('.searchInput').addEventListener('input', function (e) {
    CHAMPIONNAME받아오기(this.value);

})

document.querySelector('main ul').addEventListener('click', function (event) {
    if (event.target.tagName === 'IMG') {
        조합박스(event.target.src);
    }
})

document.querySelector('main ul').addEventListener('mouseover', function (event) {
    if (event.target.tagName === 'IMG') {
        event.target.style.width = '70px';
        event.target.style.height = '70px';
        event.target.style.transition = 'all 0.3s';
    }
})

document.querySelector('main ul').addEventListener('mouseout', function (event) {
    if (event.target.tagName === 'IMG') {
        event.target.style.width = '50px';
        event.target.style.height = '50px';
    }
})



// rightDiv
document.querySelector('.sortP').addEventListener('input', function () {
    ALLTIER받아오기();
})

document.querySelector('.tierP').addEventListener('input', function () {
    ALLTIER받아오기();
})

function ALLTIER받아오기() {
    const dataToSend = {};
    sortValue = document.querySelector('.sortP').value;
    tierValue = document.querySelector('.tierP').value;

    if (sortValue == '승률 순') {
        sortValue = 'WINRATE';
    } else {
        sortValue = 'PICKCOUNT';
    }
    var championNameData = document.querySelector('.comImg').querySelectorAll('img');
    championNameData.forEach(function (data, i) {
        dataToSend[`championName${i + 1}`] = data.src.replace('http://localhost:3000/img/', '').replace('.png', '');
    })
    dataToSend.tier = tierValue;
    dataToSend.sort = sortValue;

    fetch('http://localhost:8081/1', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(dataToSend)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('http 오류: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            if (document.querySelector('.resultComBox').innerHTML != "") {
                document.querySelector('.resultComBox').innerHTML = "";
            }
            data.forEach(function (item) {
                var 열 =
                    `<div class="resultCom">
                        <div class="littleSpace"></div>
                        <div class="totalBox">
                            <div class="championComBox">
                                <img src="/img/${item.topName}.png">
                                <img src="/img/${item.jungleName}.png">
                                <img src="/img/${item.middleName}.png">
                                <img src="/img/${item.bottomName}.png">
                                <img src="/img/${item.utilityName}.png">
                            </div>
                            <div class="definitionBox">
                                <a class="winRate">${item.winRate}</a>
                                <a class="pickCount">${item.pickCount}</a>
                            </div>
                        </div>
                        <div class="littleSpace"></div>
                    </div>`;
                document.querySelector('.resultComBox').insertAdjacentHTML('beforeend', 열);
            });
        })
}

function 조합박스(click_src) {
    var empty = "http://localhost:3000/img/emptyBox.png";
    const list = document.querySelector('.comImg').querySelectorAll('img');

    for (let i = 0; i < list.length; i++) {
        if (list[0].src != click_src && list[1].src != click_src &&
            list[2].src != click_src && list[3].src != click_src && list[4].src != click_src) {
            if (list[i].src == empty) {
                list[i].src = click_src;
                break;
            }
        }
    }

    if (list[list.length - 1].src != empty) {
        ALLTIER받아오기();
    }
}


function 빼기(id) {
    document.getElementById(id).src = "/img/emptyBox.png";
}


function 랜덤() {
    var empty = "http://localhost:3000/img/emptyBox.png";
    const list = document.querySelector('.comImg').querySelectorAll('img');
    for (let i = 0; i < list.length; i++) {
        if (list[i].src == empty) {
            list[i].src = "/img/random.png";
            break;
        }
    }

    if (list[list.length - 1].src != empty) {
        ALLTIER받아오기();
    }
}


function 비우기() {
    const list = document.querySelector('.comImg').querySelectorAll('img');
    for (let i = 0; i < list.length; i++) {
        list[i].src = "/img/emptyBox.png";
    }
    ALLTIER받아오기();
}