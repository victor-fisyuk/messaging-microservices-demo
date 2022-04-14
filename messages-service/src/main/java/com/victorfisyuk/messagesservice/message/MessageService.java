package com.victorfisyuk.messagesservice.message;

import java.util.List;

/**
 * Provides methods to work with messages.
 */
public interface MessageService {
    /**
     * Send a message from the sender to the recipient.
     *
     * @param message describes the message to be sent
     * @return the message sent
     */
    Message sendMessage(Message message);

    /**
     * Get the user inbox messages.
     *
     * @param userId user id
     * @return List of inbox messages.
     */
    List<Message> getInboxMessages(String userId);

    /**
     * Get the user outbox messages.
     *
     * @param userId user id
     * @return List of outbox messages.
     */
    List<Message> getOutboxMessages(String userId);
}
