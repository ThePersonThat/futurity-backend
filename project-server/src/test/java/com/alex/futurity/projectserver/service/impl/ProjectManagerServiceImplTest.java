package com.alex.futurity.projectserver.service.impl;

import com.alex.futurity.projectserver.dto.CreationProjectRequestDTO;
import com.alex.futurity.projectserver.entity.Project;
import com.alex.futurity.projectserver.exception.ClientSideException;
import com.alex.futurity.projectserver.service.ProjectService;
import lombok.SneakyThrows;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProjectManagerServiceImplTest {
    @Mock
    private ProjectService mockService;
    @InjectMocks
    private ProjectManagerServiceImpl service;
    private static LogCaptor logCaptor;

    @BeforeAll
    static void setLogCaptor() {
        logCaptor = LogCaptor.forClass(ProjectManagerServiceImpl.class);
    }

    @BeforeEach
    void clearLogs() {
        logCaptor.clearLogs();
    }

    @AfterAll
    static void closeLogCaptor() {
        logCaptor.close();
    }

    private final CreationProjectRequestDTO dto =
            new CreationProjectRequestDTO("test", "description", mock(MultipartFile.class), 1L);

    @Test
    @DisplayName("Should throw an ClientSideException")
    void testIfProjectWithNameAlreadyExists() {
        when(mockService.hasUserProjectWithName(anyString(), anyLong())).thenReturn(true);
        assertThatThrownBy(() -> service.createProject(dto))
                .isInstanceOf(ClientSideException.class)
                .hasMessage("Project with such name exists");
    }

    @Test
    @SneakyThrows
    @DisplayName("Should create the project")
    void testCreationProject() {
        when(mockService.hasUserProjectWithName(anyString(), anyLong())).thenReturn(false);
        CreationProjectRequestDTO mockRequest = spy(dto);
        Project mockProject = mock(Project.class);
        doReturn(mockProject).when(mockRequest).toProject();
        String message = String.format("The project with name %s has been saved for user with %s id",
                mockProject.getName(), mockProject.getUserId());

        service.createProject(mockRequest);

        verify(mockService).saveProject(eq(mockProject));
        assertThat(logCaptor.getLogs())
                .hasSize(1)
                .contains(message);
    }

    @Test
    @SneakyThrows
    @DisplayName("Should throw an IllegalStateException")
    void testCreationProjectWithIOError() {
        when(mockService.hasUserProjectWithName(anyString(), anyLong())).thenReturn(false);
        CreationProjectRequestDTO mockRequest = spy(dto);
        String message = "Something went wrong";
        IOException exception = new IOException(message);
        doThrow(exception).when(mockRequest).toProject();

        assertThatThrownBy(() -> service.createProject(mockRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("The preview cannot be read");

        assertThat(logCaptor.getLogs())
                .hasSize(1)
                .contains(String.format("The preview %s cannot be read: %s", dto.getPreview(), message));
    }
}