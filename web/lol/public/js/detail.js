document.addEventListener("DOMContentLoaded", async function () {
    document.querySelector('#tab1').style.color = '#DA81F5';
    await 엑세스토큰검증();
    await 댓글불러오기();
    파라미터보내기();
})

async function 엑세스토큰검증() {
    var jwtToken = localStorage.getItem('jwtToken');

    if (jwtToken != "null" && jwtToken != null) {
        await fetch('http://localhost:8081/api/init', {
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
                } else {
                    document.querySelector('.loginDiv').innerHTML = '로그아웃';
                }
            })
    } else {
        document.querySelector('.loginDiv').innerHTML = '로그인';
    }
}

async function 댓글불러오기() {
    const urlParams = new URLSearchParams(window.location.search);
    const dataToSend = {
        comsaveId: urlParams.get('comsaveId')
    }
    fetch('http://localhost:8081/getComment', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dataToSend)
    })
        .then(response => {

            return response.json();
        })
        .then(data => {
            document.querySelector('.commentContainer').innerHTML = "";
            for (let i = 0; i < data.length; i++) {
                var 게시글 =
                    `
                <div>
                        <div>${data[i].username}</div>
                        <div>${data[i].content}</div>
                        <div>${data[i].writeTime}</div>
                        <hr/>
                    </div>
                    `
                document.querySelector('.commentContainer').insertAdjacentHTML('beforeend', 게시글);
            }
            console.log(data);
        })
}

document.querySelector('.loginDiv').addEventListener('click', function () {
    if (this.innerHTML == '로그인') {
        window.location.href = "/login";
    } else {
        fetch('http://localhost:8081/api/logout', {
            method: 'GET',
            headers: {
                'Authorization': localStorage.getItem('jwtToken')
            }
        })
        localStorage.removeItem('jwtToken');
        location.reload();
    }
})

function 파라미터보내기() {
    const urlParams = new URLSearchParams(window.location.search);
    comsaveId = urlParams.get('comsaveId');
    tier = urlParams.get('tier');
    fetch(`http://localhost:8081/getDetailInfo?comsaveId=${comsaveId}&tier=${tier}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('http 오류: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            var 원본조합 =
                `
                <div class="comImgDiv">
                    <img class="pickImg" src="/img/${data[0].topName}.png">
                    <img class="pickImg" src="/img/${data[0].jungleName}.png">
                    <img class="pickImg" src="/img/${data[0].middleName}.png">
                    <img class="pickImg" src="/img/${data[0].bottomName}.png">
                    <img class="pickImg" src="/img/${data[0].utilityName}.png">
                </div>
                <div>픽횟수: ${data[0].pickCount}</div>
                <div>승률: ${data[0].winRate}</div>
                `
            document.querySelector('.pickComBox').insertAdjacentHTML('afterbegin', 원본조합);

            for (let i = 1; i < data.length; i++) {
                var 진조합 =
                    `
                    <div class="loseComImgDiv">
                    <img src="/img/${data[i].topName}.png">
                    <img src="/img/${data[i].jungleName}.png">
                    <img src="/img/${data[i].middleName}.png">
                    <img src="/img/${data[i].bottomName}.png">
                    <img src="/img/${data[i].utilityName}.png">
                    <a class="loseCount">${data[i].loseCount}</a>
                    </div>
                    `
                document.querySelector('.loseComBox').insertAdjacentHTML('afterbegin', 진조합);
            }
        })
}

document.querySelector('.commentButton').addEventListener('click', async function () {
    await 엑세스토큰검증();
    await 댓글저장();
    await 댓글불러오기();
    document.querySelector('.commentText').value = '';
})

async function 댓글저장() {
    const urlParams = new URLSearchParams(window.location.search);
    const dataToSend = {
        content: document.querySelector('.commentText').value,
        comsaveId: urlParams.get('comsaveId')
    }
    await fetch('http://localhost:8081/saveComment', {
        method: 'POST',
        headers: {
            'Authorization': localStorage.getItem('jwtToken'),
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dataToSend)
    })
        .then(response => {
            if(response.status == 401) {
                alert('로그인 하세요');
            }
        })
}

async function 댓글불러오기() {
    const urlParams = new URLSearchParams(window.location.search);
    const dataToSend = {
        comsaveId: urlParams.get('comsaveId')
    }
    await fetch('http://localhost:8081/getComment', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dataToSend)
    })
        .then(response => {

            return response.json();
        })
        .then(data => {
            document.querySelector('.commentContainer').innerHTML = "";
            for (let i = 0; i < data.length; i++) {
                var 게시글 =
                    `
                <div>
                        <div>${data[i].username}</div>
                        <div>${data[i].content}</div>
                        <div>${data[i].writeTime}</div>
                        <hr/>
                    </div>
                    `
                document.querySelector('.commentContainer').insertAdjacentHTML('beforeend', 게시글);
            }
        })
}