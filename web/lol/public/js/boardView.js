const url = 'http://54.180.81.104:8081';

document.addEventListener("DOMContentLoaded", async function () {
    await 엑세스토큰검증();
    게시글불러오기();
    좋아요불러오기();
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

document.querySelector('.commentButton').addEventListener('click', async function () {
    await 엑세스토큰검증();
    댓글저장();
})

document.querySelector('.likeButton').addEventListener('click', async function () {
    await 엑세스토큰검증();
    좋아요저장();
})

document.querySelector('.boardHeaderBodyDiv').addEventListener('click', async function (event) {
    if (event.target.classList.contains('boardUpdate')) {
        게시글수정();
    }
    if (event.target.classList.contains('boardDelete')) {
        await 엑세스토큰검증();
        게시글삭제();
    }
})

document.querySelector('main ul').addEventListener('click', async function (event) {
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
                'Authorization': jwtToken,
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

async function 게시글불러오기() {
    const urlParams = new URLSearchParams(window.location.search);
    var boardId = urlParams.get('boardId');
    await fetch(url + '/board/getBoard/' + boardId, {
        method: 'GET',
        credentials: 'include' //쿠키를 받아와야 되기 때문에 추가
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('http 오류: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            document.querySelector('.boardHeaderBodyDiv').innerHTML = "";
            document.querySelector('main ul').innerHTML = "";

            var 게시판내용 =
                `
                <h2>${data.board.title}</h2>
                <div>
                    <p class="writerP">${data.board.writer}</p>
                    <div class="detailDiv">
                    <p>${data.board.writeTime}</p>
                    <div style="width: 50px;"></div>
                    <p>조회수 ${data.board.viewCount}</p>
                </div>
                <hr>
                <div class="adjustBoardDiv">
                    <p class="boardUpdate">게시글 수정</p>
                    <div class="between_pTag"></div>
                    <p class="boardDelete">게시글 삭제</p>
                </div>
                <div class="contentDiv">
                    <p>${data.board.content}</p>
                </div>
                `;
            document.querySelector('.boardHeaderBodyDiv').insertAdjacentHTML('beforeend', 게시판내용);



            data.boardComments.forEach(function (item) {
                var 댓글 =
                    `
                <li>
                    <p class="writerP">${item.username}</p>
                    <p class="contentP">${item.content}</p>
                    <div class="commentDateDiv">
                        <p class="writeTimeP">${item.writeTime}</p>
                        <p class="commentDeleteP">삭제</p>
                    </div>
                    <input type="hidden" value="${item.id}">
                    <hr>
                </li>
                `;
                document.querySelector('main ul').insertAdjacentHTML('beforeend', 댓글);
            })
        })
    권한체크();
}

function 권한체크() {
    fetch(url + '/board/checkUser', {
        method: 'GET',
        headers: {
            'Authorization': localStorage.getItem('jwtToken')
        }
    })
        .then(response => {
            if (!response.status == 401) {
                throw new Error('http 오류: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            if (data.role == 'ROLE_ADMIN') {
                document.querySelector('.adjustBoardDiv').style.display = 'flex';
            } else if (document.querySelector('.writerP').innerHTML == data.username) {
                document.querySelector('.adjustBoardDiv').style.display = 'flex';
            }
            document.querySelectorAll('main li').forEach(function (item) {
                if (data.role == 'ROLE_ADMIN') {
                    item.querySelector('.commentDeleteP').style.display = 'block';
                } else if (item.querySelector('.writerP').innerHTML == data.username) {
                    item.querySelector('.commentDeleteP').style.display = 'block';
                }
            })
        })
}

function 좋아요불러오기() {
    fetch(url + '/board/getLike' + window.location.search, {
        method: 'GET'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('http 오류: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            document.querySelector('.likeCountP').innerHTML = data;
        })

    fetch(url + '/board/getMyLike' + window.location.search, {
        method: 'GET',
        headers: {
            'Authorization': localStorage.getItem('jwtToken')
        }
    })
        .then(response => {
            if (response.status == 401) {
                document.querySelector('.likeShapeP').innerHTML = '♡';
            }
            return response.json();
        })
        .then(data => {
            if (data == 1) {
                document.querySelector('.likeShapeP').innerHTML = '♥';
            } else {
                document.querySelector('.likeShapeP').innerHTML = '♡';
            }
        })
}

async function 좋아요저장() {
    await fetch(url + '/board/postLike' + window.location.search, {
        method: 'GET',
        headers: {
            'Authorization': localStorage.getItem('jwtToken')
        }
    })
        .then(response => {
            if (response.status == 401) {
                alert('로그인 하세요');
            } else if (response.ok) {
                좋아요불러오기();
            }
        })
}

async function 댓글저장() {
    const urlParams = new URLSearchParams(window.location.search);
    const dataToSend = {
        boardId: urlParams.get('boardId'),
        content: document.querySelector('.commentText').value
    }
    await fetch(url + '/board/postComment', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('jwtToken')
        },
        body: JSON.stringify(dataToSend)
    })
        .then(response => {
            document.querySelector('.commentText').value = '';
            if (response.status == 401) {
                alert('로그인 하세요');
            } else if (response.ok) {
                게시글불러오기();
            }
        })
}

function 게시글수정() {
    window.location.href = 'boardUpdate' + window.location.search;
}

function 게시글삭제() {
    const urlParams = new URLSearchParams(window.location.search);
    const boardId = urlParams.get('boardId');
    fetch(url + '/board/deleteBoard/' + boardId, {
        method: 'DELETE',
        headers: {
            'Authorization': localStorage.getItem('jwtToken')
        }
    })
        .then(response => {
            if (!response.ok) {
                alert('게시글을 삭제할 권한이 없습니다.');
                throw new Error('http 오류: ' + response.status);
            } else {
                window.location.href = '/board?page=1';
            }
        })
}

function 댓글삭제(event) {
    if (event.target.classList.contains('commentDeleteP')) {
        const commentId = event.target.parentNode.parentNode.querySelector('input').value;
        fetch(url + '/board/deleteComment/' + commentId, {
            method: 'DELETE',
            headers: {
                'Authorization': localStorage.getItem('jwtToken')
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('http 오류: ' + response.status);
                } else {
                    게시글불러오기();
                }
            })
    }
}