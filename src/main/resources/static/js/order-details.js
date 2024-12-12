function loadOrderDetails(button) {
    const orderId = button.getAttribute("data-order-id");
    console.log(`Загрузка деталей для заказа ID: ${orderId}`);
    const modalContent = document.getElementById("orderDetailsContent");
    modalContent.innerHTML = `<div class="spinner-border text-primary" role="status">
                                  <span class="sr-only">Loading...</span>
                              </div>`;

    fetch(`/home/orders/${orderId}/details`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Ошибка загрузки деталей заказа ${orderId}`);
            }
            return response.json();
        })
        .then(details => {
            if (details.length > 0) {
                modalContent.innerHTML = `
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>Продукт</th>
                                <th>Количество</th>
                                <th>Цена</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${details.map(detail => `
                                <tr>
                                    <td>${detail.menuItemName || "Неизвестно"}</td>
                                    <td>${detail.quantity}</td>
                                    <td>${detail.price.toFixed(2)}</td>
                                </tr>
                            `).join('')}
                        </tbody>
                    </table>
                `;
            } else {
                modalContent.innerHTML = '<p>Детали заказа отсутствуют.</p>';
            }
        })
        .catch(error => {
            console.error("Ошибка:", error);
            modalContent.innerHTML = '<p>Ошибка загрузки деталей заказа.</p>';
        });
}
