const url = 'http://3.37.36.48:8081';

document.querySelector('.usernameInput').addEventListener('blur', function () {
    //fetch로 db에서 같은 아이디가 있는지 체크
    const errMsg = document.querySelector('.idErrorMessage');
    const username = document.querySelector('.usernameInput').value;
    if (username == "") {
        errMsg.style.color = '#de283b';
        errMsg.innerHTML = '아이디를 입력해 주세요.';
    } else if (username.length > 10) {
        errMsg.style.color = '#de283b';
        errMsg.innerHTML = '아이디는 10자리를 넘길 수 없습니다.';
    } else {
        const username = {
            "username": document.querySelector('.usernameInput').value
        }
        fetch(url + '/api/usernameDuplicateCheck', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(username)
        })
            .then(response => {
                return response.json();
            })
            .then(data => {
                if (data == 1) {
                    errMsg.style.color = '#de283b';
                    errMsg.innerHTML = '사용할 수 없는 아이디입니다.';

                } else if (data == 0) {
                    errMsg.style.color = '#03c75a';
                    errMsg.innerHTML = '사용 가능한 아이디입니다.';
                }
                회원가입허용();
            })
    }
})

document.querySelector('.password1Input').addEventListener('blur', function () {
    //비밀번호 길이 15자리 이상이면 오류 메시지 출력
    var password1_errMsg = document.querySelector('.password1ErrorMessage');
    var password2_errMsg = document.querySelector('.password2ErrorMessage');
    var password1 = document.querySelector('.password1Input').value;
    var password2 = document.querySelector('.password2Input').value;

    if (password1 == "") {
        password1_errMsg.style.color = '#de283b';
        password1_errMsg.innerHTML = '비밀번호를 입력해 주세요.';
    } else if (password1.length > 15) {
        password1_errMsg.style.color = '#de283b';
        password1_errMsg.innerHTML = '비밀번호는 15자리를 넘을 수 없습니다.';
    } else {
        password1_errMsg.style.color = '#03c75a';
        password1_errMsg.innerHTML = '사용 가능한 비밀번호입니다.';
    }

    // password1 password2 비교해서 password2ErrorMassage 컨트롤
    if (password1 == password2 && password1 != "") {
        password2_errMsg.style.color = '#03c75a';
        password2_errMsg.innerHTML = '비밀번호가 일치합니다.';
    } else {
        password2_errMsg.style.color = '#de283b';
        password2_errMsg.innerHTML = '비밀번호가 일치하지 않습니다.';
    }
    회원가입허용();

})

document.querySelector('.password2Input').addEventListener('blur', function () {
    //위에 비밀번호와 같지 않으면 오류 메시지 출력
    var errMsg = document.querySelector('.password2ErrorMessage');
    var password1 = document.querySelector('.password1Input').value;
    var password2 = document.querySelector('.password2Input').value;

    if (password1 != password2 || password2 == "") {
        errMsg.style.color = '#de283b';
        errMsg.innerHTML = '비밀번호가 일치하지 않습니다.';
    }
    else {
        errMsg.style.color = '#03c75a';
        errMsg.innerHTML = '비밀번호가 일치합니다.';
    }
    회원가입허용();
})

document.querySelector('.submitButton').addEventListener('click', function () {
    회원가입();
})

function 회원가입허용() {
    var errMsg1 = document.querySelector('.idErrorMessage').innerHTML;
    var errMsg2 = document.querySelector('.password1ErrorMessage').innerHTML;
    var errMsg3 = document.querySelector('.password2ErrorMessage').innerHTML;
    var loginButton = document.querySelector('.submitButton');

    if (errMsg1 == "사용 가능한 아이디입니다." &&
        errMsg2 == '사용 가능한 비밀번호입니다.' &&
        errMsg3 == '비밀번호가 일치합니다.') {
        loginButton.style.backgroundColor = '#9A73B5';
    } else {
        loginButton.style.backgroundColor = '#848484';
    }
}

function 회원가입() {
    var errMsg1 = document.querySelector('.idErrorMessage').innerHTML;
    var errMsg2 = document.querySelector('.password1ErrorMessage').innerHTML;
    var errMsg3 = document.querySelector('.password2ErrorMessage').innerHTML;
    if (errMsg1 == "사용 가능한 아이디입니다." &&
        errMsg2 == '사용 가능한 비밀번호입니다.' &&
        errMsg3 == '비밀번호가 일치합니다.') {
        const dataToSend = {
            "username": document.querySelectorAll('input')[0].value,
            "password": document.querySelectorAll('input')[1].value
        }

        fetch(url + '/api/registerUser', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(dataToSend)
        })
            .then(response => {
                if (response.ok) {
                    window.location.href = "/login";
                }
            })
    } else {
        alert("정보를 다시 입력해주세요.");
    }
}