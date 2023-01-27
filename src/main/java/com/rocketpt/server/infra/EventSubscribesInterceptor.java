package com.rocketpt.server.infra;

import com.rocketpt.server.common.DomainEvent;
import com.rocketpt.server.common.DomainEventPublisher;
import com.rocketpt.server.common.EventStore;
import com.rocketpt.server.sys.event.ResourceDeleted;
import com.rocketpt.server.sys.event.ResourceUpdated;
import com.rocketpt.server.sys.event.RoleDeleted;
import com.rocketpt.server.sys.event.RoleUpdated;
import com.rocketpt.server.sys.service.SessionService;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 通用事件处理拦截器，
 *
 * @author plexpt
 */
public class EventSubscribesInterceptor implements HandlerInterceptor {
    private final EventStore eventStore;
    private final SessionService sessionService;

    public EventSubscribesInterceptor(EventStore eventStore, SessionService sessionService) {
        this.eventStore = eventStore;
        this.sessionService = sessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {
        DomainEventPublisher.instance().reset();
        DomainEventPublisher.instance().asyncSubscribe(DomainEvent.class, eventStore::append);
        //发生以下事件, 刷新会话
        DomainEventPublisher.instance().subscribe(RoleUpdated.class,
                event -> sessionService.refresh());
        DomainEventPublisher.instance().subscribe(RoleDeleted.class,
                event -> sessionService.refresh());
        DomainEventPublisher.instance().subscribe(ResourceUpdated.class,
                event -> sessionService.refresh());
        DomainEventPublisher.instance().subscribe(ResourceDeleted.class,
                event -> sessionService.refresh());
        return true;
    }


}
