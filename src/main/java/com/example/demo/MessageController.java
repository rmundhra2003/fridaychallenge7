package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

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

    @GetMapping("/add")
    public String messageForm(Model model) {
        User user = userService.getCurrentUser();
        // Gets the currently logged in user and maps it to "user" in the Thymeleaf template
        model.addAttribute("user", user );
        model.addAttribute("message", new Message());
        return "messageform";
    }

    @PostMapping("/process")
    public String processForm(@Valid Message message, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userRepository.findAll());
            return "messageform";
        }
        message.setUid(userService.getCurrentUser().getId());
        message.setUser(userService.getCurrentUser());
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
        model.addAttribute("message", messageRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateMessage(@PathVariable("id") long id, Model model) {
        User user = userService.getCurrentUser();
        // Gets the currently logged in user and maps it to "user" in the Thymeleaf template
        model.addAttribute("user", user );
        model.addAttribute("message", messageRepository.findById(id));
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
