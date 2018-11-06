package ar.com.tacsutn.grupo1.eventapp.telegram.callback.operation;

import ar.com.tacsutn.grupo1.eventapp.models.EventId;
import ar.com.tacsutn.grupo1.eventapp.models.EventList;
import ar.com.tacsutn.grupo1.eventapp.models.User;
import ar.com.tacsutn.grupo1.eventapp.services.EventListService;
import ar.com.tacsutn.grupo1.eventapp.services.EventService;
import ar.com.tacsutn.grupo1.eventapp.services.UserService;
import ar.com.tacsutn.grupo1.eventapp.telegram.TelegramBot;
import ar.com.tacsutn.grupo1.eventapp.telegram.callback.CallbackData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.Optional;

@Component
public class SelectedListCallbackOperation extends AuthenticatedCallbackOperation {

    private final EventService eventService;
    private final EventListService eventListService;

    @Autowired
    protected SelectedListCallbackOperation(
            UserService userService,
            EventService eventService,
            EventListService eventListService) {

        super(userService);
        this.eventService = eventService;
        this.eventListService = eventListService;
    }

    /**
     * Process a callback query of a selected list (and event) by the user.
     * @param bot the telegram bot.
     * @param callbackQuery the callback query with the selected list and event data.
     */
    @Override
    public void handle(TelegramBot bot, CallbackQuery callbackQuery, CallbackData callbackData) {
        EventId eventId = new EventId(callbackData.getEventId());
        String listId = callbackData.getListId();

        getUserOrAlert(bot, callbackQuery)
                .flatMap(user -> addEventToList(user, listId, eventId))
                .map(list -> answerAddedEventToList(callbackQuery, list))
                .ifPresent(answer -> makeRequest(bot, answer));
    }

    /**
     * Creates the response message request when the event is added successfully to the list.
     * @param callbackQuery the original callback query requested the add operation.
     * @param eventList the event list with the recently added event.
     * @return the response message request.
     */
    private SendMessage answerAddedEventToList(CallbackQuery callbackQuery, EventList eventList) {
        return new SendMessage()
                .setChatId(callbackQuery.getFrom().getId().longValue())
                .setText("El evento fue añadido a la lista \"" + eventList.getName() + "\".");
    }

    @Transactional
    private Optional<EventList> addEventToList(User user, String listId, EventId eventId) {
        try {
            eventService.save(eventId);
            return eventListService.addEvent(user, listId, eventId);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
