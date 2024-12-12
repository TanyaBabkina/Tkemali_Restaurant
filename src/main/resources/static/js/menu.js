// Запрашиваем данные меню при загрузке страницы
document.addEventListener("DOMContentLoaded", function () {
    fetch('/home/load/menu')
        .then(response => response.json())
        .then(data => renderMenu(data))
        .catch(error => console.error("Ошибка загрузки меню:", error));
});

document.getElementById('searchForm').addEventListener('submit', function (event) {
    event.preventDefault(); // Отключаем стандартную отправку формы

    const formData = new FormData(this);

    fetch('/home/load/menu/search', {
        method: 'POST',
        body: formData,
    })
        .then(response => response.json())
        .then(data => renderMenu(data))
        .catch(error => console.error('Ошибка при поиске:', error));
});

// Функция для отображения меню
function renderMenu(menuItems) {
    const menuContainer = document.getElementById('menuContainer');
    menuContainer.innerHTML = '';

    menuItems.forEach(product => {
        const productCard = document.createElement('div');
        productCard.classList.add('col-md-4', 'mb-4');

        // Используем относительный путь к изображению
        const imageUrl = `/images/${product.imagePath}`;
        productCard.innerHTML = `
            <div class="card h-100">
                <img src="${imageUrl}" class="card-img-top" alt="${product.name}"
                     onerror="this.onerror=null; this.src='/images/placeholder.jpg';">
                <div class="card-body">
                    <h5 class="card-title">${product.name}</h5>
                    <h6>${product.category.name}</h6>
                    <p class="card-text">${product.description}</p>
                    <p class="card-text font-weight-bold">${product.price} руб.</p>
                </div>
                <div class="card-footer text-center">
                    <a href="/home/menu/edit/${product.id}" class="btn btn-primary">Редактировать</a>
                    <a href="/home/menu/delete/${product.id}" class="btn btn-danger">Удалить</a>
                </div>
            </div>
        `;
        menuContainer.appendChild(productCard);
    });
}


