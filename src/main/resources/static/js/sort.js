document.addEventListener("DOMContentLoaded", function () {
    const sortDateBtn = document.getElementById("sortDateBtn");
    let ascending = true; // Флаг для переключения между восходящей и нисходящей сортировкой

    sortDateBtn.addEventListener("click", function () {


        const tableBody = document.getElementById("eventsTableBody");
        const rows = Array.from(tableBody.querySelectorAll("tr"));

        // Фильтруем строки с корректным data-date
        const validRows = rows.filter(row => row.querySelector("td[data-date]"));

        if (validRows.length === 0) {
            console.error("Нет строк с атрибутом data-date для сортировки");
            return;
        }

        // Логируем, какие строки будут сортироваться
        validRows.forEach(row => {
            const dateValue = row.querySelector("td[data-date]").getAttribute("data-date");

        });

        validRows.sort((a, b) => {
            const dateA = new Date(a.querySelector("td[data-date]").getAttribute("data-date"));
            const dateB = new Date(b.querySelector("td[data-date]").getAttribute("data-date"));
            if (isNaN(dateA) || isNaN(dateB)) {
                console.error("Некорректные даты для сортировки");
                return 0;
            }
            return ascending ? dateA - dateB : dateB - dateA;
        });

        // Очищаем tbody и добавляем отсортированные строки
        tableBody.innerHTML = "";
        validRows.forEach(row => tableBody.appendChild(row));

        ascending = !ascending; // Переключаем направление сортировки
    });
});
//
// function loadEventDetails(button) {
//     const eventId = button.getAttribute('data-event-id');
//     fetch(`/home/events/${eventId}/details`)
//         .then(response => response.json())
//         .then(data => {
//             const eventDetailsContent = document.getElementById('eventDetailsContent');
//             let detailsHtml = '<ul class="list-group">';
//             data.forEach(detail => {
//                 detailsHtml += `<li class="list-group-item">${detail.menuItemName}: ${detail.quantity} x ${detail.price} руб.</li>`;
//             });
//             detailsHtml += '</ul>';
//             eventDetailsContent.innerHTML = detailsHtml;
//         })
//         .catch(error => console.error('Error loading event details:', error));
// }
//
// document.getElementById('sortDateBtn').addEventListener('click', function() {
//     const tableBody = document.getElementById('eventsTableBody');
//     const rows = Array.from(tableBody.getElementsByTagName('tr'));
//     rows.sort((a, b) => {
//         const dateA = new Date(a.getAttribute('data-date'));
//         const dateB = new Date(b.getAttribute('data-date'));
//         return dateA - dateB;
//     });
//     rows.forEach(row => tableBody.appendChild(row));
// });