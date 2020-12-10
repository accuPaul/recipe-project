package sandbox.paul.recipeproject.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sandbox.paul.recipeproject.commands.UnitOfMeasureCommand;
import sandbox.paul.recipeproject.converters.UnitOfMeasureToUnitOfMeasureCommand;
import sandbox.paul.recipeproject.domain.UnitOfMeasure;
import sandbox.paul.recipeproject.repositories.UnitOfMeasureRepository;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UnitOfMeasureServiceImplTest {

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    UnitOfMeasureService unitOfMeasureService;
    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        unitOfMeasureService = new UnitOfMeasureServiceImpl(unitOfMeasureRepository, unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    public void listAllUoms() throws Exception {
        //given
        Set<UnitOfMeasure> unitOfMeasures = new HashSet<>();
        UnitOfMeasure testUom1 = new UnitOfMeasure();
        testUom1.setId(1L);
        unitOfMeasures.add(testUom1);

        UnitOfMeasure testUom2 = new UnitOfMeasure();
        testUom2.setId(2L);
        unitOfMeasures.add(testUom2);

        //when
        when(unitOfMeasureRepository.findAll()).thenReturn(unitOfMeasures);
        Set<UnitOfMeasureCommand> commands = unitOfMeasureService.listAllUoms();

        //then
        assertEquals(unitOfMeasures.size(), commands.size());
        verify(unitOfMeasureRepository, times(1)).findAll();
    }
}