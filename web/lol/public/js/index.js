function 바꾸기() {
    document.querySelector('.hi').innerHTML = '안녕 못해';
}

function 테스트() {
    var empty = 'http://localhost:3000/';
    if (document.getElementById('com1').src == empty
        && document.getElementById('com2').src == empty
        && document.getElementById('com3').src == empty
        && document.getElementById('com4').src == empty
        && document.getElementById('com5').src == empty) {
        alert('조합을 선택하세요');
    } else {
        var champion1 = document.getElementById('com1').src.replace('http://localhost:3000/', '').replace('img/', '').replace('.png', '');
        var champion2 = document.getElementById('com2').src.replace('http://localhost:3000/', '').replace('img/', '').replace('.png', '');
        var champion3 = document.getElementById('com3').src.replace('http://localhost:3000/', '').replace('img/', '').replace('.png', '');
        var champion4 = document.getElementById('com4').src.replace('http://localhost:3000/', '').replace('img/', '').replace('.png', '');
        var champion5 = document.getElementById('com5').src.replace('http://localhost:3000/', '').replace('img/', '').replace('.png', '');

        const dataToSend = {
            topId: champion1,
            jungleId: champion2,
            middleId: champion3,
            bottomId: champion4,
            utilityId: champion5
        };

        fetch('http://localhost:8081/1', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
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
                if (data == null) {
                    alert('조합이 없습니다');
                } else {
                    document.querySelector('.pickCount').innerHTML = data.value.pickCount;
                    document.querySelector('.winRate').innerHTML = data.value.winRate * 100 + '%';
                }

            })
    }
}


function 조합박스(click_src) {
    var fileName = click_src.replace('http://localhost:3000', '');
    var space1 = document.querySelector('#com1').src;
    var space2 = document.querySelector('#com2').src;
    var space3 = document.querySelector('#com3').src;
    var space4 = document.querySelector('#com4').src;
    var space5 = document.querySelector('#com5').src;
    var empty = "http://localhost:3000/img/emptyBox.png";

    if (space1 != empty && space2 != empty &&
        space3 != empty && space4 != empty && space5 != empty) {
        alert('다 채웠음');
    } else if (click_src == space1 || click_src == space2 ||
        click_src == space3 || click_src == space4 || click_src == space5) {
        alert('중복 챔피언');
    } else {
        if (space1 == empty) {
            document.querySelector('#com1').src = fileName;
        } else if (space2 == empty) {
            document.querySelector('#com2').src = fileName;
        } else if (space3 == empty) {
            document.querySelector('#com3').src = fileName;
        } else if (space4 == empty) {
            document.querySelector('#com4').src = fileName;
        } else if (space5 == empty) {
            document.querySelector('#com5').src = fileName;
        }
    }
}


function 빼기(id) {
    document.getElementById(id).src = "/img/emptyBox.png";
}


function 랜덤() {
    var space1 = document.querySelector('#com1').src;
    var space2 = document.querySelector('#com2').src;
    var space3 = document.querySelector('#com3').src;
    var space4 = document.querySelector('#com4').src;
    var space5 = document.querySelector('#com5').src;
    var empty = "http://localhost:3000/img/emptyBox.png";

    if (space1 == empty) {
        document.querySelector('#com1').src = "/img/random.png";
    } else if (space2 == empty) {
        document.querySelector('#com2').src = "/img/random.png";
    } else if (space3 == empty) {
        document.querySelector('#com3').src = "/img/random.png";
    } else if (space4 == empty) {
        document.querySelector('#com4').src = "/img/random.png";
    } else if (space5 == empty) {
        document.querySelector('#com5').src = "/img/random.png";
    } else {
        alert('다 채웠음');
    }
}


function 비우기() {
    document.querySelector('#com1').src = "/img/emptyBox.png";
    document.querySelector('#com2').src = "/img/emptyBox.png";
    document.querySelector('#com3').src = "/img/emptyBox.png";
    document.querySelector('#com4').src = "/img/emptyBox.png";
    document.querySelector('#com5').src = "/img/emptyBox.png";
}