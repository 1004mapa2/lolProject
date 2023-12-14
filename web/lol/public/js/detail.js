const url = 'http://13.124.127.226:8081';
// const url = 'http://localhost:8081';

document.addEventListener("DOMContentLoaded", async function () {
    await 엑세스토큰검증();
    댓글불러오기();
    조합정보가져오기();
})

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
    const comsaveId = urlParams.get('comsaveId');
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

document.querySelector('.commentButton').addEventListener('click', async function () {
    await 엑세스토큰검증();
    await 댓글저장();
    댓글불러오기();
})

document.querySelector('.commentContainer').addEventListener('click', async function (event) {
    await 엑세스토큰검증();
    댓글삭제(event);
})

/**
 * 함수 시작
 */
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
                    jwtToken = response.headers.get('Authorization');
                    localStorage.setItem('jwtToken', jwtToken);
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

function 조합정보가져오기() {
    fetch(url + "/getDetailInfo" + window.location.search)
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
            const winRate = Math.round(data.winRate * 100);
            document.querySelector('.winRateBox').innerHTML = winRate + '%';
        })
}

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
            document.querySelector('.commentText').value = '';
            if (response.status == 401) {
                alert('로그인 하세요');
            }
        })
}

async function 댓글불러오기() {
    await fetch(url + '/getComment' + window.location.search, {
        method: 'GET'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('http 오류: ' + response.status);
            }

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
                            <div style="color: #848484" class="username">${data.commentList[i].username}</div>
                            <div>${data.commentList[i].content}</div>
                            <div class="commentDateDiv">
                                <p>${data.commentList[i].writeTime}</p>
                                <p class="commentDeleteP">삭제</p>
                            </div>
                            <input type="hidden" value="${data.commentList[i].id}"/>
                        </div>
                        <hr/>
                        `
                    document.querySelector('.commentContainer').insertAdjacentHTML('beforeend', 게시글);
                }
            }
        })
    if(document.querySelector('.loginDiv').innerHTML == '로그아웃') {
        권한체크();
    }
}

function 티어바꾸기(comsaveIdValue, tierValue) {
    fetch(url + '/getDetailInfoDynamic' + "?comsaveId=" + comsaveIdValue + "&tier=" + tierValue, {
        method: 'GET'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('http 오류: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            document.querySelector('.pickCountBox').innerHTML = data.pickCount;
            const winRate = Math.round(data.winRate * 100);
            document.querySelector('.winRateBox').innerHTML = winRate + '%';
        })
}

function 권한체크() {
    fetch(url + '/checkUser', {
        method: 'GET',
        headers: {
            'Authorization': localStorage.getItem('jwtToken')
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('http 오류: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            document.querySelectorAll('.commentDetail').forEach(function (item) {
                if(data.role == 'ROLE_ADMIN') {
                    item.querySelector('.commentDeleteP').style.display = 'block';
                } else if(item.querySelector('.username').innerHTML == data.username) {
                    item.querySelector('.commentDeleteP').style.display = 'block';
                }
            })
        })
}

function 댓글삭제(event) {
    if (event.target.classList.contains('commentDeleteP')) {
        const commentId = event.target.parentNode.parentNode.querySelector('input').value;
        fetch(url + '/deleteComment/' + commentId, {
            method: 'DELETE',
            headers: {
                'Authorization': localStorage.getItem('jwtToken')
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('http 오류: ' + response.status);
                } else {
                    댓글불러오기();
                }
            })
    }
}