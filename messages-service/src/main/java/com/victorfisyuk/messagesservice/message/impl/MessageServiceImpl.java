package com.victorfisyuk.messagesservice.message.impl;

import com.victorfisyuk.messagesservice.message.Message;
import com.victorfisyuk.messagesservice.message.MessageRepository;
import com.victorfisyuk.messagesservice.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public List<Message> getInboxMessages(String userId) {
        return messageRepository.findAllByRecipientIdOrderBySentTimestampDesc(userId);
    }

    @Override
    public List<Message> getOutboxMessages(String userId) {
        return messageRepository.findAllBySenderIdOrderBySentTimestampDesc(userId);
    }

    @Override
    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }
}
