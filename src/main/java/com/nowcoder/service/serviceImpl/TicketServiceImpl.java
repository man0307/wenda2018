package com.nowcoder.service.serviceImpl;

import com.nowcoder.dao.LoginTicketMapper;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.service.TicketService;
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
