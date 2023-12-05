const url = 'http://localhost:8081';

document.addEventListener("DOMContentLoaded", function () {
    엑세스토큰검증();
    게시글불러오기();
})

function 엑세스토큰검증() {
    var jwtToken = localStorage.getItem('jwtToken');

    if (jwtToken != "null" && jwtToken != null) {
        fetch(url + '/api/init', {
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
        document.querySelector('.writingButton').style.display = 'none';
    }
}

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

function 게시글불러오기() {
    const urlParams = new URLSearchParams(window.location.search);
    const dataToSend = {
        page: urlParams.get('page'),
        keyword: urlParams.get('keyword'),
        sort: urlParams.get('sort')
    }

    fetch(url + '/board/getBoardList', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dataToSend)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('http 오류: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            var innervalue = "";
            document.querySelector('main ul').insertAdjacentHTML('beforeend', '<hr>');
            if (data.maxPage == 0) {
                var 메시지 =
                    `
                <div>
                    게시글이 없습니다.
                </div>
                `
                document.querySelector('main ul').insertAdjacentHTML('beforeend', 메시지);
            } else {
                if (urlParams.get('keyword') == null && urlParams.get('sort') == null) {
                    for (let i = 1; i <= data.maxPage; i++) {
                        var 페이지갯수 =
                            `
                            <a href="/board?page=${i}">${i}</a>
                            `
                        document.querySelector('.pageNumberDiv').insertAdjacentHTML('beforeend', 페이지갯수);
                    }
                } else {
                    for (let i = 1; i <= data.maxPage; i++) {
                        var 페이지갯수 =
                            `
                            <a href="/board?page=${i}&keyword=${urlParams.get('keyword')}&sort=${urlParams.get('sort')}">${i}</a>
                            `
                        document.querySelector('.pageNumberDiv').insertAdjacentHTML('beforeend', 페이지갯수);
                    }
                }

                data.boardList.forEach(function (item) {
                    var 게시물 =
                        `
                        <li class="writeBox">
                            <div class="upperDiv">
                                <p>${item.title}</p>
                                <img class="commentImg" src="/img/말풍선.png">
                            </div>
                            <div class="lowerDiv">
                                <div class="lowerLeft">
                                    <p>${item.writer}</p>
                                    <p>작성일 ${item.writeTime}</p>
                                    <p>조회수 ${item.viewCount}</p>
                                    <p>좋아요 ${item.likeCount}</p>
                                </div>
                                <p class="commentCount">0</p>
                            </div>
                            <hr>
                        </li>
                        `;
                    document.querySelector('main ul').insertAdjacentHTML('beforeend', 게시물);
                })
            }
        })
}

document.querySelector('.writingButton').addEventListener('click', function () {
    window.location.href = "boardWrite";
})

document.querySelector('.searchButton').addEventListener('click', function () {
    검색();
})

document.querySelector('.searchInput').addEventListener('keyup', function (event) {
    if (event.key === 'Enter') {
        검색();
    }
})

function 검색() {
    var keyword = document.querySelector('.searchInput').value;
    var searchSort = document.querySelector('.searchSort').value;
    var searchURL = "/board?page=1&keyword=" + keyword + "&sort=" + searchSort;
    if (keyword != '') {
        window.location.href = searchURL;
    }
}