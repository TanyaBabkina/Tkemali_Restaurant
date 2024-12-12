// // profile-scripts.js
//
// // Function to check if the user is authenticated
// function checkAuthentication() {
//     const token = localStorage.getItem('token');
//     if (!token) {
//         window.location.replace('/login');
//     }
//     return token;
// }
//
// // Function to add authorization header to fetch requests
// function addAuthorizationHeader(token) {
//     const originalFetch = window.fetch;
//     window.fetch = function(...args) {
//         const [url, config = {}] = args;
//         config.headers = {
//             ...config.headers,
//             'Authorization': `Bearer ${token}`
//         };
//         return originalFetch(url, config);
//     };
// }
//
// // Function to handle form submission
// async function handleFormSubmit(event) {
//     event.preventDefault();
//
//     const form = event.target;
//     const formData = new FormData(form);
//     const jsonData = {};
//     formData.forEach((value, key) => {
//         jsonData[key] = value;
//     });
//
//     try {
//         const response = await fetch(form.action, {
//             method: form.method || 'POST', // Ensure you're using the correct method
//             headers: {
//                 'Content-Type': 'application/json'
//             },
//             body: JSON.stringify(jsonData)
//         });
//
//         if (response.ok) {
//             alert('Профиль успешно обновлен');
//         } else {
//             const error = await response.json();
//             alert(error.message || 'Не удалось обновить профиль');
//         }
//     } catch (error) {
//         alert('Что-то пошло не так!');
//         console.error('Ошибка:', error);
//     }
// }
//
// // Function to delete user
// function deleteUser(userId) {
//     if (confirm('Вы уверены, что хотите удалить этого пользователя?')) {
//         fetch(`/home/users/delete/${userId}`, {
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json'
//             }
//         })
//             .then(response => {
//                 if (response.ok) {
//                     location.reload(); // Reloads the page
//                 } else {
//                     return response.json().then(errorData => {
//                         alert(errorData.message || 'Ошибка при удалении пользователя.');
//                     });
//                 }
//             })
//             .catch(error => {
//                 console.error('Ошибка:', error);
//                 alert('Что-то пошло не так!');
//             });
//     }
// }
//
// // Function to update role
// function updateRole(userId, role) {
//     const form = document.createElement('form');
//     form.method = 'post';
//     form.action = `/home/users/update/${userId}`;
//
//     const input = document.createElement('input');
//     input.type = 'hidden';
//     input.name = 'role';
//     input.value = role;
//
//     form.appendChild(input);
//     document.body.appendChild(form);
//     form.submit();
// }
//
// // Function to handle adding new user
// document.getElementById('addUserForm').addEventListener('submit', async function(event) {
//     event.preventDefault(); // Prevent default form submission
//
//     const formData = new FormData(this);
//     const jsonData = {};
//
//     formData.forEach((value, key) => {
//         jsonData[key] = value;
//     });
//
//     try {
//         const response = await fetch('/home/users/add', {
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json'
//             },
//             body: JSON.stringify(jsonData)
//         });
//
//         if (response.ok) {
//             window.location.reload(); // Reload the page
//         } else {
//             const errorData = await response.json();
//             handleErrors(errorData);
//         }
//     } catch (error) {
//         console.error('Ошибка:', error);
//         showGeneralError('Что-то пошло не так!'); // Handle network errors
//     }
// });
//
// // Function to handle errors
// function handleErrors(errorData) {
//     if (errorData.message) {
//         showEmailError(errorData.message); // Отображаем ошибку для поля email
//     }
// }
//
// // Function to show email error
// function showEmailError(message) {
//     const emailInput = document.getElementById('email');
//     const emailError = document.getElementById('emailError');
//
//     emailInput.classList.add('is-invalid'); // Выделяем поле красным
//     emailError.textContent = message; // Устанавливаем текст ошибки
// }
//
// // Function to show general error
// function showGeneralError(message) {
//     const generalErrorMessage = document.getElementById('generalErrorMessage');
//
//     generalErrorMessage.textContent = message; // Устанавливаем текст ошибки
//     generalErrorMessage.classList.remove('d-none'); // Показываем сообщение об ошибке
// }
//
// // Main function to initialize the page
// function initializePage() {
//     const token = checkAuthentication();
//     addAuthorizationHeader(token);
//
//     const profileForm = document.getElementById('profileForm');
//     profileForm.addEventListener('submit', handleFormSubmit);
//
//     const passwordForm = document.getElementById('userPasswordForm');
//     passwordForm.addEventListener('submit', handleFormSubmit);
// }
//
// // Wait for the DOM to be fully loaded
// document.addEventListener('DOMContentLoaded', initializePage);

    function deleteUser(userId) {
    if (confirm('Вы уверены, что хотите удалить этого пользователя?')) {
    fetch(`/home/users/delete/${userId}`, {
    method: 'POST',
    headers: {
    'Content-Type': 'application/json'
}
})
    .then(response => {
    if (response.ok) {
    // Optionally refresh the page or remove the user from the UI
    location.reload(); // Reloads the page
} else {
    return response.json().then(errorData => {
    alert(errorData.message || 'Ошибка при удалении пользователя.');
});
}
})
    .catch(error => {
    console.error('Ошибка:', error);
    alert('Что-то пошло не так!');
});
}
}

    function updateRole(userId, role) {
    const form = document.createElement('form');
    form.method = 'post';
    form.action = `/home/users/update/${userId}`;

    const input = document.createElement('input');
    input.type = 'hidden';
    input.name = 'role';
    input.value = role;

    form.appendChild(input);
    document.body.appendChild(form);
    form.submit();
}

    document.getElementById('addUserForm').addEventListener('submit', async function(event) {
    const currentUserEmail = getUserId();
    event.preventDefault(); // Prevent default form submission

    const formData = new FormData(this);
    const jsonData = {};

    formData.forEach((value, key) => {
    jsonData[key] = value;
});

    try {
    const response = await fetch('/home/users/add', {
    method: 'POST',
    headers: {
    'Content-Type': 'application/json'
},
    body: JSON.stringify(jsonData)
});

    if (response.ok) {
    // Redirect to the profile of the current user
    window.location.href = `/home/users/${currentUserEmail}`; // Use .value to get the email
} else {
    const errorData = await response.json();
    handleErrors(errorData);
}
} catch (error) {
    console.error('Ошибка:', error);
    showGeneralError('Что-то пошло не так!'); // Handle network errors
}
});

    function handleErrors(errorData) {
    if (errorData.message) {
    showEmailError(errorData.message); // Отображаем ошибку для поля email
}
}

    function showEmailError(message) {
    const emailInput = document.getElementById('email');
    const emailError = document.getElementById('emailError');

    emailInput.classList.add('is-invalid'); // Выделяем поле красным
    emailError.textContent = message; // Устанавливаем текст ошибки
}

    function showGeneralError(message) {
    const generalErrorMessage = document.getElementById('generalErrorMessage');

    generalErrorMessage.textContent = message; // Устанавливаем текст ошибки
    generalErrorMessage.classList.remove('d-none'); // Показываем сообщение об ошибке
}

    // Existing handleFormSubmit function
    async function handleFormSubmit(event) {
    event.preventDefault();

    const form = event.target;
    const formData = new FormData(form);
    const jsonData = {};
    formData.forEach((value, key) => {
    jsonData[key] = value;
});

    try {
    const response = await fetch(form.action, {
    method: 'POST', // Ensure you're using the correct method
    headers: {
    'Content-Type': 'application/json'
},
    body: JSON.stringify(jsonData)
});

    if (response.ok) {
    alert('Профиль успешно обновлен');
} else {
    const error = await response.json();
    showError(error.message || 'Не удалось обновить профиль'); // Show error message in modal
}
} catch (error) {
    showError('Что-то пошло не так!'); // Show generic error message
    console.error('Ошибка:', error);
}
}

    // Function to check if the user is authenticated
    function checkAuthentication() {
    const token = localStorage.getItem('token');
    if (!token) {
    window.location.replace('/login');
}
    return token;
}

    // Function to add authorization header to fetch requests
    function addAuthorizationHeader(token) {
    const originalFetch = window.fetch;
    window.fetch = function(...args) {
    const [url, config = {}] = args;
    config.headers = {
    ...config.headers,
    'Authorization': `Bearer ${token}`
};
    return originalFetch(url, config);
};
}

    // Function to handle form submission
    async function handleFormSubmit(event) {
    event.preventDefault();

    const form = event.target;
    const formData = new FormData(form);
    const jsonData = {};
    formData.forEach((value, key) => {
    jsonData[key] = value;
});
    console.log(jsonData);
    try {
    const response = await fetch(form.action, {
    method: 'PUT',
    headers: {
    'Content-Type': 'application/json'
},
    body: JSON.stringify(jsonData)
});

    if (response.ok) {
    alert('Профиль успешно обновлен');
} else {
    const error = await response.json();
    alert(error.message || 'Не удалось обновить профиль');
}
} catch (error) {
    alert('Что-то пошло не так!');
    console.error('Ошибка:', error);
}
}

    // Main function to initialize the page
    function initializePage() {
    const token = checkAuthentication();
    addAuthorizationHeader(token);

    const profileForm = document.getElementById('profileForm');
    profileForm.addEventListener('submit', handleFormSubmit);

    const passwordForm = document.getElementById('userPasswordForm');
    passwordForm.addEventListener('submit', handleFormSubmit);
}

    // Wait for the DOM to be fully loaded
    document.addEventListener('DOMContentLoaded', initializePage);
