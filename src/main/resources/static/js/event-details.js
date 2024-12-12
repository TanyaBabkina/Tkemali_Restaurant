function loadEventDetails(button) {
    const eventId = button.getAttribute("data-event-id");
    const url = `/home/events/${eventId}/details`;

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error("Ошибка загрузки данных");
            }
            return response.text();
        })
        .then(html => {
            document.getElementById("eventDetailsContent").innerHTML = html;
        })
        .catch(error => {
            console.error("Ошибка:", error);
            document.getElementById("eventDetailsContent").innerHTML = "<p>Не удалось загрузить детали мероприятия.</p>";
        });
}
