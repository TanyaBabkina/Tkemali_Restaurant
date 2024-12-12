function getUserFromToken() {
    const token = localStorage.getItem('token');
    if (token) {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        return JSON.parse(jsonPayload);
    }
    return null;
}

// Function to get the user email from the token
function getUserId() {
    const user = getUserFromToken();
    return user ? user.sub : null; // Assuming 'sub' is the user email
}

// Function to handle profile button click
function handleProfileClick(event) {
    event.preventDefault();
    const userEmail = getUserId();

    console.log("User ID:", userEmail); // Log user ID
    if (userEmail) {
        window.location.href = `/home/users/${userEmail}`;
    } else {
        alert('User ID not found. Please log in.');
    }
}

// Main function to initialize the page
function initializePage() {
    const profileButton = document.getElementById('profileLink');
    if (profileButton) {
        profileButton.addEventListener('click', handleProfileClick);
        console.log("Обработчик события добавлен к профилю."); // Лог для проверки
    } else {
        console.error("Элемент profileButton не найден."); // Лог ошибки
    }
}

// Wait for the DOM to be fully loaded
document.addEventListener('DOMContentLoaded', initializePage);