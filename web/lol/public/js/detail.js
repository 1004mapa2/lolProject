// const url = 'http://localhost:8081';
const url = 'http://3.34.99.97:8081';
document.addEventListener("DOMContentLoaded", function () {
    엑세스토큰검증();
    댓글불러오기();
    파라미터보내기();
})

async function 엑세스토큰검증() {
    var jwtToken = localStorage.getItem('jwtToken');

    if (jwtToken != "null" && jwtToken != null) {
        await fetch(url + '/api/init', {
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
                } else {
                    document.querySelector('.loginDiv').innerHTML = '로그아웃';
                }
            })
    } else {
        document.querySelector('.loginDiv').innerHTML = '로그인';
    }
}



document.querySelector('.loginDiv').addEventListener('click', function () {
    if (this.innerHTML == '로그인') {
        window.location.href = "/login";
    } else {
        fetch(url + '/api/logout', {
            method: 'GET',
            headers: {
                'Authorization': localStorage.getItem('jwtToken')
            }
        })
        localStorage.removeItem('jwtToken');
        location.reload();
    }
})

function 파라미터보내기() {
    const urlParams = new URLSearchParams(window.location.search);
    var comsaveId = urlParams.get('comsaveId');
    var tier = urlParams.get('tier');
    fetch(url + `/getDetailInfo?comsaveId=${comsaveId}&tier=${tier}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('http 오류: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            var 선택조합 =
                `
                <div class="comImgDiv">
                    <div>
                        <div class="iconImgDiv">
                            <img class="iconImg" src="/img/positionImg/topIcon.png"/>
                        </div>
                        <img class="pickImg" src="/img/${data.topName}.png"/>
                        <p class="championNameP">${data.championKorNames[0]}</p>
                    </div>
                    <div>
                        <div class="iconImgDiv">
                            <img class="iconImg" src="/img/positionImg/jungleIcon.png"/>
                        </div>
                        <img class="pickImg" src="/img/${data.jungleName}.png"/>
                        <p class="championNameP">${data.championKorNames[1]}</p>
                    </div>
                    <div>
                        <div class="iconImgDiv">
                            <img class="iconImg" src="/img/positionImg/middleIcon.png"/>
                        </div>
                        <img class="pickImg" src="/img/${data.middleName}.png"/>
                        <p class="championNameP">${data.championKorNames[2]}</p>
                    </div>
                    <div>
                        <div class="iconImgDiv">
                            <img class="iconImg" src="/img/positionImg/bottomIcon.png"/>
                        </div>
                        <img class="pickImg" src="/img/${data.bottomName}.png"/>
                        <p class="championNameP">${data.championKorNames[3]}</p>
                    </div>
                    <div>
                        <div class="iconImgDiv">
                            <img class="iconImg" src="/img/positionImg/utilityIcon.png"/>
                        </div>
                        <img class="pickImg" src="/img/${data.utilityName}.png"/>
                        <p class="championNameP">${data.championKorNames[4]}</p>
                    </div>
                </div>
                `
            document.querySelector('.pickComBox').insertAdjacentHTML('afterbegin', 선택조합);

            document.querySelector('.pickCountBox').innerHTML = data.pickCount;
            document.querySelector('.winRateBox').innerHTML = data.winRate + '%';
        })
}

document.querySelector('.commentButton').addEventListener('click', async function () {
    await 엑세스토큰검증();
    await 댓글저장();
    await 댓글불러오기();
    document.querySelector('.commentText').value = '';
})

async function 댓글저장() {
    const urlParams = new URLSearchParams(window.location.search);
    const dataToSend = {
        content: document.querySelector('.commentText').value,
        comsaveId: urlParams.get('comsaveId')
    }
    await fetch(url + '/saveComment', {
        method: 'POST',
        headers: {
            'Authorization': localStorage.getItem('jwtToken'),
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dataToSend)
    })
        .then(response => {
            if (response.status == 401) {
                alert('로그인 하세요');
            }
        })
}

async function 댓글불러오기() {
    const urlParams = new URLSearchParams(window.location.search);
    const dataToSend = {
        comsaveId: urlParams.get('comsaveId'),
        page: urlParams.get('page')
    }
    await fetch(url + '/getComment', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dataToSend)
    })
        .then(response => {

            return response.json();
        })
        .then(data => {
            document.querySelector('.commentContainer').innerHTML = "";
            document.querySelector('.pageNumberDiv').innerHTML = "";
            if (data.page == 0) {
                var 메시지 =
                `
                <div>
                    댓글이 없습니다.
                </div>
                `
                document.querySelector('.commentContainer').insertAdjacentHTML('beforeend', 메시지);
            } else {
                for (let i = 1; i <= data.page; i++) {
                    var 페이지갯수 =
                        `
                        <a href="/detail?comsaveId=${data.comsaveId}&tier=ALLTIER&page=${i}">${i}</a>
                        `
                    document.querySelector('.pageNumberDiv').insertAdjacentHTML('beforeend', 페이지갯수);
                }
                for (let i = 0; i < data.commentList.length; i++) {
                    var 게시글 =
                        `
                        <div class="commentDetail">
                            <div style="color: #848484">${data.commentList[i].username}</div>
                            <div>${data.commentList[i].content}</div>
                            <div style="margin-top: 20px;">${data.commentList[i].writeTime}</div>
                        </div>
                        <hr/>
                        `
                    document.querySelector('.commentContainer').insertAdjacentHTML('beforeend', 게시글);
                }
            }
        })
}

document.querySelectorAll('.tierDiv').forEach(function (element) {
    element.addEventListener('mouseover', function () {
        this.classList.add('backColor');
    })
})

document.querySelectorAll('.tierDiv').forEach(function (element) {
    element.addEventListener('mouseout', function () {
        this.classList.remove('backColor');
    })
})

document.querySelector('.allTierDiv').addEventListener('mouseover', function () {
    this.classList.add('backColor');
})

document.querySelector('.allTierDiv').addEventListener('mouseout', function () {
    this.classList.remove('backColor');
})

document.querySelectorAll('.tierDiv').forEach(function (element) {
    const urlParams = new URLSearchParams(window.location.search);
    var comsaveId = urlParams.get('comsaveId');
    var tier = "";
    element.addEventListener('click', function () {
        if (this.querySelector('p').innerHTML == '챌린저') {
            tier = "CHALLENGER";
        } else if (this.querySelector('p').innerHTML == '그랜드마스터') {
            tier = "GRANDMASTER";
        } else if (this.querySelector('p').innerHTML == '마스터') {
            tier = "MASTER";
        } else if (this.querySelector('p').innerHTML == '다이아몬드') {
            tier = "DIAMOND";
        }
        티어바꾸기(comsaveId, tier);
    })
})

document.querySelector('.allTierDiv').addEventListener('click', function () {
    const urlParams = new URLSearchParams(window.location.search);
    var comsaveId = urlParams.get('comsaveId');
    var tier = "ALLTIER";
    티어바꾸기(comsaveId, tier);
})

function 티어바꾸기(comsaveIdValue, tierValue) {
    var dataToSend = {
        comsaveId: comsaveIdValue,
        tier: tierValue
    }
    fetch(url + '/getDetailInfoDynamic', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
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
            document.querySelector('.pickCountBox').innerHTML = data.pickCount;
            document.querySelector('.winRateBox').innerHTML = data.winRate + '%';
        })
}