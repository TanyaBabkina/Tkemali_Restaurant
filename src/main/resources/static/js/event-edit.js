document.addEventListener('DOMContentLoaded', function () {
    console.log("Menu items:", menuItems); // Проверка в консоли

    const addPlanningButton = document.getElementById('addPlanningButton');
    const eventPlanningContainer = document.getElementById('eventPlanningContainer');

    addPlanningButton.addEventListener('click', function () {
        const index = eventPlanningContainer.querySelectorAll('.event-planning').length;

        if (menuItems.length === 0) {
            alert("Список меню пуст. Добавьте элементы меню перед планированием.");
            return;
        }

        const planningHtml = `
      <div class="event-planning row mb-3">
        <div class="col-md-6">
          <label for="menuItem${index}">Меню</label>
          <select id="menuItem${index}" name="eventPlannings[${index}].menuId" class="form-control" required>
            <option value="" disabled selected>Выберите элемент меню</option>
            ${menuItems.map(item => `<option value="${item.id}">${item.name}</option>`).join('')}
          </select>
        </div>
        <div class="col-md-3">
          <label for="quantity${index}">Количество</label>
          <input type="number" id="quantity${index}" name="eventPlannings[${index}].quantity" class="form-control" min="1" required>
        </div>
        <div class="col-md-3 d-flex align-items-end">
          <button type="button" class="btn btn-danger btn-sm remove-planning-button">Удалить</button>
        </div>
      </div>
    `;

        eventPlanningContainer.insertAdjacentHTML('beforeend', planningHtml);
    });

    eventPlanningContainer.addEventListener('click', function (e) {
        if (e.target.classList.contains('remove-planning-button')) {
            e.target.closest('.event-planning').remove();
        }
    });
});
