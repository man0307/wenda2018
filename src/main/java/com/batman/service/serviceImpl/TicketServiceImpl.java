package com.batman.service.serviceImpl;

import com.batman.dao.LoginTicketMapper;
import com.batman.model.LoginTicket;
import com.batman.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    LoginTicketMapper loginTicketMapper;

    @Override
    public void updateTicketStatusByTicket(String ticket, Integer status) {
        LoginTicket loginTicket=loginTicketMapper.selectByTicket(ticket);
        loginTicket.setStatus(status);
        loginTicketMapper.updateByPrimaryKey(loginTicket);
    }
}
