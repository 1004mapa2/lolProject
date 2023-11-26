document.querySelector('.loginButton').addEventListener('click', function () {
    로그인();
})

document.querySelector('.username').addEventListener('keyup', function (event) {
    if(event.key === 'Enter') {
        로그인();
    }
})

document.querySelector('.password').addEventListener('keyup', function (event) {
    if(event.key === 'Enter') {
        로그인();
    }
})

function 로그인() {
    var username = document.querySelectorAll('input')[0].value;
    var password = document.querySelectorAll('input')[1].value;

    if (username != "" && password != "") {
        const dataToSend = {
            "username": username,
            "password": password
        }

        fetch('http://localhost:8081/api/login', {
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
                    window.location.href = "/";
                } else {
                    document.querySelector('.errorMessage').style.display = 'block';
                }
            })
    } else {
        document.querySelector('.errorMessage').style.display = 'block';
    }
}