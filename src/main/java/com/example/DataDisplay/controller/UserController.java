package com.example.DataDisplay.controller;

import com.example.DataDisplay.model.User;
import com.example.DataDisplay.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public String getUsers(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        // Calculate the page number (0-based) and number of items per page
        PageRequest pageRequest = PageRequest.of(page, size);

        // Retrieve a Page of users
        Page<User> userPage = userRepository.findAll(pageRequest);
        // Populate the model with the Page of users
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("user", new User());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());

        return "users";
    }

//    @PostMapping("/users")
//    public String createUser(User user) {
//        userRepository.save(user);
//        return "redirect:/users"; // Redirect to the user list page
//    }

    @PostMapping("/users")
    public String createUser(@Valid User user,  Model model) {
        model.addAttribute("user", user);
            // Save the valid user data
        userRepository.save(user);
        return "redirect:/users"; // Redirect to the user list
    }



    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        // Find the user by ID
        Optional<User> optionalUser = userRepository.findById(id);

        // Check if the user exists
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Populate the model with user data for the form
            model.addAttribute("user", user);

            // Return the "edit-user" template for editing
            return "edit-user";
        } else {
            return "redirect:/users";
        }
    }

    @PostMapping("/users/save/{id}")
    public String saveChanges(@PathVariable Long id, @ModelAttribute("user") User updatedUser) {
        // Find the user by ID
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Update the user's data
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());

            // Save the updated user to the database
            userRepository.save(user);
        }

        return "redirect:/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        // Find the user by ID
        Optional<User> optionalUser = userRepository.findById(id);

        // Check if the user exists
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Delete the user
            userRepository.delete(user);
        }

        // Redirect to the user list page
        return "redirect:/users";
    }
}



