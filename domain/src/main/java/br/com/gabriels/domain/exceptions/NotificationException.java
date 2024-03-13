package br.com.gabriels.domain.exceptions;

import br.com.gabriels.domain.validation.handler.Notification;

public class NotificationException extends DomainException {
    public NotificationException(final Notification notification) {
        super("", notification.getErrors());
    }
}
