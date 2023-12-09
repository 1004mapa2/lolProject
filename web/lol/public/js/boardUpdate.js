const url = 'http://localhost:8081';

document.addEventListener("DOMContentLoaded", function () {
    엑세스토큰검증();
    글데이터불러오기();
})

document.querySelector('.loginDiv').addEventListener('click', function () {
    if (this.innerHTML == '로그아웃') {
        fetch(url + '/api/logout', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('jwtToken')
            }
        })
        localStorage.removeItem('jwtToken');
        this.href = '/';
    }
})

document.querySelector('.cencelButton').addEventListener('click', function () {
    window.location.href = '/boardView' + window.location.search;
})

document.querySelector('.postButton').addEventListener('click', async function () {
    await 엑세스토큰검증();
    권한체크();
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

function 글데이터불러오기() {
    fetch(url + '/board/getBoardUpdateData' + window.location.search, {
        method: 'GET'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('http 오류: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            document.querySelector('.titleText').value = data.title;
            document.querySelector('.contentText').value = data.content;
        })
}

function 권한체크() {
    fetch(url + '/board/checkUser' + window.location.search, {
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
            if (data == 1) {
                글수정하기();
            } else {
                alert('정보 불일치로 수정 불가');
            }
        })
}

function 글수정하기() {
    const dataToSend = {
        title: document.querySelector('.titleText').value,
        content: document.querySelector('.contentText').value
    }
    const urlParams = new URLSearchParams(window.location.search);
    var boardId = urlParams.get('boardId');

    fetch(url + '/board/updateBoard/' + boardId, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('jwtToken')
        },
        body: JSON.stringify(dataToSend)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('http 오류: ' + response.status);
            } else {
                window.location.href = '/boardView' + window.location.search;
            }
        })
}