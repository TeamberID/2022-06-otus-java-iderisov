package ru.otus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.service.ClientService;

@Controller
public class UserController {

    private final ClientService clientService;

    public UserController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping({"/", "/client/list"})
    public String clientsListView(Model model) {
        var clients = clientService.findAll();
        model.addAttribute("clients", clients);

        return "clientsList";
    }

    @PostMapping("/client")
    public RedirectView clientCreateView(@RequestParam String name) {
        clientService.saveClient(name);
        return new RedirectView("/", true);
    }


}
