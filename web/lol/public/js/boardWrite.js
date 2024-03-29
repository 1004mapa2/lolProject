const url = 'http://13.124.127.226:8081';
// const url = 'http://localhost:8081';

/**
 * 페이지가 로드될 때 실행
 */
document.addEventListener("DOMContentLoaded", function () {
    엑세스토큰검증();
})

/**
 * 로그아웃 버튼 클릭 이벤트
 */
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

/**
 * 취소 버튼 클릭 시 게시글 목록 url로 이동
 */
document.querySelector('.cencelButton').addEventListener('click', function () {
    window.location.href = "board";
})

/**
 * 글등록 버튼 클릭 이벤트
 */
document.querySelector('.postButton').addEventListener('click', async function () {
    await 엑세스토큰검증();
    글등록();
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

function 글등록() {
    const title = document.querySelector('.titleText').value;
    const content = document.querySelector('.contentText').value;
    if(title.length > 30) {
        alert('제목의 길이는 30자를 넘을 수 없습니다.');
    } else if(content.length > 1000) {
        alert('내용의 길이는 1000자를 넘을 수 없습니다.');
    } else {
        const dataToSend = {
            title: title,
            content: content
        };
        fetch(url + '/board/postBoard', {
            method: 'POST',
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
                    window.location.href = "board?page=1";
                }
            })
    }
}