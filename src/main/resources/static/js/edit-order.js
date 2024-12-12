document.addEventListener("DOMContentLoaded", function () {
    let detailIndex = document.querySelectorAll(".order-detail").length;

    // Добавление новой детали заказа
    document.getElementById("addDetailButton").addEventListener("click", function () {
        const orderDetailsContainer = document.getElementById("orderDetailsContainer");

        const newDetail = document.createElement("div");
        newDetail.classList.add("order-detail", "row", "mb-3");

        newDetail.innerHTML = `
            <div class="col-md-5">
                <label for="menuItem${detailIndex}">Блюдо</label>
                <select id="menuItem${detailIndex}" name="orderDetails[${detailIndex}].menuItemId" class="form-control" required>
                    ${menuItems.map(item => `<option value="${item.id}">${item.name}</option>`).join('')}
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
        updatePrices();
    });

    // Удаление детали заказа
    document.getElementById("orderDetailsContainer").addEventListener("click", function (event) {
        if (event.target.classList.contains("remove-detail-button")) {
            const detailRow = event.target.closest(".order-detail");
            detailRow.remove();
            updatePrices();
        }
    });

    // Функция обновления цен
    function updatePrices() {
        document.querySelectorAll(".order-detail").forEach(detailRow => {
            const menuItemId = detailRow.querySelector("select").value;
            const quantity = parseInt(detailRow.querySelector("input[type='number']").value, 10) || 0;
            const priceField = detailRow.querySelector("input[type='text']");

            const menuItem = menuItems.find(item => item.id === parseInt(menuItemId, 10));
            if (menuItem) {
                priceField.value = (menuItem.price * quantity).toFixed(2);
            }
        });
    }

    // Обновление цен при изменении блюда или количества
    document.getElementById("orderDetailsContainer").addEventListener("change", function (event) {
        if (event.target.tagName === "SELECT" || event.target.type === "number") {
            updatePrices();
        }
    });
});

function reindexDetails() {
    document.querySelectorAll(".order-detail").forEach((detailRow, index) => {
        detailRow.querySelector("select").setAttribute("name", `orderDetails[${index}].menuItemId`);
        detailRow.querySelector("input[type='number']").setAttribute("name", `orderDetails[${index}].quantity`);
        detailRow.querySelector("input[type='text']").setAttribute("id", `price${index}`);
    });
}
document.getElementById("orderDetailsContainer").addEventListener("click", function (event) {
    if (event.target.classList.contains("remove-detail-button")) {
        const detailRow = event.target.closest(".order-detail");
        detailRow.remove();
        reindexDetails();
        updatePrices();
    }
});

document.getElementById("editOrderForm").addEventListener("submit", function (event) {
    const details = document.querySelectorAll(".order-detail");
    if (details.length === 0) {
        alert("Добавьте хотя бы одну деталь заказа.");
        event.preventDefault();
    }
});
document.getElementById("editOrderForm").addEventListener("submit", function (event) {
    const formData = new FormData(this);
    for (let [key, value] of formData.entries()) {
        console.log(`${key}: ${value}`);
    }
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