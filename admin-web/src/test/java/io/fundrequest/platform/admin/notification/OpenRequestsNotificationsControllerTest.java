package io.fundrequest.platform.admin.notification;

import io.fundrequest.common.infrastructure.AbstractControllerTest;
import io.fundrequest.core.request.RequestService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class OpenRequestsNotificationsControllerTest extends AbstractControllerTest<OpenRequestsNotificationsController> {
    private RequestService requestService;
    private NotificationsTemplateService notificationsTemplateService;

    @Override
    protected OpenRequestsNotificationsController setupController() {
        requestService = mock(RequestService.class);
        notificationsTemplateService = mock(NotificationsTemplateService.class);
        return new OpenRequestsNotificationsController(notificationsTemplateService, requestService);
    }

    @Test
    void showGenerateTemplateForm() throws Exception {
        final HashSet<String> projects = new HashSet<>();
        final HashSet<String> technologies = new HashSet<>();

        when(requestService.findAllProjects()).thenReturn(projects);
        when(requestService.findAllTechnologies()).thenReturn(technologies);

        this.mockMvc.perform(get("/notifications/open-requests"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.view().name("notifications/open-requests"))
                    .andExpect(MockMvcResultMatchers.model().attribute("projects", sameInstance(projects)))
                    .andExpect(MockMvcResultMatchers.model().attribute("technologies", sameInstance(technologies)))
                    .andExpect(MockMvcResultMatchers.model().attribute("targetPlatforms", TargetPlatform.values()));
    }

    @Test
    void showGeneratedTemplate_allFilledIn() throws Exception {
        final HashSet<String> projects = new HashSet<>();
        final HashSet<String> technologies = new HashSet<>();
        final List<String> selectedProjects = Arrays.asList("FundRequest", "Cindercloud");
        final List<String> selectedTechnologies = Arrays.asList("Java", "HTML");
        final TargetPlatform selectedTargetPlatform = TargetPlatform.REDDIT;
        final String lastUpdatedSinceDays = "9";
        final String template = "dfhgjhkjlkgh";

        when(requestService.findAllProjects()).thenReturn(projects);
        when(requestService.findAllTechnologies()).thenReturn(technologies);
        when(notificationsTemplateService.generateOpenRequestsTemplateFor(selectedTargetPlatform, selectedProjects, selectedTechnologies, Long.valueOf(lastUpdatedSinceDays))).thenReturn(template);

        this.mockMvc.perform(get("/notifications/open-requests/template").param("projects", selectedProjects.get(0))
                                                                         .param("projects", selectedProjects.get(1))
                                                                         .param("technologies", selectedTechnologies.get(0))
                                                                         .param("technologies", selectedTechnologies.get(1))
                                                                         .param("last-updated", lastUpdatedSinceDays)
                                                                         .param("target-platform", selectedTargetPlatform.name()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.view().name("notifications/open-requests"))
                    .andExpect(MockMvcResultMatchers.model().attribute("projects", sameInstance(projects)))
                    .andExpect(MockMvcResultMatchers.model().attribute("technologies", sameInstance(technologies)))
                    .andExpect(MockMvcResultMatchers.model().attribute("targetPlatforms", TargetPlatform.values()))
                    .andExpect(MockMvcResultMatchers.model().attribute("template", template))
                    .andExpect(MockMvcResultMatchers.model().attribute("selectedProjects", selectedProjects))
                    .andExpect(MockMvcResultMatchers.model().attribute("selectedTechnologies", selectedTechnologies))
                    .andExpect(MockMvcResultMatchers.model().attribute("selectedTargetPlatform", selectedTargetPlatform))
                    .andExpect(MockMvcResultMatchers.model().attribute("lastUpdated", lastUpdatedSinceDays));
    }

    @Test
    void showGeneratedTemplate_noLastUpdatedSinceDays() throws Exception {
        final HashSet<String> projects = new HashSet<>();
        final HashSet<String> technologies = new HashSet<>();
        final List<String> selectedProjects = Arrays.asList("FundRequest", "Cindercloud");
        final List<String> selectedTechnologies = Arrays.asList("Java", "HTML");
        final TargetPlatform selectedTargetPlatform = TargetPlatform.EMAIL;
        final String template = "dfhgjhkjlkgh";

        when(requestService.findAllProjects()).thenReturn(projects);
        when(requestService.findAllTechnologies()).thenReturn(technologies);
        when(notificationsTemplateService.generateOpenRequestsTemplateFor(selectedTargetPlatform, selectedProjects, selectedTechnologies, 0L)).thenReturn(template);

        this.mockMvc.perform(get("/notifications/open-requests/template").param("projects", selectedProjects.get(0))
                                                                         .param("projects", selectedProjects.get(1))
                                                                         .param("technologies", selectedTechnologies.get(0))
                                                                         .param("technologies", selectedTechnologies.get(1))
                                                                         .param("target-platform", selectedTargetPlatform.name()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.view().name("notifications/open-requests"))
                    .andExpect(MockMvcResultMatchers.model().attribute("projects", sameInstance(projects)))
                    .andExpect(MockMvcResultMatchers.model().attribute("technologies", sameInstance(technologies)))
                    .andExpect(MockMvcResultMatchers.model().attribute("targetPlatforms", TargetPlatform.values()))
                    .andExpect(MockMvcResultMatchers.model().attribute("template", template))
                    .andExpect(MockMvcResultMatchers.model().attribute("selectedProjects", selectedProjects))
                    .andExpect(MockMvcResultMatchers.model().attribute("selectedTechnologies", selectedTechnologies))
                    .andExpect(MockMvcResultMatchers.model().attribute("selectedTargetPlatform", selectedTargetPlatform))
                    .andExpect(MockMvcResultMatchers.model().attribute("lastUpdated", ""));
    }

    @Test
    void showGeneratedTemplate_emptyLastUpdatedSinceDays() throws Exception {
        final HashSet<String> projects = new HashSet<>();
        final HashSet<String> technologies = new HashSet<>();
        final List<String> selectedProjects = Arrays.asList("FundRequest", "Cindercloud");
        final List<String> selectedTechnologies = Arrays.asList("Java", "HTML");
        final TargetPlatform selectedTargetPlatform = TargetPlatform.REDDIT;
        final String template = "dfhgjhkjlkgh";

        when(requestService.findAllProjects()).thenReturn(projects);
        when(requestService.findAllTechnologies()).thenReturn(technologies);
        when(notificationsTemplateService.generateOpenRequestsTemplateFor(selectedTargetPlatform, selectedProjects, selectedTechnologies, 0L)).thenReturn(template);

        this.mockMvc.perform(get("/notifications/open-requests/template").param("projects", selectedProjects.get(0))
                                                                         .param("projects", selectedProjects.get(1))
                                                                         .param("technologies", selectedTechnologies.get(0))
                                                                         .param("technologies", selectedTechnologies.get(1))
                                                                         .param("last-updated", "")
                                                                         .param("target-platform", selectedTargetPlatform.name()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.view().name("notifications/open-requests"))
                    .andExpect(MockMvcResultMatchers.model().attribute("projects", sameInstance(projects)))
                    .andExpect(MockMvcResultMatchers.model().attribute("technologies", sameInstance(technologies)))
                    .andExpect(MockMvcResultMatchers.model().attribute("targetPlatforms", TargetPlatform.values()))
                    .andExpect(MockMvcResultMatchers.model().attribute("template", template))
                    .andExpect(MockMvcResultMatchers.model().attribute("selectedProjects", selectedProjects))
                    .andExpect(MockMvcResultMatchers.model().attribute("selectedTechnologies", selectedTechnologies))
                    .andExpect(MockMvcResultMatchers.model().attribute("selectedTargetPlatform", selectedTargetPlatform))
                    .andExpect(MockMvcResultMatchers.model().attribute("lastUpdated", ""));
    }

    @Test
    void showGeneratedTemplate_noProjects() throws Exception {
        final HashSet<String> projects = new HashSet<>();
        final HashSet<String> technologies = new HashSet<>();
        final List<String> selectedTechnologies = Arrays.asList("Java", "HTML");
        final TargetPlatform selectedTargetPlatform = TargetPlatform.REDDIT;
        final String lastUpdatedSinceDays = "9";
        final String template = "dfhgjhkjlkgh";

        when(requestService.findAllProjects()).thenReturn(projects);
        when(requestService.findAllTechnologies()).thenReturn(technologies);
        when(notificationsTemplateService.generateOpenRequestsTemplateFor(selectedTargetPlatform,
                                                                          new ArrayList<>(), selectedTechnologies, Long.valueOf(lastUpdatedSinceDays))).thenReturn(template);

        this.mockMvc.perform(get("/notifications/open-requests/template").param("technologies", selectedTechnologies.get(0))
                                                                         .param("technologies", selectedTechnologies.get(1))
                                                                         .param("last-updated", lastUpdatedSinceDays)
                                                                         .param("target-platform", selectedTargetPlatform.name()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.view().name("notifications/open-requests"))
                    .andExpect(MockMvcResultMatchers.model().attribute("projects", sameInstance(projects)))
                    .andExpect(MockMvcResultMatchers.model().attribute("technologies", sameInstance(technologies)))
                    .andExpect(MockMvcResultMatchers.model().attribute("targetPlatforms", TargetPlatform.values()))
                    .andExpect(MockMvcResultMatchers.model().attribute("template", template))
                    .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("selectedProjects"))
                    .andExpect(MockMvcResultMatchers.model().attribute("selectedTechnologies", selectedTechnologies))
                    .andExpect(MockMvcResultMatchers.model().attribute("selectedTargetPlatform", selectedTargetPlatform))
                    .andExpect(MockMvcResultMatchers.model().attribute("lastUpdated", lastUpdatedSinceDays));
    }

    @Test
    void showGeneratedTemplate_noTechnologies() throws Exception {
        final HashSet<String> projects = new HashSet<>();
        final HashSet<String> technologies = new HashSet<>();
        final List<String> selectedProjects = Arrays.asList("FundRequest", "Cindercloud");
        final TargetPlatform selectedTargetPlatform = TargetPlatform.REDDIT;
        final String lastUpdatedSinceDays = "9";
        final String template = "dfhgjhkjlkgh";

        when(requestService.findAllProjects()).thenReturn(projects);
        when(requestService.findAllTechnologies()).thenReturn(technologies);
        when(notificationsTemplateService.generateOpenRequestsTemplateFor(selectedTargetPlatform,
                                                                          selectedProjects, new ArrayList<>(), Long.valueOf(lastUpdatedSinceDays))).thenReturn(template);

        this.mockMvc.perform(get("/notifications/open-requests/template").param("projects", selectedProjects.get(0))
                                                                         .param("projects", selectedProjects.get(1))
                                                                         .param("last-updated", lastUpdatedSinceDays)
                                                                         .param("target-platform", selectedTargetPlatform.name()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.view().name("notifications/open-requests"))
                    .andExpect(MockMvcResultMatchers.model().attribute("projects", sameInstance(projects)))
                    .andExpect(MockMvcResultMatchers.model().attribute("technologies", sameInstance(technologies)))
                    .andExpect(MockMvcResultMatchers.model().attribute("targetPlatforms", TargetPlatform.values()))
                    .andExpect(MockMvcResultMatchers.model().attribute("template", template))
                    .andExpect(MockMvcResultMatchers.model().attribute("selectedProjects", selectedProjects))
                    .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("selectedTechnologies"))
                    .andExpect(MockMvcResultMatchers.model().attribute("selectedTargetPlatform", selectedTargetPlatform))
                    .andExpect(MockMvcResultMatchers.model().attribute("lastUpdated", lastUpdatedSinceDays));
    }

    @Test
    void showGeneratedTemplate_onlyTargetPlatformSelected() throws Exception {
        final HashSet<String> projects = new HashSet<>();
        final HashSet<String> technologies = new HashSet<>();
        final TargetPlatform selectedTargetPlatform = TargetPlatform.REDDIT;
        final String template = "dfhgjhkjlkgh";

        when(requestService.findAllProjects()).thenReturn(projects);
        when(requestService.findAllTechnologies()).thenReturn(technologies);
        when(notificationsTemplateService.generateOpenRequestsTemplateFor(selectedTargetPlatform, new ArrayList<>(), new ArrayList<>(), 0L)).thenReturn(template);

        this.mockMvc.perform(get("/notifications/open-requests/template").param("last-updated", "")
                                                                         .param("target-platform", selectedTargetPlatform.name()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.view().name("notifications/open-requests"))
                    .andExpect(MockMvcResultMatchers.model().attribute("projects", sameInstance(projects)))
                    .andExpect(MockMvcResultMatchers.model().attribute("technologies", sameInstance(technologies)))
                    .andExpect(MockMvcResultMatchers.model().attribute("targetPlatforms", TargetPlatform.values()))
                    .andExpect(MockMvcResultMatchers.model().attribute("template", template))
                    .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("selectedProjects"))
                    .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("selectedTechnologies"))
                    .andExpect(MockMvcResultMatchers.model().attribute("selectedTargetPlatform", selectedTargetPlatform))
                    .andExpect(MockMvcResultMatchers.model().attribute("lastUpdated", ""));
    }

    @Test
    void showGeneratedTemplate_nothingFilledIn() throws Exception {
        this.mockMvc.perform(get("/notifications/open-requests/template").param("last-updated", ""))
                    .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}
