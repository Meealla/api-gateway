package webapp.apigateway.application.service;


import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import webapp.apigateway.domain.model.Department;
import webapp.apigateway.domain.model.ResumeWithTemplate;
import webapp.resumeanalyzer.domain.model.Resume;
import webapp.resumegenerator.domain.model.Template;

import java.util.ArrayList;
import java.util.List;


@Service
public class ApiGatewayService {

    public Mono<List<ResumeWithTemplate>> getListOfResumeWithTemplates() {
        WebClient webClient = WebClient.create();
        Mono<List<Template>> templates = webClient.get()
                .uri("http://localhost:8765/generator/templates/all")
                .retrieve()
                .bodyToFlux(Template.class)
                .collectList();

        Mono<List<Resume>> resumes = webClient.get()
                .uri("http://localhost:8765/analyzer/resumes/all")
                .retrieve()
                .bodyToFlux(Resume.class)
                .collectList();

        return Mono.zip(resumes, templates)
                .map(tuple -> {
                    List<ResumeWithTemplate> result = new ArrayList<>();
                    for (Resume resume : tuple.getT1()) {
                        result.add(new ResumeWithTemplate(resume));
                        for (Template template : tuple.getT2()) {
                            if (resume.getDepartment().toString().equals(template.getDepartment().toString())) {
                                result.getLast().addTemplate(template);
                            }
                        }
                    }
                    return result;
                });

    }

    public Mono<List<ResumeWithTemplate>> getListOfResumeWithTemplates(Department department) {
        WebClient webClient = WebClient.create();
        Mono<List<Template>> templates = webClient.get()
                .uri("http://localhost:8765/generator/templates/all?department=" + department)
                .retrieve()
                .bodyToFlux(Template.class)
                .collectList();

        Mono<List<Resume>> resumes = webClient.get()
                .uri("http://localhost:8765/analyzer/resumes/all?department=" + department)
                .retrieve()
                .bodyToFlux(Resume.class)
                .collectList();

        return Mono.zip(resumes, templates)
                .map(tuple -> {
                    List<ResumeWithTemplate> result = new ArrayList<>();
                    for (Resume resume : tuple.getT1()) {
                        result.add(new ResumeWithTemplate(resume));
                        for (Template template : tuple.getT2()) {
                            result.getLast().addTemplate(template);
                        }
                    }
                    return result;
                });

    }
}
