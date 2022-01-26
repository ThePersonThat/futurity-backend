package com.alex.futurity.projectserver.controller;

import com.alex.futurity.projectserver.ProjectServerApplication;
import com.alex.futurity.projectserver.dto.CreationProjectRequestDTO;
import com.alex.futurity.projectserver.dto.IdResponse;
import com.alex.futurity.projectserver.dto.ListResponse;
import com.alex.futurity.projectserver.dto.ProjectDTO;
import com.alex.futurity.projectserver.entity.Project;
import com.alex.futurity.projectserver.exception.ErrorMessage;
import com.alex.futurity.projectserver.repo.ProjectRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ProjectServerApplication.class)
@Testcontainers
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProjectControllerIntegrationTest {
    @LocalServerPort
    private int port;
    protected String url;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProjectRepository projectRepository;

    @Container
    private final static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withUsername("postgres")
                    .withPassword("root");

    private final static String validName = "Spring learning";
    private final static String validDescription = "Learning Spring boot";
    private final static MockMultipartFile validPreview =
            new MockMultipartFile("project", "project-preview.jpeg", MediaType.IMAGE_JPEG_VALUE, new byte[1]);

    @Test
    @SneakyThrows
    @DisplayName("Should create project if a project request is valid")
    void testCreateProject() {
        Long id = 1L;
        CreationProjectRequestDTO dto = new CreationProjectRequestDTO(validName, validDescription, null, null);
        ResponseEntity<IdResponse> response =
                restTemplate.postForEntity(url + "/" + id + "/create", buildMultiPartHttpEntity(dto, validPreview),
                        IdResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Project project = getProject();

        assertThat(project.getId()).isEqualTo(response.getBody().getId());
        assertThat(project.getName()).isEqualTo(validName);
        assertThat(project.getUserId()).isEqualTo(id);
        assertThat(project.getDescription()).isEqualTo(validDescription);
        assertThat(project.getPreview()).isEqualTo(validPreview.getBytes());
    }

    @ParameterizedTest
    @MethodSource("getInvalidProjectRequests")
    @SneakyThrows
    @DisplayName("Should return error if a project request is invalid")
    void testCreateProjectWithInvalidRequest(CreationProjectRequestDTO dto, MockMultipartFile preview, ErrorMessage message) {
        ResponseEntity<ErrorMessage> response =
                restTemplate.postForEntity(url + "/1/create", buildMultiPartHttpEntity(dto, preview), ErrorMessage.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(message);
    }

    @Test
    @SneakyThrows
    @DisplayName("Should return error if the project with the same name exist")
    void testCreateProjectWithExistingName() {
        long id = 1L;
        Project project = new Project(id, validName, validDescription, validPreview.getBytes());
        createProject(project);
        CreationProjectRequestDTO dto = new CreationProjectRequestDTO(validName, validDescription, null, null);
        ResponseEntity<ErrorMessage> response =
                restTemplate.postForEntity(url + "/" + id + "/create", buildMultiPartHttpEntity(dto, validPreview), ErrorMessage.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isEqualTo(new ErrorMessage("Project with such name exists"));
    }

    @Test
    @SneakyThrows
    @DisplayName("Should return projects by id")
    void testGetProjectsByUserId() {
        long id = 1L;
        Project project = new Project(id, validName, validDescription, validPreview.getBytes());
        createProject(project);
        long projectId = getProject().getId();
        ResponseEntity<ListResponse<ProjectDTO>> response =
                restTemplate.exchange(url + "/" + id + "/projects", HttpMethod.GET, null,
                        new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<ProjectDTO> projects = (List<ProjectDTO>) response.getBody().getValues();
        assertThat(projects).hasSize(1);
        ProjectDTO projectDTO = projects.get(0);
        assertThat(projectDTO.getId()).isEqualTo(id);
        assertThat(projectDTO.getDescription()).isEqualTo(validDescription);
        assertThat(projectDTO.getName()).isEqualTo(validName);
        assertThat(projectDTO.getPreviewUrl()).isEqualTo("/preview/" + projectId);
    }

    @Test
    @SneakyThrows
    @DisplayName("Should load the preview")
    void testGetPreview() {
        long id = 1L;
        Project project = new Project(id, validName, validDescription, validPreview.getBytes());
        createProject(project);
        long projectId = getProject().getId();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        ResponseEntity<byte[]> response =
                restTemplate.exchange(url + "/" + id + "/preview/" + projectId, HttpMethod.GET, new HttpEntity<>(headers), byte[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(validPreview.getBytes());
    }

    @Test
    @SneakyThrows
    @DisplayName("Should return error if review is not found")
    void testGetNotExistingPreview() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        ResponseEntity<ErrorMessage> response =
                restTemplate.exchange(url + "/1/preview/1", HttpMethod.GET, new HttpEntity<>(headers), ErrorMessage.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(new ErrorMessage("The project is associated with such data does not exist"));
    }

    @Test
    @SneakyThrows
    @DisplayName("Should delete project if it exists")
    void testDeleteProject() {
        long id = 1L;
        Project project = new Project(id, validName, validDescription, validPreview.getBytes());
        createProject(project);
        long projectId = getProject().getId();

        ResponseEntity<String> response =
                restTemplate.exchange(url + "/" + id + "/delete/" + projectId, HttpMethod.DELETE, null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Project> projects = projectRepository.findAll();
        assertThat(projects).hasSize(0);
    }

    @Test
    @SneakyThrows
    @DisplayName("Should return an error if a project is not found")
    void testDeleteNotExistingProject() {
        ResponseEntity<ErrorMessage> response =
                restTemplate.exchange(url + "/1/delete/1", HttpMethod.DELETE, null, ErrorMessage.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody())
                .isEqualTo(new ErrorMessage("The project is associated with such data does not exist"));
    }

    private static Stream<Arguments> getInvalidProjectRequests() {
        MockMultipartFile fileEmpty = new MockMultipartFile("preview", "preview.jpeg", MediaType.IMAGE_JPEG_VALUE,
                new byte[0]);
        MockMultipartFile largeFile = new MockMultipartFile("preview", "preview.jpeg", MediaType.IMAGE_JPEG_VALUE,
                new byte[6 * (1024 * 1024)]);
        MockMultipartFile wrongType = new MockMultipartFile("preview", "preview.docx", MediaType.IMAGE_JPEG_VALUE,
                new byte[10]);
        MockMultipartFile withoutType = new MockMultipartFile("preview", "preview", MediaType.IMAGE_JPEG_VALUE,
                new byte[10]);

        CreationProjectRequestDTO validDto = new CreationProjectRequestDTO(validName, validDescription,
                null, null);

        // invalid names
        CreationProjectRequestDTO dtoWithNullName = new CreationProjectRequestDTO(null, validDescription, null, null);
        CreationProjectRequestDTO dtoWithEmptyName = new CreationProjectRequestDTO("", validDescription, null, null);
        CreationProjectRequestDTO dtoWithBlankName = new CreationProjectRequestDTO("      ", validDescription, null, null);

        // invalid descriptions
        CreationProjectRequestDTO dtoWithNullDescription = new CreationProjectRequestDTO(validName, null, null, null);
        CreationProjectRequestDTO dtoWithEmptyDescription = new CreationProjectRequestDTO(validName, "", null, null);
        CreationProjectRequestDTO dtoWithBlankDescription = new CreationProjectRequestDTO(validName, "      ", null, null);

        return Stream.of(
                // invalid files
                Arguments.of(validDto, fileEmpty, new ErrorMessage("Preview must not be empty")),
                Arguments.of(validDto, largeFile, new ErrorMessage("Preview is too large. Max size 5MB")),
                Arguments.of(validDto, wrongType, new ErrorMessage("Wrong image type. Must be one of the following: .jpeg, .png, .gif")),
                Arguments.of(validDto, withoutType, new ErrorMessage("Wrong image type. Must be one of the following: .jpeg, .png, .gif")),

                // invalid names
                Arguments.of(dtoWithNullName, validPreview, new ErrorMessage("Wrong name. Name must not be empty")),
                Arguments.of(dtoWithEmptyName, validPreview, new ErrorMessage("Wrong name. Name must not be empty")),
                Arguments.of(dtoWithBlankName, validPreview, new ErrorMessage("Wrong name. Name must not be empty")),

                // invalid descriptions
                Arguments.of(dtoWithNullDescription, validPreview, new ErrorMessage("Wrong description. Description must not be empty")),
                Arguments.of(dtoWithEmptyDescription, validPreview, new ErrorMessage("Wrong description. Description must not be empty")),
                Arguments.of(dtoWithBlankDescription, validPreview, new ErrorMessage("Wrong description. Description must not be empty"))
        );
    }

    protected HttpEntity<MultiValueMap<String, Object>> buildMultiPartHttpEntity(Object dto, MockMultipartFile preview) {
        Map<String, List<Object>> fields = Map.of(
                "preview", List.of(preview.getResource()), "project", List.of(dto)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>(fields);

        return new HttpEntity<>(body, headers);
    }

    private void createProject(Project project) {
        projectRepository.save(project);
    }

    private Project getProject() {
        List<Project> projects = projectRepository.findAll();
        assertThat(projects).hasSize(1);

        return projects.get(0);
    }

    @PostConstruct
    private void initHost() {
        url = "http://localhost:" + port;
    }

    @DynamicPropertySource
    public static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
    }
}