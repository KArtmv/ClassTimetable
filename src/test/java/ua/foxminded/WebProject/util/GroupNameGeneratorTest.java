package ua.foxminded.WebProject.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.random.RandomGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class GroupNameGeneratorTest {

    @Mock
    private RandomGenerator randomGenerator;
    @InjectMocks
    private GroupNameGenerator groupNameGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateGroupName() {
        when(randomGenerator.nextInt(26)).thenReturn(1, 2);
        when(randomGenerator.nextInt(10)).thenReturn(3, 4);

        assertThat(groupNameGenerator.generateGroupName()).isEqualTo("BC-34");

        verify(randomGenerator, times(2)).nextInt(26);
        verify(randomGenerator, times(2)).nextInt(10);
    }
}