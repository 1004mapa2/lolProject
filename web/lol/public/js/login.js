document.querySelector('.submitButton').addEventListener('click', function(){
    const dataToSend = {
        "username" : document.querySelectorAll('input')[0].value,
        "password" : document.querySelectorAll('input')[1].value
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

            console.log(response.status);
            if (response.ok) {
                window.location.href="/";
            } else {
                //처리
                console.log("로그인 안됨");
            }
        })
})