const url = 'http://13.124.127.226:8081';
// const url = 'http://localhost:8081';

/**
 * 페이지가 로드될 때 실행
 */
document.addEventListener("DOMContentLoaded", async function () {
    await 게시글리스트불러오기();
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
                'Authorization': localStorage.getItem('jwtToken')
            }
        })
        localStorage.removeItem('jwtToken');
        this.href = '/';
    }
})

/**
 * 글쓰기 버튼 클릭 이벤트
 */
document.querySelector('.writingButton').addEventListener('click', function () {
    window.location.href = "boardWrite";
})

/**
 * 검색 버튼 클릭 이벤트
 */
document.querySelector('.searchButton').addEventListener('click', function () {
    검색();
})

/**
 * 검색 인풋박스에서 enter 눌렀을 때 이벤트
 */
document.querySelector('.searchInput').addEventListener('keyup', function (event) {
    if (event.key === 'Enter') {
        검색();
    }
})

/**
 * 작성 글 클릭 이벤트
 * 글 클릭 시 boardId를 parameter로 갖고 글상세 url로 이동
 */
document.querySelector('main ul').addEventListener('click', function (event) {
    if (event.target.classList.contains("upperDiv")) {
        var boardId = event.target.parentNode.querySelector('input').value;
        var boardViewURL = "/boardView?boardId=" + boardId;
        window.location.href = boardViewURL;
    } else if(event.target.classList.contains("boardTitle")) {
        var boardId = event.target.parentNode.parentNode.querySelector('input').value;
        var boardViewURL = "/boardView?boardId=" + boardId;
        window.location.href = boardViewURL;
    }
})

/**
 * 글 정렬 순서 바꾸기 이벤트
 */
document.querySelector('.sort').addEventListener('input', function () {
    게시글리스트불러오기();
})

/**
 * 함수 시작
 */
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
                    jwtToken = response.headers.get('Authorization');
                    localStorage.setItem('jwtToken', jwtToken);
                    document.querySelector('.loginDiv').innerHTML = '로그아웃';
                    document.querySelector('.myPage').style.display = 'block';
                } else {
                    document.querySelector('.loginDiv').innerHTML = '로그아웃';
                    document.querySelector('.myPage').style.display = 'block';
                }
                document.querySelector('.writingButton').style.display = 'block';
            })
    } else {
        document.querySelector('.loginDiv').innerHTML = '로그인';
    }
}

async function 게시글리스트불러오기() {
    const urlParams = new URLSearchParams(window.location.search);
    const dataToSend = {
        page: urlParams.get('page'),
        keyword: urlParams.get('keyword'),
        sort: document.querySelector('.sort').value,
        searchSort: urlParams.get('searchSort')
    }
    await fetch(url + '/board/getBoardList', {
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
            document.querySelector('main ul').innerHTML = '';
            document.querySelector('.pageNumberDiv').innerHTML = '';
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
                            <input type="hidden" id="boardId" value="${item.id}">
                            <div class="upperDiv">
                                <p class="boardTitle">${item.title}</p>
                                <img class="commentImg" src="/img/말풍선.png">
                            </div>
                            <div class="lowerDiv">
                                <div class="lowerLeft">
                                    <p>${item.writer}</p>
                                    <p>작성일 ${item.writeTime}</p>
                                    <p>조회수 ${item.viewCount}</p>
                                    <p>좋아요 ${item.likeCount}</p>
                                </div>
                                <p class="commentCount">${item.commentCount}</p>
                            </div>
                            <hr>
                        </li>
                        `;
                    document.querySelector('main ul').insertAdjacentHTML('beforeend', 게시물);
                })
            }
        })
}

function 검색() {
    const keyword = document.querySelector('.searchInput').value;
    const searchSort = document.querySelector('.searchSort').value;

    var searchURL = "/board?page=1&keyword=" + keyword + "&searchSort=" + searchSort;
    if (keyword != '') {
        window.location.href = searchURL;
    }
}