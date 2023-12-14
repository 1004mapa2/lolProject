const url = 'http://13.124.127.226:8081';
// const url = 'http://localhost:8081';

document.addEventListener("DOMContentLoaded", async function () {
    await 엑세스토큰검증();
    아이디불러오기();
    비밀번호수정허용();
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

document.querySelector('.submitButton').addEventListener('click', function () {
    현재비밀번호체크();
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

function 아이디불러오기() {
    fetch(url + '/api/getMyPage', {
        method: 'GET',
        headers: {
            'Authorization': localStorage.getItem('jwtToken')
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new error(response.status);
            }
            return response.text();
        })
        .then(data => {
            document.querySelector('.usernameInput').value = data;
        })
}

function 현재비밀번호체크() {
    var password = document.querySelectorAll('input')[1].value;
    var newPassword1 = document.querySelectorAll('input')[2].value;
    var newPassword2 = document.querySelectorAll('input')[3].value;
    const dataToSend = {
        originalPassword: password,
        newPassword: newPassword1
    }

    if (password != "" && newPassword1 == newPassword2) {
        fetch(url + '/api/updateUser', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('jwtToken')
            },
            body: JSON.stringify(dataToSend)
        })
            .then(response => {
                if (!response.ok) {
                    throw new error(response.status);
                }
                return response.json();
            })
            .then(data => {
                if (data == 1) {
                    alert('비밀번호가 변경되었습니다.');
                    window.location.href = '/';
                } else {
                    alert('정보를 다시 입력하세요.');
                }
            })
    } else {
        alert('정보를 다시 입력하세요.');
    }
}

function 비밀번호수정허용() {
    const inputBox = document.querySelectorAll('input');
    const submitButton = document.querySelector('.submitButton');
    inputBox.forEach(function (e) {
        e.addEventListener('blur', function () {
            if (문자열확인(inputBox)) {
                submitButton.style.backgroundColor = '#9A73B5';
            } else {
                submitButton.style.backgroundColor = '#848484';
            }
        })
    })
}

function 문자열확인(inputBox) {
    for (let i = 1; i < inputBox.length - 1; i++) {
        if (inputBox[i].value === '') {
            return false;
        }
    }
    return true;
}