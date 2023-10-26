document.addEventListener("DOMContentLoaded", function () {
    document.querySelector('#tab1').style.color = '#DA81F5';
    ALLTIER받아오기();
})

document.querySelector('.sortP').addEventListener('input', function(){
    ALLTIER받아오기();
})

document.querySelector('.tierP').addEventListener('input', function(){
    ALLTIER받아오기();
})

function ALLTIER받아오기() {
    var tierValue = document.querySelector('.tierP').value;
    var sortValue = document.querySelector('.sortP').value;

    if(sortValue == '승률 순'){
        sortValue = 'WINRATE';
    } else {
        sortValue = 'PICKCOUNT';
    }

    const dataToSend = {
        tier: tierValue,
        sort: sortValue
    }

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
            console.log(data);
            for(let i = 0; i < data.length; i++){
                document.querySelectorAll('.winRate')[i].innerHTML = data[i].winRate;
                document.querySelectorAll('.pickCount')[i].innerHTML = data[i].pickCount;
                for(let j = 0; j < 5; j++){
                    if(j == 0){
                        document.querySelectorAll('.resultCom')[i].querySelectorAll('img')[j].src = '/img/' + data[i].topId + '.png';
                    }else if(j == 1){
                        document.querySelectorAll('.resultCom')[i].querySelectorAll('img')[j].src = '/img/' + data[i].jungleId + '.png';
                    }else if(j == 2){
                        document.querySelectorAll('.resultCom')[i].querySelectorAll('img')[j].src = '/img/' + data[i].middleId + '.png';
                    }else if(j == 3){
                        document.querySelectorAll('.resultCom')[i].querySelectorAll('img')[j].src = '/img/' + data[i].bottomId + '.png';
                    }else if(j == 4){
                        document.querySelectorAll('.resultCom')[i].querySelectorAll('img')[j].src = '/img/' + data[i].utilityId + '.png';
                    }
                }
            }
        })
}











function 데이터넘기기(src) {
    조합박스(src);
    var top = document.querySelector('#com1').src.replace('http://localhost:3000/img/', '').replace('.png', "");
    var jungle = document.querySelector('#com2').src.replace('http://localhost:3000/img/', '').replace('.png', "");
    var middle = document.querySelector('#com3').src.replace('http://localhost:3000/img/', '').replace('.png', "");
    var bottom = document.querySelector('#com4').src.replace('http://localhost:3000/img/', '').replace('.png', "");
    var utility = document.querySelector('#com5').src.replace('http://localhost:3000/img/', '').replace('.png', "");

    const dataToSend = {
        topName: top,
        jungleName: jungle,
        middleName: middle,
        bottomName: bottom,
        utilityName: utility
    };

    // fetch('http://localhost:8081/1', {
    //     method: 'POST',
    //     headers: {
    //         'Content-Type': 'application/json',
    //     },
    //     body: JSON.stringify(dataToSend)
    // })
    // .then(response => {
    //     if (!response.ok) {
    //         throw new Error('http 오류: ' + response.status);
    //     }
    //     return response.json();
    // })
    // .then(data => {
    //     if (data == null) {
    //         alert('조합이 없습니다');
    //     } else {
    //         document.querySelector('.pickCount').innerHTML = data.value.pickCount;
    //         document.querySelector('.winRate').innerHTML = data.value.winRate * 100 + '%';
    //     }

    // })
}


function 조합박스(click_src) {
    var fileName = click_src.replace('http://localhost:3000', '');
    var space1 = document.querySelector('#com1').src;
    var space2 = document.querySelector('#com2').src;
    var space3 = document.querySelector('#com3').src;
    var space4 = document.querySelector('#com4').src;
    var space5 = document.querySelector('#com5').src;
    var empty = "http://localhost:3000/img/emptyBox.png";

    if (click_src == space1 || click_src == space2 ||
        click_src == space3 || click_src == space4 || click_src == space5) {
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