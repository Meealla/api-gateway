package webapp.apigateway.application.service;


import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import webapp.apigateway.domain.model.Department;
import webapp.apigateway.domain.model.ResumeWithTemplate;
import webapp.resumeanalyzer.domain.model.Resume;
import webapp.resumegenerator.domain.model.Template;

/**
 * Сервис для получения Резюме с соответствущими Шаблонами
 */
@Service
public class ApiGatewayServiceImpl
{

    public <T> Mono<List<T>> makeRequest(String uri, Class<T> elementClass) {
        WebClient webClient = WebClient.create();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(elementClass)
                .collectList();
    }

    public Mono<List<ResumeWithTemplate>> getListOfResumeWithTemplates(Department department) {

        Mono<List<Template>> templates = makeRequest("http://localhost:8765/generator/templates/all?department=" + department, Template.class);
        Mono<List<Resume>> resumes = makeRequest("http://localhost:8765/analyzer/resumes/all?department=" + department, Resume.class);

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

    public Mono<List<ResumeWithTemplate>> getListOfResumeWithTemplates() {

        Mono<List<Template>> templates = makeRequest("http://localhost:8765/generator/templates/all", Template.class);
        Mono<List<Resume>> resumes = makeRequest("http://localhost:8765/analyzer/resumes/all", Resume.class);

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
}
