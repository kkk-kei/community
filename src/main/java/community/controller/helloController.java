package community.controller;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class helloController {

    @GetMapping("/hello")
    public String hello(@RequestParam(name="name",required = false,defaultValue = "world") String name,
                        Model model){
        model.addAttribute("name",name);
        return "hello";
    }
//    @RequestMapping("/hello")
//    @ResponseBody
//    public String hello(){
//        return "hello!";
//    }
}
