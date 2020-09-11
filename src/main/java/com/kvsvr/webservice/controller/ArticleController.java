package com.kvsvr.webservice.controller;

import com.kvsvr.webservice.repository.model.Article;
import com.kvsvr.webservice.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.*;

@Controller
@Validated
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private MessageSource messageSource;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String index() {
        return "THIS IS THE HOME PAGE OF WEB SERVICE";
    }

    //Content Response format using content negotiation
    @RequestMapping(value = "/articles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Article> contentNegotiation() {
        return articleService.findAllArticles();
    }

    //Content Response format using url suffix
    @RequestMapping(value = "/articles-suffix", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public List<Article> findAllArticles() {
        return articleService.findAllArticles();
    }

    @RequestMapping(
            value = "/employee/{id}",
            produces = {"application/json", "application/xml"},
            method = RequestMethod.GET)
    public @ResponseBody
    List<Article> getEmployeeById(@PathVariable long id) {
        return articleService.findAllArticles();
    }

    //PathVariable with regex ({variable:regex})
    //+ sign is used when the case can happen several times
    @RequestMapping(value = "/regex/{name:[a-z]+}")
    @ResponseBody
    public String testPathVariableWithRegex(@PathVariable String name) {

        return name;
    }

    //Need @Validated on the controller to make it work.
    @RequestMapping(value = "/regex")
    @ResponseBody
    public String testRequestParamWithRegex(@Valid @Pattern(regexp = "[a-z]+") @RequestParam(required = false) String name) {

        return name;
    }

    //This is the fallback controller when we cannot find a suitable controller for the client request
    @RequestMapping("*")
    @ResponseBody
    public String notFound() {
        return "This resource cannot be found!";
    }


    //Consume = client request media type
    //Produce = the media type that the api can return to the clients' requests
    @RequestMapping(value = "/articles-consume-produce", consumes = MediaType.IMAGE_GIF_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public List<Article> articleConsumeProduce() {
        return articleService.findAllArticles();
    }


    // i18n using query parameter value using @PathVariable or @RequestPrams
    @RequestMapping(value = {"/language", "/language/{locale}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> multiLanguage(@PathVariable Optional<String> locale) {
        if (!locale.isPresent()) {
            locale = Optional.of("en");
        }
        Map<String, Object> passData = new HashMap<>();
        passData.put("name", "kongvungsovnareach");
        passData.put("school", "KSHRD");
        passData.put("language", messageSource.getMessage("name", null, new Locale(locale.get())));
        return passData;
    }

    @RequestMapping(value = "header", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> httpHeader() {
        Map<String, Object> passData = new HashMap<>();
        passData.put("name", messageSource.getMessage("name", null, LocaleContextHolder.getLocale()));
        return passData;
    }


    @RequestMapping(value = "/hateoas/allArticles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Article> hateoasController() {

        List<Article> articles = articleService.findAllArticles();
        for (Article article : articles) {
            Link link = ControllerLinkBuilder.linkTo(ArticleController.class).slash(article.getTitle()).withSelfRel();
            Link link1 = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(ArticleController.class).getEmployeeById(90)).withRel("employeeId");
            article.add(link);
            article.add(link1);
        }
        return articles;
    }
}
