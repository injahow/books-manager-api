package com.injahow.booksManager.controller;

import com.injahow.booksManager.bean.ResultVO;
import com.injahow.booksManager.bean.User;
import com.injahow.booksManager.bean.UserAuth;
import com.injahow.booksManager.service.UserAuthService;
import com.injahow.booksManager.utils.AuthUtil;
import io.jsonwebtoken.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/user")
@CrossOrigin
public class UserAuthController {

    @Resource
    private UserAuthService userAuthService;

    @PostMapping("/login")
    @ResponseBody
    public ResultVO login(@RequestBody Map<String, String> person) {

        UserAuth userAuthReq = new UserAuth();
        String username = person.get("username");
        String password = person.get("password");

        System.out.println(username+"-------"+password);

        if(password==null || username == null){
            return new ResultVO(500,"为空！",null);
        }
        userAuthReq.setAccount(username);
        userAuthReq.setPassword(password);

        User user = userAuthService.loginCheck(userAuthReq);

        if(user!=null) {
            Map map = new HashMap();
            String token = AuthUtil.getToken(user);
            map.put("token", token);
            return new ResultVO(200,null, map);
        }else {
            return new ResultVO(500, "???", null);
        }
    }

    @GetMapping("/info")
    @ResponseBody
    public ResultVO info(@Param("token") String token) {

        if(token == null){
            return new ResultVO( 500, "请先登录", null);
        }else {
            JwtParser parser = Jwts.parser();
            parser.setSigningKey(AuthUtil.SECRET);

            try {
                //System.out.println(token);
                Jws<Claims> headerClaimsJwt = parser.parseClaimsJws(token);
                Claims body = headerClaimsJwt.getBody();

                Map userInfo = new HashMap();

                userInfo.put("name", body.get("name", String.class));
                userInfo.put("introduction", body.get("introduction", String.class));
                userInfo.put("avatar", body.get("avatar", String.class));

                return new ResultVO(200, "登陆成功！", userInfo);
            } catch (Exception e) {
                return new ResultVO(500, "登录过期，请重新登录! ", null);
            }

        }

    }

    @PostMapping("/logout")
    @ResponseBody
    public ResultVO logout() {
        return new ResultVO(200, null, "success");
    }


}
