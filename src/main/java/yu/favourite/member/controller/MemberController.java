package yu.favourite.member.controller;

import org.springframework.web.bind.annotation.*;
import yu.favourite.base.rq.Rq;
import yu.favourite.base.rsData.RsData;
import yu.favourite.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/usr")
@RequiredArgsConstructor
public class MemberController {

    @PreAuthorize("isAnonymous()")
    @GetMapping("/member/login")
    public String showLogin() {
        return "usr/member/login";
    }

    @PreAuthorize("isAuthenticated()") // 로그인 해야만 접속가능
    @GetMapping("/home/main")
    public String showMe(Model model) {

        return "usr/home/main";
    }
}
