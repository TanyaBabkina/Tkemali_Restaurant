// Функция для очистки cookies
function clearLocalStorage() {
    localStorage.clear(); // Очищает все данные из localStorage
    console.log("localStorage очищен."); // Лог для подтверждения

}

// Функция для обработки выхода
function handleLogout() {
    clearLocalStorage(); // Очистка cookies
    window.location.href = '/logout'; // Перенаправление на главную страницу
}

// Привязка обработчика события к кнопке выхода
document.getElementById('logout').addEventListener('click', handleLogout);