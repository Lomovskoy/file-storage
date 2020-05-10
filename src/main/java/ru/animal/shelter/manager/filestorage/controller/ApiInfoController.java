package ru.animal.shelter.manager.filestorage.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@RequestMapping(path = "v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Controller
@ApiIgnore
class ApiInfoController {

    @GetMapping
    public String getInfo(){
        try {
            return "redirect:/swagger-ui.html";
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }
}