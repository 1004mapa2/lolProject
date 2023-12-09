const url = 'http://localhost:8081';
// const url = 'http://3.34.99.97:8081';
// const clientUrl = 'http://3.34.99.97:3000';
const clientUrl = 'http://localhost:3000';
let dragged;

document.addEventListener("DOMContentLoaded", function () {
    ALLTIER받아오기();
    엑세스토큰검증();
    CHAMPIONNAME받아오기(document.querySelector('.searchInput').value);
})


// leftDiv 시작
function CHAMPIONNAME받아오기(value) {
    fetch(url + '/getChampionNameInfo', {
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
                    var span크기 = item.ChampionKorName.length * 15;
                    var 열 =
                        `<li>
                        <img src="/img/${item.ChampionEngName}.png">
                        <span style="width: ${span크기}px;" class="korNameBox">${item.ChampionKorName}</span>
                        </li>`;
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
        event.target.style.width = '55px';
        event.target.style.height = '55px';
        event.target.style.transition = 'all 0.3s';
        event.target.parentNode.querySelector('span').style.opacity = 1;
    }
})

document.querySelector('main ul').addEventListener('mouseout', function (event) {
    if (event.target.tagName === 'IMG') {
        event.target.style.width = '50px';
        event.target.style.height = '50px';
        event.target.parentNode.querySelector('span').style.opacity = 0;
    }
})

/**
 * 드래그 로직
 */
document.addEventListener("dragstart", function (event) {
    dragged = event.target;
})

document.addEventListener("dragover", function (event) {
    event.preventDefault();
}, false)

document.addEventListener("dragstart", function () {
    document.querySelector('.selectChampion').style.backgroundColor = 'rgba(0, 0, 0, 0.7)';
})

document.addEventListener("dragend", function () {
    document.querySelector('.selectChampion').style.backgroundColor = 'rgba(0, 0, 0, 0)';
})

document.addEventListener("drop", function (event) {
    event.preventDefault();
    if (event.target.classList.contains("com")) {
        var store = event.target.src;
        event.target.src = dragged.src;
        if (dragged.classList.contains("com")) {
            dragged.src = store;
        }
        ALLTIER받아오기();
    }
})
/**
 * 드래그 로직 끝
 */

// leftDiv 끝


