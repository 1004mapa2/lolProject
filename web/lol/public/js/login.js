const url = 'http://13.124.127.226:8081';
// const url = 'http://localhost:8081';

/**
 * 로그인 버튼 클릭 이벤트
 */
document.querySelector('.submitButton').addEventListener('click', function () {
    로그인();
})

/**
 * 아이디 input 박스에서 enter 눌렀을 때 이벤트
 */
document.querySelector('.username').addEventListener('keyup', function (event) {
    if(event.key === 'Enter') {
        로그인();
    }
})

/**
 * 비밀번호 input 박스에서 enter 눌렀을 때 이벤트
 */
document.querySelector('.password').addEventListener('keyup', function (event) {
    if(event.key === 'Enter') {
        로그인();
    }
})

/**
 * 함수 시작
 */
function 로그인() {
    var username = document.querySelectorAll('input')[0].value;
    var password = document.querySelectorAll('input')[1].value;

    if (username != "" && password != "") {
        const dataToSend = {
            "username": username,
            "password": password
        }

        fetch(url + '/api/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include',
            body: JSON.stringify(dataToSend)
        })
            .then(response => {
                const token = response.headers.get('Authorization');
                localStorage.setItem('jwtToken', token);

                if (response.ok) {
                    // console.log(document.referrer);
                    // if(document.referrer && document.referrer != "http://localhost:3000/membership") {
                    //     window.location = document.referrer;
                    // } else {
                        window.location.href = '/';
                    // }
                } else {
                    document.querySelector('.errorMessage').style.display = 'block';
                }
            })
    } else {
        document.querySelector('.errorMessage').style.display = 'block';
    }
}