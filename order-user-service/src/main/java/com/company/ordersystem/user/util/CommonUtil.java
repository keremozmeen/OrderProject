package com.company.ordersystem.user.util; // Paket yolu artık User modülünde

import com.company.ordersystem.user.service.impl.UserDetailsImpl; // Aynı modül içinde olduğu için artık görebilir
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CommonUtil {

    public UserDetailsImpl loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Güvenli cast işlemi için kontrol ekledik
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            return (UserDetailsImpl) authentication.getPrincipal();
        }
        return null;
    }
}