// rightDiv
function ALLTIER받아오기() {
    const dataToSend = {};
    var sortValue = document.querySelector('.sortP').value;
    var tierValue = document.querySelector('.tierP').value;

    if (sortValue == '승률 순') {
        sortValue = 'WINRATE';
    } else {
        sortValue = 'PICKCOUNT';
    }
    var championNameData = document.querySelector('.comImg').querySelectorAll('img');
    championNameData.forEach(function (data, i) {
        dataToSend[`championName${i + 1}`] = data.src.replace(clientUrl + '/img/', '').replace('.png', '');
    })
    dataToSend.tier = tierValue;
    dataToSend.sort = sortValue;

    fetch(url + '/getTierInfo', {
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
                        <input type="hidden" value="${item.comsaveId}">
                        <div class="littleSpace"></div>
                        <div class="clickBox"></div>
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
    document.querySelector('.resultComBox').style.opacity = 1;
}

function 조합박스(click_src) {
    var empty = clientUrl + "/img/emptyBox.png";
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
    ALLTIER받아오기();
}

/**
 * 클릭으로 챔피언 빼기
 */
document.querySelector('.comImg').addEventListener('click', function (event) {
    if (event.target.tagName == 'IMG') {
        event.target.src = "/img/emptyBox.png";
    }
    ALLTIER받아오기();
})

/**
 * 랜덤 넣기
 */
document.querySelector('.comButton1').addEventListener('click', function () {
    var empty = clientUrl + "/img/emptyBox.png";
    const list = document.querySelector('.comImg').querySelectorAll('img');
    for (let i = 0; i < list.length; i++) {
        if (list[i].src == empty) {
            list[i].src = "/img/random.png";
            break;
        }
    }
})

/**
 * 조합 박스 비우기
 */
document.querySelector('.comButton2').addEventListener('click', function () {
    const list = document.querySelector('.comImg').querySelectorAll('img');
    for (let i = 0; i < list.length; i++) {
        list[i].src = "/img/emptyBox.png";
    }
    ALLTIER받아오기();
})

/**
 * 픽 횟수 순, 승률 순 정렬
 */
document.querySelector('.sortP').addEventListener('input', function () {
    ALLTIER받아오기();
})

/**
 * 티어 순 정렬
 */
document.querySelector('.tierP').addEventListener('input', function () {
    ALLTIER받아오기();
})

/**
 * 효과 함수
 */
// function 조합효과불러오기() {
//     document.querySelector('.resultComBox').style.opacity = 0;
//     setTimeout(function () {
//         ALLTIER받아오기();
//     }, 500);
// }

document.querySelector('.resultComBox').addEventListener('mouseover', function (event) {

    if (event.target.classList.contains("clickBox")) {
        event.target.parentNode.querySelector('.totalBox').style.backgroundColor = '#8B5FBF';
    }
})

document.querySelector('.resultComBox').addEventListener('mouseout', function (event) {
    if (event.target.classList.contains("clickBox")) {
        event.target.parentNode.querySelector('.totalBox').style.backgroundColor = '#FFFFFF';
    }
})

document.querySelector('.resultComBox').addEventListener('click', function (event) {
    if (event.target.classList.contains("clickBox")) {
        var comsaveId = event.target.parentNode.querySelector('input').value;
        var tier = document.querySelector('.tierP').value;
        var detailURL = "/detail?comsaveId=" + comsaveId + "&tier=" + tier + "&page=1";
        window.location.href = detailURL;
    }
})

// document.querySelector('.test1').addEventListener('click', function () {
//     fetch('http://localhost:8081/api/user', {
//         method: 'GET',
//         headers: {
//             'Authorization': localStorage.getItem('jwtToken')
//         }
//     })
//         .then(response => {
//             alert(response.status);
//         })
// })

// document.querySelector('.test2').addEventListener('click', function () {
//     fetch('http://localhost:8081/api/manager', {
//         method: 'GET',
//         headers: {
//             'Authorization': localStorage.getItem('jwtToken')
//         }
//     })
//         .then(response => {
//             alert(response.status);
//         })
// })

// document.querySelector('.test3').addEventListener('click', function () {
//     fetch('http://localhost:8081/api/admin', {
//         method: 'GET',
//         headers: {
//             'Authorization': localStorage.getItem('jwtToken')
//         }
//     })
//         .then(response => {
//             alert(response.status);
//         })
// })

function 엑세스토큰검증() {
    var jwtToken = localStorage.getItem('jwtToken');

    if (jwtToken != "null" && jwtToken != null) {
        fetch(url + '/api/init', {
            method: 'GET',
            headers: {
                'Authorization': localStorage.getItem('jwtToken'),
            },
            credentials: 'include'
        })
            .then(response => {
                if (response.headers.get('Authorization') != null) {
                    const token = response.headers.get('Authorization');
                    localStorage.setItem('jwtToken', token);
                    document.querySelector('.loginDiv').innerHTML = '로그아웃';
                    document.querySelector('.myPage').style.display = 'block';
                } else {
                    document.querySelector('.loginDiv').innerHTML = '로그아웃';
                    document.querySelector('.myPage').style.display = 'block';
                }
            })
    } else {
        document.querySelector('.loginDiv').innerHTML = '로그인';
    }
}

document.querySelector('.loginDiv').addEventListener('click', function () {
    if (this.innerHTML == '로그아웃') {
        fetch(url + '/api/logout', {
            method: 'GET',
            headers: {
                'Authorization': localStorage.getItem('jwtToken')
            }
        })
        localStorage.removeItem('jwtToken');
        this.href = '/';
    }
})