document.querySelector('.submitButton').addEventListener('click', function(){
    fetch('http://localhost:8081/5', {
        method: 'POST',
        body: new FormData(document.getElementById('formId'))
    })
        .then(response => {
            if (response.ok) {
                window.location.href="/login";
            }
        })
})