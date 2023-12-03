document.addEventListener("DOMContentLoaded", function () {
    엑세스토큰검증();
})

function 엑세스토큰검증() {
    var jwtToken = localStorage.getItem('jwtToken');
    
    if (jwtToken != "null" && jwtToken != null) {
        fetch('http://localhost:8081/api/init', {
            method: 'GET',
            headers: {
                'Authorization': localStorage.getItem('jwtToken'),
            },
            credentials: 'include'
        })
            .then(response => {
                if(response.headers.get('Authorization') != null){
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

document.querySelector('.loginDiv').addEventListener('click', function () {
        fetch('http://localhost:8081/api/logout', {
            method: 'GET',
            headers: {
                'Authorization': localStorage.getItem('jwtToken')
            }
        })
        localStorage.removeItem('jwtToken');
        window.location.href = "/";
})