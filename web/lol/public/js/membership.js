document.querySelector('.submitButton').addEventListener('click', function(){
    const dataToSend = {
        "username" : document.querySelectorAll('input')[0].value,
        "password" : document.querySelectorAll('input')[1].value,
        "role" : document.querySelectorAll('input')[2].value
    }
    
    fetch('http://localhost:8081/api/registerUser', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(dataToSend)
    })
        .then(response => {
            if (response.ok) {
                window.location.href="/login";
            }
        })
})