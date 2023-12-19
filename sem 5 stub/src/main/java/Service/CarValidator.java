package Service;

import Domain.Car;
import Repository.IRepository;

public class CarValidator extends AbstractValidator<Car>{
    public CarValidator(IRepository<Car> repository) {
        super(repository);
    }
}
