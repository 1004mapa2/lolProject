const url = 'http://localhost:8081';

document.addEventListener("DOMContentLoaded", async function () {
    await 엑세스토큰검증();
    await 게시글불러오기();
    await 좋아요불러오기();
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
    await 댓글저장();
    await 게시글불러오기();
})

document.querySelector('.likeButton').addEventListener('click', async function () {
    await 엑세스토큰검증();
    await 좋아요저장();
    await 좋아요불러오기();
})

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
            }
        })
}

async function 게시글불러오기() {
    await fetch(url + '/board/getBoard' + window.location.search, {
        method: 'GET',
        credentials: 'include'
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
                    <p>${data.board.writer}</p>
                    <div class="detailDiv">
                    <p>${data.board.writeTime}</p>
                    <div style="width: 50px;"></div>
                    <p>조회수 ${data.board.viewCount}</p>
                </div>
                <hr>
                <div class="adjustBoardDiv">
                    <p>게시글 수정</p>
                    <div class="between_pTag"></div>
                    <p>게시글 삭제</p>
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
                    <p class="writeTimeP">${item.writeTime}</p>
                    <hr>
                </li>
                `;
                document.querySelector('main ul').insertAdjacentHTML('beforeend', 댓글);
            })
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
            }
        })
}

async function 좋아요불러오기() {
    await fetch(url + '/board/getLike' + window.location.search, {
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
    
    await fetch(url + '/board/getMyLike' + window.location.search, {
        method: 'GET',
        headers: {
            'Authorization': localStorage.getItem('jwtToken')
        }
    })
        .then(response => {
            if (response.status == 401) {
                document.querySelector('.likeShapeP'). innerHTML = '♡';
            }
            return response.json();
        })
        .then(data => {
            if(data == 1) {
                document.querySelector('.likeShapeP'). innerHTML = '♥';
            } else {
                document.querySelector('.likeShapeP'). innerHTML = '♡';
            }
        })
}