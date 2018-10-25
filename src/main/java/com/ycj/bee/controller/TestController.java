package com.ycj.bee.controller;

import com.ycj.bee.http.HttpHelper;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
@RequestMapping("/test")
public class TestController {

    String index = "http://rlzyggfwzx.bjchy.gov.cn/Residence/a/login";
    String validateCode = "http://rlzyggfwzx.bjchy.gov.cn/Residence/servlet/validateCodeServlet?" + System.currentTimeMillis();
    String login = "http://rlzyggfwzx.bjchy.gov.cn/Residence/a/login";


    @RequestMapping("/list")
    public Rendering listUser(Model model) {


        String data = HttpHelper.get(index);


        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:templates/test/temp.thymes");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            FileUtils.write(file, data, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Rendering rendering = Rendering.view("/test/list")
                                       .modelAttribute("test", "webflu")
                                       .build();

        return rendering;
    }

    @RequestMapping("/temp")
    public String temp() {
        return "/test/temp";
    }


}
