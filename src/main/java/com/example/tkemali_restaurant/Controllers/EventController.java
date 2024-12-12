package com.example.tkemali_restaurant.Controllers;

import com.example.tkemali_restaurant.models.Event;
import com.example.tkemali_restaurant.models.EventPlanning;
import com.example.tkemali_restaurant.models.Menu;
import com.example.tkemali_restaurant.Service.EventService;
import com.example.tkemali_restaurant.Service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("home/events")
public class EventController {

    private final EventService eventService;
    private final MenuService menuService;

    public EventController(EventService eventService, MenuService menuService) {
        this.eventService = eventService;
        this.menuService = menuService;
    }
    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String listEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model, HttpServletRequest request) {
        Page<Event> eventPage = eventService.findAllPaginated(PageRequest.of(page, size));

        model.addAttribute("events", eventPage.getContent());
        model.addAttribute("currentPath", request.getRequestURI());

        model.addAttribute("totalPages", eventPage.getTotalPages());
        return "events/list";
    }


    @GetMapping("/add")
//    @PreAuthorize("hasRole('ADMIN')")
    public String showAddEventForm(Model model) {
        model.addAttribute("menuItems", menuService.getAllMenuItems());
        model.addAttribute("event", new Event());
        return "events/add";
    }

    @PostMapping("/add")
//    @PreAuthorize("hasRole('ADMIN')")
    public String addEvent(@Valid @ModelAttribute Event event, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("menuItems", menuService.getAllMenuItems());
            return "events/add";
        }

        try {
            // Обработка связанных записей event_planning
            for (EventPlanning planning : event.getEventPlannings()) {
                if (planning.getMenuId() == null) {
                    throw new IllegalArgumentException("Menu ID is required for event planning.");
                }
                Menu menuItem = menuService.getMenuById(planning.getMenuId());
                planning.setMenu(menuItem);
                planning.setEvent(event);
            }

            // Сохранение события
            eventService.saveEvent(event);
            return "redirect:/home/events";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Ошибка при сохранении мероприятия. Проверьте данные.");
            model.addAttribute("menuItems", menuService.getAllMenuItems());
            return "events/add";
        }
    }




    @GetMapping("/{id}/details")
//    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String showEventDetails(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + id));
        model.addAttribute("event", event);
        return "events/event-details :: details";
    }

    @GetMapping("/edit/{id}")
    public String editEvent(@PathVariable Long id, Model model) {
        Event event = eventService.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));

        model.addAttribute("event", event);
        model.addAttribute("menuItems", menuService.getAllMenuItems());

        return "events/edit";
    }

    @PostMapping("/edit/{id}")
    public String editEvent(@PathVariable Long id, @Valid @ModelAttribute Event event, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("menuItems", menuService.getAllMenuItems());
            return "events/edit";
        }

        try {
            Event existingEvent = eventService.getEventById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + id));

            existingEvent.setEventName(event.getEventName());
            existingEvent.setEventDate(event.getEventDate());
            existingEvent.setNumberOfGuests(event.getNumberOfGuests());
            existingEvent.setDescription(event.getDescription());

            // Обновляем планы мероприятия
            List<EventPlanning> updatedPlannings = new ArrayList<>();
            for (EventPlanning planning : event.getEventPlannings()) {
                if (planning.getMenuId() != null) {
                    Menu menu = menuService.getMenuById(planning.getMenuId());
                    planning.setMenu(menu);
                    planning.setEvent(existingEvent); // Устанавливаем связь с текущим событием
                    updatedPlannings.add(planning);
                }
            }

            // Удаляем старые и добавляем новые планы
            existingEvent.getEventPlannings().clear();
            existingEvent.getEventPlannings().addAll(updatedPlannings);

            eventService.saveEvent(existingEvent);
            return "redirect:/home/events";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/home/events/edit/" + id + "?error=true";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id); // Метод удаления в сервисе
        return "redirect:/home/events"; // Редирект на список событий
    }


}
