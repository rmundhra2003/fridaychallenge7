package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Controller
public class MessageController {
    @Autowired
    UserService userService;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CloudinaryConfig cloudc;


    @RequestMapping("/list")
    public String listMessages(Model model) {
        User user = userService.getCurrentUser();
        // Gets the currently logged in user and maps it to "user" in the Thymeleaf template
        model.addAttribute("user", user );
        model.addAttribute("messages", messageRepository.findAll());
        return "list";
    }

    @RequestMapping("/mylist")
    public String mylistMessages(Model model) {
        User user = userService.getCurrentUser();
        // Gets the currently logged in user and maps it to "user" in the Thymeleaf template
        model.addAttribute("user", user );
        model.addAttribute("messages", messageRepository.findAllByUid(user.getId()));
        return "mylist";
    }

    String getDate() {
        ZonedDateTime dateTime = ZonedDateTime.now(); // gets the current date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String s = dateTime.format(formatter);
        return s;
    }

    @GetMapping("/add")
    public String messageForm(Model model) {

        User user = userService.getCurrentUser();
        Message message = new Message();
        // Gets the currently logged in user and maps it to "user" in the Thymeleaf template
        model.addAttribute("user", user );
        model.addAttribute("message", message);
        model.addAttribute("file", message.getUrl());
        return "messageform";
    }

    @PostMapping("/process")
    public String processForm(@Valid Message message, BindingResult result, Model model,
                              @RequestParam("file")MultipartFile file) {
        if (result.hasErrors()) {
            model.addAttribute("users", userRepository.findAll());
            return "messageform";
        }
        if(!file.isEmpty()) {
            try {
                Map uploadResult = cloudc.upload(file.getBytes(),
                        ObjectUtils.asMap("resourcetype", "auto"));
                message.setUrl(uploadResult.get("url").toString());
            }catch (IOException e) {
                e.printStackTrace();
                return "messageform";
            }
        }
            //return "messageform";

        message.setUid(userService.getCurrentUser().getId());
        message.setUser(userService.getCurrentUser());
       // message.setDate(getDate());
        System.out.println("The uid in message is "+message.getUid());


        messageRepository.save(message);
        return "redirect:/";
    }

//    @GetMapping("/addUser")
//    public String userForm(Model model) {
//        model.addAttribute("user", new User());
//        return "userform";
//    }
//
//    @PostMapping("/processUser")
//    public String processUser(@Valid User user, BindingResult result) {
//        if (result.hasErrors()) {
//            return "userform";
//        }
//        userRepository.save(user);
//        return "redirect:/";
//    }
    @RequestMapping("/detail/{id}")
    public String showMessage(@PathVariable("id") long id, Model model) {
        User user = userService.getCurrentUser();
        // Gets the currently logged in user and maps it to "user" in the Thymeleaf template
        model.addAttribute("user", user );
        messageRepository.findById(id).ifPresent(o -> model.addAttribute("message", o));
       // model.addAttribute("message", messageRepository.findById(id));

        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateMessage(@PathVariable("id") long id, Model model) {
        User user = userService.getCurrentUser();
        // Gets the currently logged in user and maps it to "user" in the Thymeleaf template
        model.addAttribute("user", user );
        //model.addAttribute("message", messageRepository.findById(id));
        messageRepository.findById(id).ifPresent(o -> model.addAttribute("message", o));
        Message msg = messageRepository.findByIdAndUid(id,userService.getCurrentUser().getId());

        if(msg != null) {
            System.out.println("Found message\n");
            return "messageform";
        }
        else
            return "redirect:/";
    }

    @RequestMapping("/delete/{id}")
    public String delMessage(@PathVariable("id") long id) {
        User user = userService.getCurrentUser();
        Message msg = messageRepository.findByIdAndUid(id,userService.getCurrentUser().getId());
        if(msg != null || (roleRepository.findByRoleAndUsersId("ADMIN", user.getId()) != null)) {
            messageRepository.deleteById(id);
            System.out.println("Found message\n");
        }

        return "redirect:/";
    }
}
