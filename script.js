function bookFlight() {
    // Implement API call to book a flight
    fetch('/bookFlight', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({})
    })
    .then(response => response.json())
    .then(data => {
        document.getElementById('outputText').innerHTML = data.message;
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function cancelReservation() {
    // Implement API call to cancel a reservation
    fetch('/cancelReservation', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({})
    })
    .then(response => response.json())
    .then(data => {
        document.getElementById('outputText').innerHTML = data.message;
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
