document.addEventListener("DOMContentLoaded", function () {
    let detailIndex = 1; // Начинаем с 1, так как первый элемент уже существует с индексом 0

    // Добавление новой детали заказа
    document.getElementById("addDetailButton").addEventListener("click", function () {
        const orderDetailsContainer = document.getElementById("orderDetailsContainer");

        const newDetail = document.createElement("div");
        newDetail.classList.add("order-detail", "row", "mb-3");

        newDetail.innerHTML = `
            <div class="col-md-5">
                <label for="menuItem${detailIndex}">Блюдо</label>
                <select id="menuItem${detailIndex}" name="orderDetails[${detailIndex}].menuItemId" class="form-control" required>
                    ${
            typeof menuItems !== 'undefined'
                ? menuItems.map(item => `<option value="${item.id}">${item.name}</option>`).join('')
                : '<option disabled>Загрузка меню...</option>'
        }
                </select>
            </div>
            <div class="col-md-3">
                <label for="quantity${detailIndex}">Количество</label>
                <input type="number" id="quantity${detailIndex}" name="orderDetails[${detailIndex}].quantity" class="form-control" min="1" required>
            </div>
            <div class="col-md-3">
                <label for="price${detailIndex}">Цена</label>
                <input type="text" id="price${detailIndex}" class="form-control" readonly value="0">
            </div>
            <div class="col-md-1 d-flex align-items-end">
                <button type="button" class="btn btn-danger btn-sm remove-detail-button">Удалить</button>
            </div>
        `;

        orderDetailsContainer.appendChild(newDetail);
        detailIndex++;
        updatePrices(); // Обновляем цены
    });

    // Удаление детали заказа
    document.getElementById("orderDetailsContainer").addEventListener("click", function (event) {
        if (event.target.classList.contains("remove-detail-button")) {
            const detailRow = event.target.closest(".order-detail");
            detailRow.remove();
            updatePrices(); // Обновляем цены
        }
    });

    // Функция обновления цены для всех блюд
    function updatePrices() {
        const details = document.querySelectorAll(".order-detail");
        details.forEach(detailRow => {
            const menuItemId = detailRow.querySelector("select").value;
            const quantity = parseInt(detailRow.querySelector("input[type='number']").value, 10) || 0;
            const priceField = detailRow.querySelector("input[type='text']");

            if (typeof menuItems !== 'undefined') {
                const menuItem = menuItems.find(item => item.id === parseInt(menuItemId, 10));
                if (menuItem) {
                    priceField.value = (menuItem.price * quantity).toFixed(2);
                }
            }
        });
    }

    // Обновление цены при изменении блюда или количества
    document.getElementById("orderDetailsContainer").addEventListener("change", function (event) {
        if (event.target.tagName === "SELECT" || event.target.type === "number") {
            updatePrices();
        }
    });

    // Валидация формы перед отправкой
    document.getElementById("addOrderForm").addEventListener("submit", function (event) {
        const orderDate = document.getElementById("orderDate").value;
        if (!orderDate) {
            alert("Пожалуйста, укажите дату заказа.");
            event.preventDefault();
            return;
        }

        const details = document.querySelectorAll(".order-detail");
        if (details.length === 0) {
            alert("Добавьте хотя бы одну деталь заказа.");
            event.preventDefault();
            return;
        }
    });
});

document.addEventListener('DOMContentLoaded', function() {
    console.log("DOM полностью загружен и разобран");
    const statusSelect = document.getElementById('status');

    // Проверка наличия элемента
    if (statusSelect) {
        console.log("Элемент status найден");
        updateOptionText();
    } else {
        console.log("Элемент status не найден");
    }

    // Функция для обновления текста опций
    function updateOptionText() {
        console.log("Функция updateOptionText вызвана");
        for (let option of statusSelect.options) {
            switch (option.value) {
                case 'В_ОЖИДАНИИ':
                    option.text = 'В ожидании';
                    break;
                case 'ЗАВЕРШЕН':
                    option.text = 'Завершен';
                    break;
                case 'ВЫПОЛНЯЕТСЯ':
                    option.text = 'Выполняется';
                    break;
                case 'ОТМЕНЕН':
                    option.text = 'Отменен';
                    break;
            }
        }
    }
});