package sandbox.paul.recipeproject.services;

import org.springframework.stereotype.Service;
import sandbox.paul.recipeproject.commands.UnitOfMeasureCommand;
import sandbox.paul.recipeproject.converters.UnitOfMeasureToUnitOfMeasureCommand;
import sandbox.paul.recipeproject.repositories.UnitOfMeasureRepository;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository unitOfMeasureRepository,
                                    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
    }


    @Override
    public Set<UnitOfMeasureCommand> listAllUoms() {

        return StreamSupport.stream(unitOfMeasureRepository.findAll()
        .spliterator(), false)
                .map(unitOfMeasureToUnitOfMeasureCommand::convert)
                .collect(Collectors.toSet());
    }
}
