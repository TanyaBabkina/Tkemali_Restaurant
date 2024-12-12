document.addEventListener("DOMContentLoaded", function () {
    let planIndex = 1; // Начальный индекс для новых планов

    document.getElementById("addPlanningButton").addEventListener("click", function () {
        const container = document.getElementById("eventPlanningContainer");

        const newPlan = document.createElement("div");
        newPlan.classList.add("event-planning", "row", "mb-3");

        newPlan.innerHTML = `
            <div class="col-md-6">
                <label for="menuItem${planIndex}">Меню</label>
                <select id="menuItem${planIndex}" name="eventPlannings[${planIndex}].menuId" class="form-control menu-select" required>
                    ${
            menuItems.map(item => `<option value="${item.id}">${item.name}</option>`).join('')
        }
                </select>
            </div>
            <div class="col-md-3">
                <label for="quantity${planIndex}">Количество</label>
                <input type="number" id="quantity${planIndex}" name="eventPlannings[${planIndex}].quantity" class="form-control quantity-input" min="1" required>
            </div>
            <div class="col-md-3">
                <button type="button" class="btn btn-danger btn-sm remove-plan-button mt-4">Удалить</button>
            </div>
        `;

        container.appendChild(newPlan);
        planIndex++;
    });

    // Удаление плана
    document.getElementById("eventPlanningContainer").addEventListener("click", function (event) {
        if (event.target.classList.contains("remove-plan-button")) {
            const planRow = event.target.closest(".event-planning");
            planRow.remove();
        }
    });
});

document.getElementById("eventPlanningContainer").addEventListener("click", function (event) {
    if (event.target.classList.contains("remove-plan-button")) {
        const planRow = event.target.closest(".event-planning");
        planRow.remove();

        // Обновляем индексы всех оставшихся записей
        const rows = document.querySelectorAll(".event-planning");
        rows.forEach((row, index) => {
            row.querySelector(".menu-select").setAttribute("name", `eventPlannings[${index}].menuId`);
            row.querySelector(".menu-select").setAttribute("id", `menuItem${index}`);
            row.querySelector(".quantity-input").setAttribute("name", `eventPlannings[${index}].quantity`);
            row.querySelector(".quantity-input").setAttribute("id", `quantity${index}`);
        });
    }
});

